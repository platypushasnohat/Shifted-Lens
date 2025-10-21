package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.entities.ai.goals.CustomRandomSwimGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.FlyingFishFollowLeaderGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.FlyingFishGlideGoal;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class FlyingFish extends WaterAnimal implements FlyingAnimal, Bucketable {

    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(FlyingFish.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> GLIDING = SynchedEntityData.defineId(FlyingFish.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(FlyingFish.class, EntityDataSerializers.BOOLEAN);

    public final AnimationState flopAnimationState = new AnimationState();
    public final AnimationState glidingAnimationState = new AnimationState();
    public final AnimationState swimmingAnimationState = new AnimationState();

    public int glideCooldown = random.nextInt(50 * 8 * 4) + 180;

    public float prevOnLandProgress;
    public float onLandProgress;

    @Nullable
    public FlyingFish leader;
    private int schoolSize = 1;

    public FlyingFish(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SmoothSwimmingMoveControl(this, 1000, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.1F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new FlyingFishGlideGoal(this));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.6D, 1.4D, EntitySelector.NO_SPECTATORS::test));
        this.goalSelector.addGoal(3, new CustomRandomSwimGoal(this, 1, 1, 16, 8, 3));
        this.goalSelector.addGoal(4, new FlyingFishFollowLeaderGoal(this));
    }

    @Override
    public void travel(Vec3 travelVec) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), travelVec);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(travelVec);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean hurt = super.hurt(source, amount);
        if (hurt && source.getEntity() != null) {
            this.glideCooldown = 0;
            List<? extends FlyingFish> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8, 7, 8));
            for (FlyingFish entity : list) {
                entity.glideCooldown = 0;
            }
        }
        return hurt;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return SLItems.FLYING_FISH_BUCKET.get().getDefaultInstance();
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void tick() {
        super.tick();

        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends FlyingFish> list = this.level().getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if (list.size() <= 1) {
                this.schoolSize = 1;
            }
        }

        if (isGliding()) {
            if (!this.isInWaterOrBubble() && this.getDeltaMovement().y < 0.0) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(1.0F, 0.55F, 1.0F));
            }
        }

        if (this.leader != null) {
            this.glideCooldown = this.leader.glideCooldown;
        }

        if (glideCooldown > 0) {
            glideCooldown--;
        }

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    private void setupAnimationStates() {
        this.flopAnimationState.animateWhen(!this.isGliding() && !this.isInWaterOrBubble(), this.tickCount);
        this.glidingAnimationState.animateWhen(this.isGliding() && !this.isInWaterOrBubble(), this.tickCount);
        this.swimmingAnimationState.animateWhen(this.isInWaterOrBubble(), this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.walkAnimation.update(f2, 0.4F);
    }

    public void aiStep() {
        this.prevOnLandProgress = onLandProgress;
        boolean onLand = !this.isInWaterOrBubble() && this.onGround();
        if (onLand && onLandProgress < 5F) {
            onLandProgress++;
        }
        if (!onLand && onLandProgress > 0F) {
            onLandProgress--;
        }

        if (!isInWaterOrBubble() && this.isAlive()) {
            if (this.onGround() && random.nextFloat() < 0.1F) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5D, (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F));
                this.setYRot(this.random.nextFloat() * 360.0F);
                this.playSound(this.getFlopSound(), this.getSoundVolume(), this.getVoicePitch());
            }
        }
        super.aiStep();
    }

    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return 12;
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public FlyingFish startFollowing(FlyingFish fish) {
        this.leader = fish;
        fish.addFollower();
        return fish;
    }

    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        ++this.schoolSize;
    }

    private void removeFollower() {
        --this.schoolSize;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }

    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 121.0D;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, 1.0D);
        }
    }

    public void addFollowers(Stream<? extends FlyingFish> entity) {
        entity.limit(this.getMaxSchoolSize() - this.schoolSize).filter((fish) -> fish != this).forEach((fish1) -> fish1.startFollowing(this));
    }

    public static boolean canSpawn(EntityType<FlyingFish> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return WaterAnimal.checkSurfaceWaterAnimalSpawnRules(entityType, level, spawnType, pos, random);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(GLIDING, false);
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant());
        compoundTag.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(compoundTag.getInt("Variant"));
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
    }

    public int getVariant() {
        return this.entityData.get(VARIANT);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT, variant);
    }

    public boolean isGliding() {
        return this.entityData.get(GLIDING);
    }

    public void setGliding(boolean flying) {
        this.entityData.set(GLIDING, flying);
    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions size) {
        return size.height * 0.5F;
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        this.setVariant(level().getRandom().nextInt(1));
        if (spawnType == MobSpawnType.CHUNK_GENERATION || spawnType == MobSpawnType.NATURAL) {
            int schoolCount = (int) (this.getMaxSchoolSize() * this.getRandom().nextFloat());
            if (schoolCount > 0 && !this.level().isClientSide()) {
                for (int i = 0; i < schoolCount; i++) {
                    float distance = 1.5F;
                    FlyingFish entity = new FlyingFish(SLEntities.FLYING_FISH.get(), this.level());
                    entity.setVariant(this.getVariant());
                    entity.moveTo(this.getX() + this.getRandom().nextFloat() * distance, this.getY() + this.getRandom().nextFloat() * distance, this.getZ() + this.getRandom().nextFloat() * distance);
                    entity.startFollowing(this);
                    entity.glideCooldown = this.glideCooldown;
                    this.level().addFreshEntity(entity);
                }
            }
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SLSoundEvents.FISH_DEATH.get();
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SLSoundEvents.FISH_HURT.get();
    }

    protected @NotNull SoundEvent getFlopSound() {
        return SLSoundEvents.FISH_FLOP.get();
    }

    @Override
    public boolean isFlying() {
        return true;
    }
}
