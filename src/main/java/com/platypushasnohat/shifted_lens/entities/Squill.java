package com.platypushasnohat.shifted_lens.entities;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Squill extends PathfinderMob implements FlyingAnimal {

    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Squill.class, EntityDataSerializers.BOOLEAN);

    private Vec3 prevPull = Vec3.ZERO, pull = Vec3.ZERO;
    private float alphaProgress;
    private float prevAlphaProgress;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState aggroAnimationState = new AnimationState();

    public Squill(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SquillMoveController(this);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.18F)
                .add(Attributes.FLYING_SPEED, 0.18F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SquillAttackGoal(this));
        this.goalSelector.addGoal(2, new SquillSchoolGoal(this));
        this.goalSelector.addGoal(3, new AirSwimGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, pLevel) {
            public boolean isStableDestination(BlockPos pos) {
                return !level().getBlockState(pos.below(100)).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    public void updatePull(Vec3 pos) {
        this.prevPull = this.pull = pos.subtract(0.0F, 1.0F, 0.0F);
    }

    public static Vec3 lerp(Vec3 prev, Vec3 current, float ptc) {
        return prev.add(current.subtract(prev).scale(ptc));
    }

    public Vec3 getPull(float partialTicks) {
        return lerp(this.prevPull, this.pull, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();

        prevAlphaProgress = alphaProgress;

        if (this.isAlive() && alphaProgress < 20F) {
            alphaProgress++;
        }
        if (!this.isAlive() && alphaProgress > 0F) {
            alphaProgress--;
        }

        if (this.level().isClientSide()) {
            setupAnimationStates();

            this.prevPull = this.pull;
            Vec3 pos = this.position();
            this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.25F));
        }
    }

    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.isAlive() && !this.isAttacking(), this.tickCount);
        this.aggroAnimationState.animateWhen(this.isAlive() && this.isAttacking(), this.tickCount);
    }

    public float getAlphaProgress(float partialTicks) {
        return (prevAlphaProgress + (alphaProgress - prevAlphaProgress) * partialTicks) * 0.05F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Attacking", this.isAttacking());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setAttacking(compoundTag.getBoolean("Attacking"));
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3, 3, 3);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0D;
    }

    // goals
    static class AirSwimGoal extends Goal {

        private final Squill squill;
        private int cooldown;
        private double x, y, z;

        public AirSwimGoal(Squill squill) {
            this.squill = squill;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (this.cooldown > 0) {
                this.cooldown--;
            } else {
                Level level = squill.level();
                int blockX = squill.getBlockX();
                int blockZ = squill.getBlockZ();
                int heightBelow = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockX, blockZ);
                boolean isAirBelow = level.getBlockState(new BlockPos(blockX, heightBelow, blockZ)).isAir();
                int blockY = squill.getBlockY();
                Vec3 randomPos;
                if ((!isAirBelow && blockY - heightBelow > 15) || (isAirBelow && blockY > level.getSeaLevel() + 15)) {
                    RandomSource random = squill.getRandom();
                    randomPos = squill.position().add(new Vec3(random.nextBoolean() ? (random.nextInt(12)) : (random.nextInt(12) * -1), -random.nextInt(16), random.nextBoolean() ? (random.nextInt(12)) : (random.nextInt(12) * -1)));
                } else {
                    Vec3 view = squill.getViewVector(0.0F);
                    randomPos = HoverRandomPos.getPos(squill, 32, 16, view.x, view.z, ((float) Math.PI / 2F), 3, 1);
                }
                if (randomPos != null) {
                    this.x = randomPos.x();
                    this.y = randomPos.y();
                    this.z = randomPos.z();
                    return true;
                }
            }
            return false;
        }

        @Override
        public void start() {
            squill.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0F);
            this.cooldown = squill.getRandom().nextInt(40) + 20;
        }

        @Override
        public boolean canContinueToUse() {
            return this.squill.getMoveControl().hasWanted();
        }

        @Override
        public void stop() {
            this.squill.getNavigation().stop();
        }
    }

    static class SquillAttackGoal extends Goal {

        private Squill squill;

        private Vec3 startOrbitFrom;
        private int orbitTime;
        private int maxOrbitTime;
        private int attackTime;

        public SquillAttackGoal(Squill entity) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
            this.squill = entity;
        }

        @Override
        public void start() {
            orbitTime = 0;
            maxOrbitTime = 80;
            startOrbitFrom = null;
            squill.setAttacking(false);
        }

        @Override
        public void stop() {
            LivingEntity target = squill.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
                squill.setTarget(null);
            }
            squill.setAggressive(false);
            squill.setAttacking(false);
            squill.getNavigation().stop();
        }

        @Override
        public boolean canUse() {
            LivingEntity target = squill.getTarget();
            return target != null && target.isAlive() && !squill.isPassenger();
        }

        @Override
        public boolean canContinueToUse() {
            LivingEntity target = squill.getTarget();
            if (target == null) {
                return false;
            } else if (!target.isAlive()) {
                return false;
            } else if (!squill.isWithinRestriction(target.blockPosition())) {
                return false;
            } else {
                return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative() || !squill.getNavigation().isDone();
            }
        }

        public void tick() {
            LivingEntity target = squill.getTarget();
            if (target != null && target.isAlive()) {
                double distance = squill.distanceTo(target);
                if (startOrbitFrom == null) {
                    squill.getNavigation().moveTo(target, 4D);
                    squill.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
                } else if (orbitTime < maxOrbitTime) {
                    orbitTime++;
                    float zoomIn = 1F - orbitTime / (float) maxOrbitTime;
                    Vec3 orbitPos = orbitAroundPos(10.0F + zoomIn * 5.0F).add(0, 4 + zoomIn * 3, 0);
                    squill.getNavigation().moveTo(orbitPos.x, orbitPos.y, orbitPos.z, 3D);
                    squill.lookAt(EntityAnchorArgument.Anchor.EYES, orbitPos);
                } else {
                    orbitTime = 0;
                    startOrbitFrom = null;
                }
                if (distance <= 16.0D && orbitTime <= 0) {
                    tickAttack();
                }
            }
        }

        protected void tickAttack() {
            attackTime++;
            LivingEntity target = squill.getTarget();
            double distance = squill.distanceTo(target);
            float attackReach = squill.getBbWidth() + target.getBbWidth();
            squill.setAttacking(true);

            if (attackTime <= 4) {
                squill.getNavigation().stop();
                squill.lookAt(Objects.requireNonNull(target), 360F, 360F);
                squill.getLookControl().setLookAt(target, 360F, 360F);
            }

            if (attackTime > 8 && attackTime <= 24) {
                if (distance < attackReach + 0.5D) {
                    squill.doHurtTarget(target);
                    squill.swing(InteractionHand.MAIN_HAND);
                    maxOrbitTime = 40 + squill.getRandom().nextInt(60);
                    startOrbitFrom = target.getEyePosition();
                    attackTime = 0;
                    squill.setAttacking(false);
                }
            } else if (attackTime > 24) {
                maxOrbitTime = 40 + squill.getRandom().nextInt(60);
                startOrbitFrom = target.getEyePosition();
                attackTime = 0;
                squill.setAttacking(false);
            }
        }

        public Vec3 orbitAroundPos(float circleDistance) {
            final float angle = 3 * (float) (Math.toRadians(orbitTime * 3F));
            final double extraX = circleDistance * Mth.sin((angle));
            final double extraZ = circleDistance * Mth.cos(angle);
            return startOrbitFrom.add(extraX, 0, extraZ);
        }
    }

    static class SquillSchoolGoal extends Goal {
        private final Squill squill;
        @Nullable
        public Squill leader;
        private int timeToRecalcPath;

        public SquillSchoolGoal(Squill purp) {
            this.squill = purp;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            List<? extends Squill> list = squill.level().getEntitiesOfClass(Squill.class, squill.getBoundingBox().inflate(12.0D, 12.0D, 12.0D));
            Squill newLeader = null;
            double smallestDistance = Double.MAX_VALUE;
            for (Squill squills : list) {
                double distance = squill.distanceToSqr(squills);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    newLeader = squills;
                }
            }

            if (newLeader == null || smallestDistance < 9.0D) return false;
            this.leader = newLeader;
            return true;
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.squill.getNavigation().moveTo(this.leader, 1.0F);
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (!squill.isBaby() || leader == null || !leader.isAlive()) return false;
            double distanceToLeader = squill.distanceToSqr(leader);
            return distanceToLeader >= 9.0D && distanceToLeader <= 256.0D;
        }

        @Override
        public void stop() {
            this.leader = null;
            this.squill.getNavigation().stop();
        }
    }

    static class SquillMoveController extends MoveControl {

        private Squill squill;
        private Vec3 prevPos;
        private int stuckTicks;

        public SquillMoveController(Squill squill) {
            super(squill);
            this.prevPos = squill.position();
            this.squill = squill;
        }

        @Override
        public void setWantedPosition(double x, double y, double z, double speedIn) {
            super.setWantedPosition(x, y, z, speedIn);
            this.stuckTicks = 0;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 pos = squill.position();
                double x = pos.x();
                double z = pos.z();
                Vec3 vector3d = new Vec3(this.wantedX - x, this.wantedY - pos.y(), this.wantedZ - z);
                double distance = vector3d.length();
                if (distance <= 0.2F) {
                    this.operation = Operation.WAIT;
                } else {
                    double dx = vector3d.x;
                    double dz = vector3d.z;
                    squill.setYRot(squill.yBodyRot = this.rotlerp(squill.getYRot(), (float) (Mth.atan2(dz, dx) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F));
                    float newMoveSpeed = Mth.lerp(0.125F, squill.getSpeed(), (float) (this.speedModifier * squill.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    squill.setSpeed(newMoveSpeed);
                    double normalizedY = vector3d.y / distance;
                    squill.setDeltaMovement(squill.getDeltaMovement().add(0.0F, newMoveSpeed * normalizedY * 0.1D, 0.0F));
                    LookControl lookControl = squill.getLookControl();
                    double d11 = lookControl.getWantedX();
                    double d12 = lookControl.getWantedY();
                    double d13 = lookControl.getWantedZ();
                    double d8 = x + (dx / distance) * 2.0D;
                    double d9 = squill.getEyeY() + normalizedY / distance;
                    double d10 = z + (dz / distance) * 2.0D;
                    if (!lookControl.isLookingAtTarget()) {
                        d11 = d8;
                        d12 = d9;
                        d13 = d10;
                    }

                    lookControl.setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);

                    if (this.prevPos.distanceToSqr(pos) <= 0.005F) {
                        if (++this.stuckTicks >= 60) {
                            this.operation = Operation.WAIT;
                        }
                    } else {
                        this.stuckTicks = 0;
                    }
                }
            } else {
                squill.setSpeed(0.0F);
                squill.setDeltaMovement(squill.getDeltaMovement().add(0.0F, 0.01F, 0.0F));
            }
            this.prevPos = squill.position();
        }
    }
}
