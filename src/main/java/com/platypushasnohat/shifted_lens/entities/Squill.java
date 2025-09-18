package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.ai.goals.*;
import com.platypushasnohat.shifted_lens.entities.ai.utils.SquillMoveControl;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLParticles;
import com.platypushasnohat.shifted_lens.registry.SLSoundEvents;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Squill extends PathfinderMob implements FlyingAnimal, Bucketable {

    private static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(Squill.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(Squill.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> COMBAT_COOLDOWN = SynchedEntityData.defineId(Squill.class, EntityDataSerializers.INT);

    private Vec3 prevPull = Vec3.ZERO, pull = Vec3.ZERO;
    private float alphaProgress;
    private float prevAlphaProgress;
    public float xBodyRot;
    public float xBodyRotO;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState aggroAnimationState = new AnimationState();

    public Squill(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SquillMoveControl(this);
        this.xpReward = 5;
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.17F)
                .add(Attributes.FLYING_SPEED, 0.17F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new SquillPanicGoal(this));
        this.goalSelector.addGoal(1, new SquillAttackGoal(this));
        this.goalSelector.addGoal(2, new SquillWanderGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this).setAlertOthers());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 20, true, false, entity -> entity.getType().is(SLEntityTags.SQUILL_TARGETS)));
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected @NotNull PathNavigation createNavigation(Level level) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, level) {
            public boolean isStableDestination(BlockPos pos) {
                return !level().getBlockState(pos.below(128)).isAir();
            }
        };
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(false);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            super.travel(travelVector);
        }
    }

    private Vec3 rotateVector(Vec3 vec3) {
        Vec3 vec31 = vec3.xRot(this.xBodyRotO * ((float) Math.PI / 180F));
        return vec31.yRot(-this.yBodyRotO * ((float) Math.PI / 180F));
    }

    private void spawnInk() {
        for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.25F, 2.25F, 2.25F))) {
            if (!(entity instanceof Squill)) {
                entity.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 40));
            }
        }

        this.playSound(SLSoundEvents.SQUILL_SQUIRT.get(), this.getSoundVolume(), this.getVoicePitch());
        Vec3 vec3 = this.rotateVector(new Vec3(0.0D, 0.0D, 0.0D)).add(this.getX(), this.getY(), this.getZ());

        for (int i = 0; i < 30; ++i) {
            Vec3 vec31 = this.rotateVector(new Vec3((double) this.random.nextFloat() * 0.6D - 0.3D, -1.0D, (double) this.random.nextFloat() * 0.6D - 0.3D));
            Vec3 vec32 = vec31.scale(0.3D + (double) (this.random.nextFloat() * 2.0F));

            ((ServerLevel) this.level()).sendParticles(SLParticles.WHIRLIWIND.get(), vec3.x, vec3.y, vec3.z, 0, vec32.x, vec32.y, vec32.z, 0.1F);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (super.hurt(source, amount) && this.getLastHurtByMob() != null) {
            if (!this.level().isClientSide) {
                if (this.getHealth() <= this.getMaxHealth() * 0.25F && this.getCombatCooldown() <= 0) {
                    this.combatCooldown();
                }
                if (this.getCombatCooldown() > 0 && !this.isAttacking()) {
                    this.spawnInk();
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public static boolean canSpawn(EntityType<Squill> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return checkMobSpawnRules(entityType, level, spawnType, pos, random) && wholeHitboxCanSeeSky(level, pos, 2);
    }

    public static boolean wholeHitboxCanSeeSky(LevelAccessor level, BlockPos pos, int hitboxRadius) {
        boolean flag = true;
        for (int xOffset = -hitboxRadius; xOffset <= hitboxRadius; xOffset++) {
            for (int zOffset = -hitboxRadius; zOffset <= hitboxRadius; zOffset++) {
                flag = flag && level.canSeeSky(pos.offset(xOffset, 0, zOffset));
            }
        }
        return flag;
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        int spawnHeight = Math.min((this.blockPosition().getY() + ShiftedLensConfig.SQUILL_SPAWN_HEIGHT.get()), this.level().getMaxBuildHeight());
        if (spawnType == MobSpawnType.NATURAL || spawnType == MobSpawnType.CHUNK_GENERATION) {
            this.moveTo(this.getX(), spawnHeight, this.getZ(), this.getYRot(), this.getXRot());
        }
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }

    @Override
    public boolean isFlying() {
        return true;
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    public void updatePull(Vec3 pos) {
        this.prevPull = this.pull = pos.subtract(0.0F, 1.0F, 0.0F);
    }

    public static Vec3 lerp(Vec3 prev, Vec3 current, float ptc) {
        return prev.add(current.subtract(prev).scale(ptc));
    }

    public Vec3 getPull(float partialTicks) {
        return lerp(this.prevPull, this.pull, partialTicks);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 48;
    }

    @Override
    public void tick() {
        super.tick();

        xBodyRotO = xBodyRot;
        prevAlphaProgress = alphaProgress;

        if (this.isAlive() && alphaProgress < 20F) {
            alphaProgress++;
        }
        if (!this.isAlive() && alphaProgress > 0F) {
            alphaProgress--;
        }

        if (this.getCombatCooldown() > 0) {
            this.setCombatCooldown(this.getCombatCooldown() - 1);
        }

        if (this.level().isClientSide()) {
            setupAnimationStates();

            this.prevPull = this.pull;
            Vec3 pos = this.position();
            this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.25F));
        }

        Vec3 vec3 = this.getDeltaMovement();
        double d0 = vec3.horizontalDistance();
        this.xBodyRot += (-((float)Mth.atan2(d0, vec3.y)) * (180F / (float)Math.PI) - this.xBodyRot) * 0.1F;
    }

    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(!this.isAttacking(), this.tickCount);
        this.aggroAnimationState.animateWhen(this.isAttacking(), this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.walkAnimation.update(f2, 0.4F);
    }

    public float getAlphaProgress(float partialTicks) {
        return (prevAlphaProgress + (alphaProgress - prevAlphaProgress) * partialTicks) * 0.05F;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ATTACKING, false);
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(COMBAT_COOLDOWN, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Attacking", this.isAttacking());
        compoundTag.putBoolean("FromBucket", this.fromBucket());
        compoundTag.putInt("CombatCooldown", this.getCombatCooldown());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setAttacking(compoundTag.getBoolean("Attacking"));
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
        this.setCombatCooldown(compoundTag.getInt("CombatCooldown"));
    }

    public boolean isAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }

    public int getCombatCooldown() {
        return this.entityData.get(COMBAT_COOLDOWN);
    }

    public void setCombatCooldown(int cooldown) {
        this.entityData.set(COMBAT_COOLDOWN, cooldown);
    }

    public void combatCooldown() {
        this.entityData.set(COMBAT_COOLDOWN, 60 + random.nextInt(20 * 4));
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3, 3, 3);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0D;
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack stack) {
        Bucketable.saveDefaultDataToBucketTag(this, stack);
        if (this.hasCustomName()) {
            stack.setHoverName(this.getCustomName());
        }
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return SLItems.SQUILL_BUCKET.get().getDefaultInstance();
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

//    @Override
//    @Nullable
//    protected SoundEvent getAmbientSound() {
//        return SLSoundEvents.FISH_IDLE.get();
//    }

    @Override
    @Nullable
    protected SoundEvent getDeathSound() {
        return SLSoundEvents.SQUILL_DEATH.get();
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return SLSoundEvents.SQUILL_HURT.get();
    }

    @Override
    @NotNull
    protected InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        final ItemStack itemstack = player.getItemInHand(hand);
        if (itemstack.getItem() == Items.BUCKET && this.isAlive()) {
            this.gameEvent(GameEvent.ENTITY_INTERACT);
            this.playSound(this.getPickupSound(), 1.0F, 1.0F);
            final ItemStack itemstack1 = this.getBucketItemStack();
            this.saveToBucketTag(itemstack1);
            final ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.setItemInHand(hand, itemstack2);
            if (!this.level().isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemstack1);
            }

            this.discard();
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return super.mobInteract(player, hand);
    }
}
