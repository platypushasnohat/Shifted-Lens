package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SalmonLeapGoal;
import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.tags.SLBiomeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
@Mixin(Salmon.class)
public abstract class SalmonMixin extends AbstractSchoolingFish implements AbstractFishAccess, AnimationStateAccess, VariantAccess {

    @Unique
    private static final EntityDataAccessor<Integer> shiftedLens$SALMON_VARIANT = SynchedEntityData.defineId(Salmon.class, EntityDataSerializers.INT);

    @Unique
    private final AnimationState shiftedLens$flopAnimationState = new AnimationState();
    @Unique
    private final AnimationState shiftedLens$swimmingAnimationState = new AnimationState();

    @Override
    public AnimationState shiftedLens$getSwimmingAnimationState() {
        return shiftedLens$swimmingAnimationState;
    }

    @Unique
    public AnimationState shiftedLens$getFlopAnimationState() {
        return shiftedLens$flopAnimationState;
    }

    @Override
    public boolean shiftedLens$onlyFlopOnGround() {
        return true;
    }

    @Override
    public float shiftedLens$flopChance() {
        return 0.2F;
    }

    protected SalmonMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends AbstractSchoolingFish> entityType, Level level, CallbackInfo ci) {
        if (ShiftedLensConfig.SALMON_REVAMP.get()) {
            this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 10, 0.02F, 0.1F, false);
            this.lookControl = new SmoothSwimmingLookControl(this, 10);
        }
    }

    @Override
    protected void registerGoals() {
        if (ShiftedLensConfig.SALMON_REVAMP.get()) {
            this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
            this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
            this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
            this.goalSelector.addGoal(3, new SalmonLeapGoal(this, 40));
            this.goalSelector.addGoal(4, new CustomRandomSwimGoal(this, 1, 1, 16, 16, 3));
            this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
        } else {
            super.registerGoals();
        }
    }

    @Override
    public int getMaxSchoolSize() {
        return ShiftedLensConfig.SALMON_REVAMP.get() ? 8 : super.getMaxSpawnClusterSize();
    }

    @Override
    public void travel(@NotNull Vec3 travelVec) {
        if (ShiftedLensConfig.SALMON_REVAMP.get()) {
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

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && ShiftedLensConfig.SALMON_REVAMP.get()) {
            this.shiftedLens$setupAnimationStates();
        }
    }

    @Unique
    private void shiftedLens$setupAnimationStates() {
        this.shiftedLens$flopAnimationState.animateWhen(!this.isInWaterOrBubble(), this.tickCount);
        this.shiftedLens$swimmingAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        if (ShiftedLensConfig.SALMON_REVAMP.get()) {
            float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
            float f2 = Math.min(f1 * 10.0F, 1.0F);
            this.walkAnimation.update(f2, 0.4F);
        } else {
            super.calculateEntityAnimation(flying);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(shiftedLens$SALMON_VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.shiftedLens$getVariant());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.shiftedLens$setVariant(compoundTag.getInt("Variant"));
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compoundTag = bucket.getOrCreateTag();
        compoundTag.putInt("BucketVariantTag", this.shiftedLens$getVariant());
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
        if (compoundTag.contains("BucketVariantTag", 3)) {
            this.shiftedLens$setVariant(compoundTag.getInt("BucketVariantTag"));
        }
    }

    @Override
    public int shiftedLens$getVariant() {
        return this.entityData.get(shiftedLens$SALMON_VARIANT);
    }

    @Override
    public void shiftedLens$setVariant(int variant) {
        this.entityData.set(shiftedLens$SALMON_VARIANT, variant);
    }

    @Unique
    private void shiftedLens$spawnSalmonVariant() {
        if (ShiftedLensConfig.SALMON_REVAMP.get()) {
            if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_OCEAN_SALMON)) this.shiftedLens$setVariant(1);
            else if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_COLD_RIVER_SALMON)) this.shiftedLens$setVariant(2);
            else if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_COLD_OCEAN_SALMON)) this.shiftedLens$setVariant(3);
            else this.shiftedLens$setVariant(0);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        this.shiftedLens$spawnSalmonVariant();
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }
}
