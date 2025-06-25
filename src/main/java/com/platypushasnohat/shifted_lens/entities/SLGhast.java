package com.platypushasnohat.shifted_lens.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SLGhast extends Ghast {

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    public SLGhast(EntityType<? extends SLGhast> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SLGhastMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.FOLLOW_RANGE, 96.0D).add(Attributes.FLYING_SPEED, 0.1D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new GhastRandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new Ghast.GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastAttackGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (target) -> Math.abs(target.getY() - this.getY()) <= 8.0D));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.shootAnimationState.animateWhen(this.isAlive() && this.isCharging(), this.tickCount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.GHAST_SPAWN_EGG);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.75F;
    }

    @Override
    public int getMaxHeadXRot() {
        return 500;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected float getSoundVolume() {
        return 3.0F;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else {
                BlockPos ground = getBlockPosBelowThatAffectsMyMovement();
                float f = 0.91F;

                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }

                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;

                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }

                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, vec3);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(f));
            }
        }
        this.calculateEntityAnimation(false);
    }

    public void shootFireball() {
        Vec3 vec3 = this.getViewVector(1.0F);
        double targetX = this.getTarget().getX() - (this.getX() + vec3.x() * 4.0D);
        double targetY = this.getTarget().getBoundingBox().minY + this.getTarget().getBbHeight() / 2.0F - (0.5D + this.getY() + this.getBbHeight() / 2.0F);
        double targetZ = this.getTarget().getZ() - (this.getZ() + vec3.z() * 4.0D);
        LargeFireball fireball = new LargeFireball(this.level(), this, targetX, targetY, targetZ, this.getExplosionPower());
        fireball.setPos(this.getX() + vec3.x() * 4.0D, this.getY(0.5D) + 0.5D, this.getZ() + vec3.z() * 4.0D);
        this.level().addFreshEntity(fireball);
    }

    // goals
    static class GhastAttackGoal extends Goal {

        private final SLGhast ghast;
        public int chargeTime;

        public GhastAttackGoal(SLGhast ghast) {
            this.ghast = ghast;
        }

        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        public void start() {
            this.chargeTime = 0;
        }

        public void stop() {
            this.ghast.setCharging(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.ghast.getTarget();

            if (target != null) {
                if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.getSensing().hasLineOfSight(target)) {
                    this.chargeTime++;

                    this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());

                    if (this.chargeTime == 32) {
                        ghast.playSound(SoundEvents.GHAST_WARN, 10.0F, ghast.getVoicePitch());
                    }

                    if (this.chargeTime == 40) {
                        this.ghast.playSound(SoundEvents.GHAST_SHOOT, 10.0F, this.ghast.getVoicePitch());
                        this.ghast.shootFireball();
                        this.chargeTime = -40;
                    }
                } else if (this.chargeTime > 0) {
                    this.chargeTime--;
                }

                this.ghast.setCharging(this.chargeTime > 10);
            }
        }
    }

    static class GhastRandomFloatAroundGoal extends Goal {

        private final SLGhast ghast;

        public GhastRandomFloatAroundGoal(SLGhast ghast) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl moveControl = this.ghast.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            } else {
                double d0 = moveControl.getWantedX() - this.ghast.getX();
                double d1 = moveControl.getWantedY() - this.ghast.getY();
                double d2 = moveControl.getWantedZ() - this.ghast.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.ghast.getRandom();
            double d0 = this.ghast.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.ghast.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.ghast.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
        }
    }

    static class SLGhastMoveControl extends MoveControl {
        private final SLGhast ghast;
        private int floatDuration;

        public SLGhastMoveControl(SLGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        public void tick() {
            if (!this.ghast.isCharging()) {
                if (this.operation == MoveControl.Operation.MOVE_TO) {
                    if (this.floatDuration-- <= 0) {
                        this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                        Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                        double d0 = vec3.length();
                        vec3 = vec3.normalize();
                        if (this.canReach(vec3, Mth.ceil(d0))) {
                            this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(this.ghast.getAttribute(Attributes.FLYING_SPEED).getValue())));
                        } else {
                            this.operation = MoveControl.Operation.WAIT;
                        }
                    }
                }
            }
        }

        private boolean canReach(Vec3 vec3, int distance) {
            AABB aabb = this.ghast.getBoundingBox();

            for(int i = 1; i < distance; ++i) {
                aabb = aabb.move(vec3);
                if (!this.ghast.level().noCollision(this.ghast, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }
}
