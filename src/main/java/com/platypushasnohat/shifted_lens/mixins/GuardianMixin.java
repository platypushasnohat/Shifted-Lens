package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Guardian.class)
public abstract class GuardianMixin extends Monster implements AnimationStateAccess {

    @Shadow public abstract boolean hasActiveAttackTarget();

    private @Unique final AnimationState eyeAnimationState = new AnimationState();
    private @Unique final AnimationState beamAnimationState = new AnimationState();
    private @Unique final AnimationState swimmingAnimationState = new AnimationState();

    @Override
    public AnimationState shiftedLens$getSwimmingAnimationState() {
        return swimmingAnimationState;
    }

    @Override
    public AnimationState shiftedLens$getEyeAnimationState() {
        return eyeAnimationState;
    }

    @Override
    public AnimationState shiftedLens$getBeamAnimationState() {
        return beamAnimationState;
    }

    protected GuardianMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends Guardian> entityType, Level level, CallbackInfo callbackInfo) {
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 10, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
            if (this.isInWater() && this.getDeltaMovement().lengthSqr() > 0.01D) {
                Vec3 vec31 = this.getViewVector(0.0F);
                this.level().addParticle(ParticleTypes.BUBBLE, this.getRandomX(0.5D) - vec31.x * 1.5D, this.getRandomY() - vec31.y * 1.5D, this.getRandomZ(0.5D) - vec31.z * 1.5D, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Unique
    private void setupAnimationStates() {
        this.swimmingAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.eyeAnimationState.animateWhen(this.getTarget() == null && !this.hasActiveAttackTarget(), this.tickCount);
        this.beamAnimationState.animateWhen(this.hasActiveAttackTarget(), this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.walkAnimation.update(f2, 0.4F);
    }
}
