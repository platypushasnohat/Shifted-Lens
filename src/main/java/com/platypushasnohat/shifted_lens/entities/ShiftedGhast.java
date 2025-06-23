package com.platypushasnohat.shifted_lens.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class ShiftedGhast extends FlyingMob implements Enemy {

    private static final EntityDataAccessor<Boolean> IS_SHOOTING = SynchedEntityData.defineId(ShiftedGhast.class, EntityDataSerializers.BOOLEAN);

    private int explosionPower = 1;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState shootAnimationState = new AnimationState();

    public ShiftedGhast(EntityType<? extends ShiftedGhast> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 5;
        this.moveControl = new GhastMoveControl(this);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0D).add(Attributes.FOLLOW_RANGE, 96.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, new GhastRandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new GhastLookGoal(this));
        this.goalSelector.addGoal(7, new GhastShootFireballGoal(this));
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
        this.shootAnimationState.animateWhen(this.isAlive() && this.isShooting(), this.tickCount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_SHOOTING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByte("ExplosionPower", (byte) this.explosionPower);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("ExplosionPower", 99)) {
            this.explosionPower = compoundTag.getByte("ExplosionPower");
        }
    }

    public boolean isShooting() {
        return this.entityData.get(IS_SHOOTING);
    }

    public void setShooting(boolean shooting) {
        this.entityData.set(IS_SHOOTING, shooting);
    }

    public int getExplosionPower() {
        return this.explosionPower;
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    private static boolean isReflectedFireball(DamageSource damageSource) {
        return damageSource.getDirectEntity() instanceof LargeFireball && damageSource.getEntity() instanceof Player;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return !isReflectedFireball(damageSource) && super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        if (isReflectedFireball(damageSource)) {
            super.hurt(damageSource, 1000.0F);
            return true;
        } else {
            return !this.isInvulnerableTo(damageSource) && super.hurt(damageSource, amount);
        }
    }

    @Override
    public SoundSource getSoundSource() {
        return SoundSource.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.GHAST_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.GHAST_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.GHAST_DEATH;
    }

    @Override
    protected float getSoundVolume() {
        return 5.0F;
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
    public boolean canBeCollidedWith() {
        return true;
    }

    private static class GhastLookGoal extends Goal {

        private final ShiftedGhast ghast;

        public GhastLookGoal(ShiftedGhast ghast) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        public boolean canUse() {
            return true;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 vec3 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float) Math.PI));
                this.ghast.yBodyRot = this.ghast.getYRot();
            } else {
                LivingEntity livingentity = this.ghast.getTarget();
                if (livingentity.distanceToSqr(this.ghast) < 4096.0D) {
                    double d1 = livingentity.getX() - this.ghast.getX();
                    double d2 = livingentity.getZ() - this.ghast.getZ();
                    this.ghast.setYRot(-((float) Mth.atan2(d1, d2)) * (180F / (float) Math.PI));
                    this.ghast.yBodyRot = this.ghast.getYRot();
                }
            }
        }
    }

    private static class GhastMoveControl extends MoveControl {

        private final ShiftedGhast ghast;
        private int floatDuration;

        public GhastMoveControl(ShiftedGhast ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += 1;
                    Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    double length = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(length))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(0.02D)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 vec3, int floatDuration) {
            AABB aabb = this.ghast.getBoundingBox();

            for (int i = 1; i < floatDuration; ++i) {
                aabb = aabb.move(vec3);
                if (!this.ghast.level().noCollision(this.ghast, aabb)) {
                    return false;
                }
            }
            return true;
        }
    }

    private static class GhastShootFireballGoal extends Goal {

        private final ShiftedGhast ghast;
        public int chargeTime;

        public GhastShootFireballGoal(ShiftedGhast ghast) {
            this.ghast = ghast;
        }

        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        public void start() {
            this.chargeTime = 0;
        }

        public void stop() {
            this.ghast.setShooting(false);
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity target = this.ghast.getTarget();
            if (target != null) {
                if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.hasLineOfSight(target)) {
                    Level level = this.ghast.level();
                    ++this.chargeTime;

                    if (this.chargeTime == 30 && !this.ghast.isSilent()) {
                        level.levelEvent(null, 1015, this.ghast.blockPosition(), 0);
                    }

                    if (this.chargeTime == 40) {
                        Vec3 vec3 = this.ghast.getViewVector(1.0F);

                        double d2 = target.getX() - (this.ghast.getX() + vec3.x * 4.0D);
                        double d3 = target.getY(0.5D) - (this.ghast.getY() + 0.5D);
                        double d4 = target.getZ() - (this.ghast.getZ() + vec3.z * 4.0D);

                        if (!this.ghast.isSilent()) {
                            level.levelEvent(null, 1016, this.ghast.blockPosition(), 0);
                        }

                        LargeFireball largefireball = new LargeFireball(level, this.ghast, d2, d3, d4, this.ghast.getExplosionPower());
                        largefireball.setPos(this.ghast.getX() + vec3.x * 4.0D, this.ghast.getY() + 0.5D, largefireball.getZ() + vec3.z * 4.0D);
                        level.addFreshEntity(largefireball);
                        this.chargeTime = -40;
                    }

                } else if (this.chargeTime > 0) {
                    --this.chargeTime;
                }

                this.ghast.setShooting(this.chargeTime > 10);
            }
        }
    }

    private static class GhastRandomFloatAroundGoal extends Goal {

        private final ShiftedGhast ghast;

        public GhastRandomFloatAroundGoal(ShiftedGhast ghast) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse() {
            MoveControl movecontrol = this.ghast.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - this.ghast.getX();
                double d1 = movecontrol.getWantedY() - this.ghast.getY();
                double d2 = movecontrol.getWantedZ() - this.ghast.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        public boolean canContinueToUse() {
            return false;
        }

        public void start() {
            RandomSource randomsource = this.ghast.getRandom();
            double d0 = this.ghast.getX() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 24.0F);
            double d1 = this.ghast.getY() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 24.0F);
            double d2 = this.ghast.getZ() + (double) ((randomsource.nextFloat() * 2.0F - 1.0F) * 24.0F);
            this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
        }
    }
}
