package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFish.class)
public abstract class AbstractFishMixin extends WaterAnimal implements AbstractFishAccess {

    protected AbstractFishMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow
    protected abstract SoundEvent getFlopSound();

    @Unique
    public float prevOnLandProgress;

    @Unique
    public float onLandProgress;

    @Override
    public float getPrevOnLandProgress() {
        return prevOnLandProgress;
    }

    @Override
    public float getOnLandProgress() {
        return onLandProgress;
    }

    @Override
    public boolean onlyFlopOnGround() {
        return false;
    }

    @Override
    public float flopChance() {
        return 0.5F;
    }

    @Inject(method = "aiStep()V", at = @At("HEAD"), cancellable = true)
    private void aiStep(CallbackInfo ci) {
        ci.cancel();

        this.prevOnLandProgress = onLandProgress;
        boolean onLand = this.onlyFlopOnGround() ? !this.isInWaterOrBubble() && this.onGround() : !this.isInWaterOrBubble();
        if (onLand && onLandProgress < 5F) {
            onLandProgress++;
        }
        if (!onLand && onLandProgress > 0F) {
            onLandProgress--;
        }

        if (!isInWaterOrBubble() && this.isAlive()) {
            if (this.onGround() && random.nextFloat() < this.flopChance()) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5D, (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F));
                this.setYRot(this.random.nextFloat() * 360.0F);
                this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
            }
        }
        super.aiStep();
    }
}
