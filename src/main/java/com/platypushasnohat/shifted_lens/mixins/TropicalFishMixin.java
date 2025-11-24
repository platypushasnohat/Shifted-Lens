package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TropicalFish.class)
public abstract class TropicalFishMixin extends AbstractSchoolingFish {

    protected TropicalFishMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends AbstractSchoolingFish> entityType, Level level, CallbackInfo callbackInfo) {
        if (ShiftedLensConfig.TROPICAL_FISH_REVAMP.get()) {
            this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, false);
            this.lookControl = new SmoothSwimmingLookControl(this, 4);
        }
    }

    @Override
    protected void registerGoals() {
        if (ShiftedLensConfig.TROPICAL_FISH_REVAMP.get()) {
            this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
            this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
            this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
            this.goalSelector.addGoal(3, new CustomRandomSwimGoal(this, 1, 1, 16, 16, 3));
            this.goalSelector.addGoal(4, new FollowFlockLeaderGoal(this));
        } else {
            super.registerGoals();
        }
    }

    @Override
    public int getMaxSchoolSize() {
        return ShiftedLensConfig.TROPICAL_FISH_REVAMP.get() ? 12 : super.getMaxSpawnClusterSize();
    }

    @Override
    public void travel(@NotNull Vec3 travelVec) {
        if (ShiftedLensConfig.TROPICAL_FISH_REVAMP.get()) {
            if (this.isEffectiveAi() && this.isInWater()) {
                this.moveRelative(this.getSpeed(), travelVec);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            } else {
                super.travel(travelVec);
            }
        } else {
            super.travel(travelVec);
        }
    }
}
