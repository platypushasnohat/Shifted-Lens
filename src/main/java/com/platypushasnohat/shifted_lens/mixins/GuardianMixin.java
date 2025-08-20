package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import com.platypushasnohat.shifted_lens.mixin_utils.GuardianAnimationAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Guardian.class)
public abstract class GuardianMixin extends Monster implements GuardianAnimationAccess {

    @Shadow
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.INT);

    @Shadow
    @Nullable
    protected RandomStrollGoal randomStrollGoal;

    @Shadow
    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    private @Unique final AnimationState idleAnimationState = new AnimationState();
    private @Unique final AnimationState eyeAnimationState = new AnimationState();
    private @Unique final AnimationState beamStartAnimationState = new AnimationState();
    private @Unique final AnimationState beamAnimationState = new AnimationState();
    private @Unique final AnimationState beamEndAnimationState = new AnimationState();

    @Override
    public AnimationState getIdleAnimationState() {
        return idleAnimationState;
    }
    @Override
    public AnimationState getEyeAnimationState() {
        return eyeAnimationState;
    }
    @Override
    public AnimationState getBeamStartAnimationState() {
        return beamStartAnimationState;
    }
    @Override
    public AnimationState getBeamAnimationState() {
        return beamAnimationState;
    }
    @Override
    public AnimationState getBeamEndAnimationState() {
        return beamEndAnimationState;
    }

    private @Unique int idleAnimationTimeout = 0;

    protected GuardianMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends Guardian> entityType, Level level, CallbackInfo callbackInfo) {
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Inject(method = "registerGoals()V", at = @At("TAIL"))
    private void registerGoals(CallbackInfo ci) {
        this.randomStrollGoal = new RandomSwimmingGoal(this, 1.0D, 80);
    }

    @Inject(method = "aiStep()V", at = @At("TAIL"))
    public void aiStep(CallbackInfo ci) {

    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
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
    public float getWalkTargetValue(BlockPos pos, LevelReader level) {
        return 0;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            if (this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.003D) {
                Vec3 vec31 = this.getViewVector(0.0F);

                for (int i = 0; i < 2; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, this.getRandomX(0.5D) - vec31.x * 1.5D, this.getRandomY() - vec31.y * 1.5D, this.getRandomZ(0.5D) - vec31.z * 1.5D, 0.0D, 0.0D, 0.0D);
                }
            }
        }
    }

    @Unique
    private void setupAnimationStates() {
        if (this.idleAnimationTimeout == 0) {
            this.idleAnimationTimeout = 160;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        this.eyeAnimationState.animateWhen(this.isAlive() && this.getTarget() == null && !this.hasActiveAttackTarget(), this.tickCount);
    }

    @Inject(method = "onSyncedDataUpdated(Lnet/minecraft/network/syncher/EntityDataAccessor;)V", at = @At("TAIL"))
    public void onSyncedDataUpdated(EntityDataAccessor<?> accessor, CallbackInfo ci) {
        if (this.getPose() == SLPoses.BEAM_START.get()) {
            this.beamStartAnimationState.start(this.tickCount);
        }
        else if (this.getPose() == SLPoses.BEAM.get()) {
            this.beamStartAnimationState.stop();
            this.beamAnimationState.start(this.tickCount);
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

    @Mixin(targets = "net.minecraft.world.entity.monster.Guardian.GuardianAttackGoal")
    public abstract static class GuardianAttackGoal extends Goal {

        @Override
        public void start() {
            super.tick();
        }

        @Override
        public void stop() {
            super.tick();
        }

        @Override
        public boolean canUse() {
            return false;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick() {
            super.tick();
        }
    }
}
