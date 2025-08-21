package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.ai.goals.SalmonLeapGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
import com.platypushasnohat.shifted_lens.mixin_utils.FishAnimationAccess;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.tags.SLBiomeTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FollowFlockLeaderGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Salmon.class)
public abstract class SalmonMixin extends AbstractSchoolingFish implements FishAnimationAccess, VariantAccess {

    @Unique
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Salmon.class, EntityDataSerializers.INT);

    @Unique
    public final AnimationState shiftedLens$flopAnimationState = new AnimationState();

    @Unique
    public AnimationState getFlopAnimationState() {
        return shiftedLens$flopAnimationState;
    }

    protected SalmonMixin(EntityType<? extends AbstractSchoolingFish> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends AbstractSchoolingFish> entityType, Level level, CallbackInfo callbackInfo) {
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(3, new SalmonLeapGoal(this, 40));
        this.goalSelector.addGoal(4, new CustomRandomSwimGoal(this, 1, 1, 32, 32, 3));
        this.goalSelector.addGoal(5, new FollowFlockLeaderGoal(this));
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
        shiftedLens$flopAnimationState.animateWhen(!this.isInWaterOrBubble(), this.tickCount);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(compoundTag.getInt("Variant"));
    }

    @Override
    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    @Override
    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_OCEAN_SALMON)) this.setVariant(1);
        else this.setVariant(0);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }
}
