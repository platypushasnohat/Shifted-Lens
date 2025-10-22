package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
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
import net.minecraft.world.entity.animal.Cod;
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

@Mixin(Cod.class)
public abstract class CodMixin extends AbstractSchoolingFish implements AnimationStateAccess, VariantAccess {

    @Unique
    private static final EntityDataAccessor<Integer> COD_VARIANT = SynchedEntityData.defineId(Cod.class, EntityDataSerializers.INT);

    @Unique
    private final AnimationState shiftedLens$flopAnimationState = new AnimationState();
    @Unique
    private final AnimationState shiftedLens$swimmingAnimationState = new AnimationState();

    @Override
    public AnimationState shiftedLens$getSwimmingAnimationState() {
        return shiftedLens$swimmingAnimationState;
    }

    @Override
    public AnimationState shiftedLens$getFlopAnimationState() {
        return shiftedLens$flopAnimationState;
    }

    protected CodMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends AbstractSchoolingFish> entityType, Level level, CallbackInfo callbackInfo) {
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 8, 0.02F, 0.1F, false);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(3, new CustomRandomSwimGoal(this, 1, 1, 16, 16, 3));
        this.goalSelector.addGoal(4, new FollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSchoolSize() {
        return 10;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
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
        this.shiftedLens$swimmingAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.walkAnimation.update(f2, 0.4F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(COD_VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.shiftedLens$getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.shiftedLens$setVariant(compoundTag.getInt("Variant"));
    }

    @Override
    public void saveToBucketTag(ItemStack bucket) {
        Bucketable.saveDefaultDataToBucketTag(this, bucket);
        CompoundTag compoundTag = bucket.getOrCreateTag();
        compoundTag.putInt("BucketVariantTag", this.shiftedLens$getVariant());
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
        if (compoundTag.contains("BucketVariantTag", 3)) {
            this.shiftedLens$setVariant(compoundTag.getInt("BucketVariantTag"));
        }
    }

    @Override
    public int shiftedLens$getVariant() {
        return this.entityData.get(COD_VARIANT);
    }

    @Override
    public void shiftedLens$setVariant(int variant) {
        this.entityData.set(COD_VARIANT, variant);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_COLD_COD)) this.shiftedLens$setVariant(1);
        else if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_WARM_COD)) this.shiftedLens$setVariant(2);
        else this.shiftedLens$setVariant(0);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }
}
