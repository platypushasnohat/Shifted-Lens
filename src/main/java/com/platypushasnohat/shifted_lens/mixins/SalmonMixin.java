package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Salmon.class)
public abstract class SalmonMixin extends AbstractSchoolingFish {

    @Unique
    public final AnimationState shiftedLens$flopAnimationState = new AnimationState();

    protected SalmonMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends AbstractSchoolingFish> entityType, Level level, CallbackInfo callbackInfo) {
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 4);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new CustomRandomSwimGoal(this, 1, 1, 32, 32, 3));
        this.goalSelector.addGoal(2, new FollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSchoolSize() {
        return 16;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            shiftedLens$setupAnimationStates();
        }
    }

    @Unique
    private void shiftedLens$setupAnimationStates() {
        this.shiftedLens$flopAnimationState.animateWhen(!this.isInWaterOrBubble(), this.tickCount);
    }

    @Unique
    public AnimationState shiftedLens$getFlopAnimation() {
        return shiftedLens$flopAnimationState;
    }
}
