package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class BaseGuardian extends Guardian {

    private static final EntityDataAccessor<Integer> ATTACK_TARGET = SynchedEntityData.defineId(BaseGuardian.class, EntityDataSerializers.INT);

    @Nullable
    private LivingEntity clientAttackTarget;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState eyeAnimationState = new AnimationState();

    public final AnimationState beamStartAnimationState = new AnimationState();
    public final AnimationState beamAnimationState = new AnimationState();
    public final AnimationState beamEndAnimationState = new AnimationState();

    public BaseGuardian(EntityType<? extends BaseGuardian> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        } else {
            super.travel(vec3);
        }
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
        this.eyeAnimationState.animateWhen(this.isAlive() && this.getTarget() == null && !this.hasActiveAttackTarget(), this.tickCount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACK_TARGET, 0);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (ATTACK_TARGET.equals(accessor)) {
            this.clientAttackTarget = null;
        }

        if (this.getPose() == SLPoses.BEAM_START.get()) {
            this.beamStartAnimationState.start(this.tickCount);
        }
        else if (this.getPose() == SLPoses.BEAM.get()) {
            this.beamAnimationState.start(this.tickCount);
            this.beamStartAnimationState.stop();
        }
        else if (this.getPose() == SLPoses.BEAM_END.get()) {
            this.beamAnimationState.stop();
            this.beamStartAnimationState.stop();
            this.beamEndAnimationState.start(this.tickCount);
        }
        else if (this.getPose() == Pose.SWIMMING) {
            this.beamStartAnimationState.stop();
            this.beamAnimationState.stop();
            this.beamEndAnimationState.stop();
        }
    }

    public void setActiveAttackTarget(int targetId) {
        this.entityData.set(ATTACK_TARGET, targetId);
    }

    @Override
    public boolean hasActiveAttackTarget() {
        return this.entityData.get(ATTACK_TARGET) != 0;
    }

    @Nullable
    public LivingEntity getActiveAttackTarget() {
        if (!this.hasActiveAttackTarget()) {
            return null;
        } else if (this.level().isClientSide) {
            if (this.clientAttackTarget != null) {
                return this.clientAttackTarget;
            } else {
                Entity entity = this.level().getEntity(this.entityData.get(ATTACK_TARGET));
                if (entity instanceof LivingEntity) {
                    this.clientAttackTarget = (LivingEntity) entity;
                    return this.clientAttackTarget;
                } else {
                    return null;
                }
            }
        } else {
            return this.getTarget();
        }
    }

    @Override
    public int getMaxHeadXRot() {
        return 30;
    }

    public static class GuardianMoveTowardsRestrictionGoal extends Goal {

        private final BaseGuardian guardian;
        private double wantedX;
        private double wantedY;
        private double wantedZ;

        public GuardianMoveTowardsRestrictionGoal(BaseGuardian guardian) {
            this.guardian = guardian;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.guardian.isWithinRestriction()) {
                return false;
            } else {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.guardian, 16, 8, Vec3.atBottomCenterOf(this.guardian.getRestrictCenter()), (float) Math.PI / 2F);
                if (vec3 == null) {
                    return false;
                } else {
                    this.wantedX = vec3.x;
                    this.wantedY = vec3.y;
                    this.wantedZ = vec3.z;
                    return true;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !this.guardian.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.guardian.getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, 1.0D);
        }
    }
}
