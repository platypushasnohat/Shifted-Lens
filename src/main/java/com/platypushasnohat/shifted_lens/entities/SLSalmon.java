package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.FollowSchoolLeaderGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public class SLSalmon extends SchoolingFish {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(SLSalmon.class, EntityDataSerializers.INT);

    public SLSalmon(EntityType<? extends SchoolingFish> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 5, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 4);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new CustomRandomSwimGoal(this, 1, 1, 20, 20, 3));
        this.goalSelector.addGoal(2, new FollowSchoolLeaderGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 3.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.9F);
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

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.5F;
    }

    @Override
    public int getMaxSchoolSize() {
        return 20;
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.SALMON_SPAWN_EGG);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        if (level.getBiome(this.blockPosition()).is(BiomeTags.IS_OCEAN)) {
            this.setVariant(1);
        } else {
            this.setVariant(0);
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }

    @Override
    @Nullable
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SALMON_AMBIENT;
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SoundEvents.SALMON_DEATH;
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.SALMON_HURT;
    }

    @Override
    @Nullable
    protected SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }
}
