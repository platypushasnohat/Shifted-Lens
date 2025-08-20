package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import com.platypushasnohat.shifted_lens.mixin_utils.GuardianAnimationAccess;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ElderGuardian.class)
public abstract class ElderGuardianMixin extends Guardian implements GuardianAnimationAccess {

    @Unique
    private final AnimationState idleAnimationState = new AnimationState();
    @Unique
    private final AnimationState eyeAnimationState = new AnimationState();
    @Unique
    private final AnimationState beamStartAnimationState = new AnimationState();
    @Unique
    private final AnimationState beamAnimationState = new AnimationState();
    @Unique
    private final AnimationState beamEndAnimationState = new AnimationState();

    @Unique
    public AnimationState getIdleAnimationState() {
        return idleAnimationState;
    }
    @Unique
    public AnimationState getEyeAnimationState() {
        return eyeAnimationState;
    }
    @Unique
    public AnimationState getBeamStartAnimationState() {
        return beamStartAnimationState;
    }
    @Unique
    public AnimationState getBeamAnimationState() {
        return beamAnimationState;
    }
    @Unique
    public AnimationState getBeamEndAnimationState() {
        return beamEndAnimationState;
    }

    @Unique
    private int idleAnimationTimeout = 0;

    protected ElderGuardianMixin(EntityType<? extends Guardian> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
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

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
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
}
