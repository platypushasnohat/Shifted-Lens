package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SquidPanicGoal;
import com.platypushasnohat.shifted_lens.entities.ai.utils.SquidLookControl;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLSoundEvents;
import com.platypushasnohat.shifted_lens.registry.tags.SLBiomeTags;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@SuppressWarnings("deprecation")
@Mixin(Squid.class)
public abstract class SquidMixin extends WaterAnimal implements VariantAccess, AnimationStateAccess, Bucketable {

    @Unique
    private static final EntityDataAccessor<Boolean> SQUID_FROM_BUCKET = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.BOOLEAN);

    @Shadow public float xBodyRot;
    @Shadow public float xBodyRotO;
    @Shadow public float zBodyRot;
    @Shadow public float zBodyRotO;
    @Shadow public float tentacleMovement;
    @Shadow public float oldTentacleMovement;
    @Shadow public float tentacleAngle;
    @Shadow public float oldTentacleAngle;
    @Shadow private float speed;
    @Shadow private float tentacleSpeed;
    @Shadow private float rotateSpeed;

    @Shadow
    protected abstract SoundEvent getSquirtSound();

    @Shadow
    protected abstract ParticleOptions getInkParticle();

    @Unique
    private static final EntityDataAccessor<Integer> SQUID_VARIANT = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.INT);

    private @Unique final AnimationState shiftedLens$swimmingAnimationState = new AnimationState();

    @Override
    public AnimationState shiftedLens$getSwimmingAnimationState() {
        return shiftedLens$swimmingAnimationState;
    }

    protected SquidMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends WaterAnimal> entityType, Level level, CallbackInfo ci) {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            this.moveControl = new SmoothSwimmingMoveControl(this, 45, 10, 0.02F, 0.1F, false);
            this.lookControl = new SquidLookControl(this);
        }
    }

    @Inject(method = "registerGoals()V", at = @At("HEAD"), cancellable = true)
    protected void registerGoals(CallbackInfo ci) {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            ci.cancel();
            this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
            this.goalSelector.addGoal(1, new SquidPanicGoal(this, 2.0D));
            this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 40));
        }
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            return new WaterBoundPathNavigation(this, level);
        }
        return super.createNavigation(level);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            if (this.isEffectiveAi() && this.isInWaterOrBubble()) {
                this.moveRelative(this.getSpeed(), travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            } else {
                super.travel(travelVector);
            }
        } else {
            if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
                this.move(MoverType.SELF, this.getDeltaMovement());
            }
        }
    }

    @Inject(method = "aiStep()V", at = @At("HEAD"), cancellable = true)
    public void aiStep(CallbackInfo ci) {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            ci.cancel();
            super.aiStep();

            if (this.isEffectiveAi()) {
                if (this.isPassenger()) {
                    this.setYRot(0.0F);
                    this.setXRot(0.0F);
                    this.setAirSupply(this.getMaxAirSupply());
                } else if (this.isInWater() && this.getNavigation().isDone()) {
                    float yaw = this.getYRot();
                    float pitch = this.getXRot();
                    float pitchFactor = Mth.cos(pitch * ((float) Math.PI / 180F));
                    float x = -Mth.sin(yaw * ((float) Math.PI / 180F)) * pitchFactor;
                    float y = -Mth.sin(pitch * ((float) Math.PI / 180F));
                    float z = Mth.cos(yaw * ((float) Math.PI / 180F)) * pitchFactor;
                    Vec3 motion = new Vec3(x, y, z).normalize().scale(0.012F);
                    this.push(motion.x, motion.y, motion.z);
                }

                if (!this.level().isClientSide && (Mth.abs(this.xRotO - this.getXRot()) >= 1.0F || Mth.abs(this.yRotO - this.getYRot()) >= 1.0F)) {
                    this.hasImpulse = true;
                }
            }

            // keep tentacle rotation values
//            this.shiftedLens$animateTentacles();
        }

        if (ShiftedLensConfig.FLOPPING_SQUIDS.get()) {
            if (!isInWaterOrBubble() && this.isAlive()) {
                if (this.onGround() && random.nextFloat() < 0.2F) {
                    this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5D, (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F));
                    this.setYRot(this.random.nextFloat() * 360.0F);
                    this.playSound(SLSoundEvents.FISH_FLOP.get(), this.getSoundVolume(), this.getVoicePitch() * 0.8F);
                }
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide && ShiftedLensConfig.SQUID_REVAMP.get()) {
            this.shiftedLens$setupAnimationStates();
        }
    }

    @Unique
    private void shiftedLens$setupAnimationStates() {
        this.shiftedLens$swimmingAnimationState.animateWhen(this.isAlive(), this.tickCount);
    }

    @Unique
    private void shiftedLens$animateTentacles() {
        this.xBodyRotO = this.xBodyRot;
        this.zBodyRotO = this.zBodyRot;
        this.oldTentacleMovement = this.tentacleMovement;
        this.oldTentacleAngle = this.tentacleAngle;
        this.tentacleMovement += this.tentacleSpeed;
        if ((double) this.tentacleMovement > (Math.PI * 2D)) {
            if (this.level().isClientSide) {
                this.tentacleMovement = ((float) Math.PI * 2F);
            } else {
                this.tentacleMovement -= ((float) Math.PI * 2F);
                if (this.random.nextInt(10) == 0) {
                    this.tentacleSpeed = 1.0F / (this.random.nextFloat() + 1.0F) * 0.2F;
                }
                this.level().broadcastEntityEvent(this, (byte) 19);
            }
        }

        if (this.isInWaterOrBubble()) {
            if (this.tentacleMovement < (float) Math.PI) {
                float f = this.tentacleMovement / (float) Math.PI;
                this.tentacleAngle = Mth.sin(f * f * (float) Math.PI) * (float) Math.PI * 0.25F;
                if ((double) f > 0.75D) {
                    this.speed = 1.0F;
                    this.rotateSpeed = 1.0F;
                } else {
                    this.rotateSpeed *= 0.8F;
                }
            } else {
                this.tentacleAngle = 0.0F;
                this.speed *= 0.9F;
                this.rotateSpeed *= 0.99F;
            }
            Vec3 vec3 = this.getDeltaMovement();
            double horizontalDistance = vec3.horizontalDistance();
            float multiplier = 0.1F;
            this.zBodyRot += (float) Math.PI * this.rotateSpeed * 1.5F;
            this.xBodyRot += (-((float) Mth.atan2(horizontalDistance, vec3.y)) * (180F / (float) Math.PI) - this.xBodyRot) * multiplier;
        } else {
            this.tentacleAngle = Mth.abs(Mth.sin(this.tentacleMovement)) * (float) Math.PI * 0.25F;
            this.xBodyRot += (-90.0F - this.xBodyRot) * 0.02F;
        }
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.walkAnimation.update(f2, 0.4F);
    }

    @Inject(at = @At("HEAD"), method = "spawnInk", cancellable = true)
    private void spawnInk(CallbackInfo ci) {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            ci.cancel();
            this.playSound(this.getSquirtSound(), this.getSoundVolume(), this.getVoicePitch());
            if (!this.level().isClientSide) {
                Vec3 inkDirection = new Vec3(0, 0, -1.2F).xRot(-this.getXRot() * Mth.DEG_TO_RAD).yRot(-this.yBodyRot * Mth.DEG_TO_RAD);
                Vec3 vec3 = this.position().add(inkDirection);
                for (int i = 0; i < 30; ++i) {
                    Vec3 vec32 = inkDirection.add(random.nextFloat() - 0.5F, random.nextFloat() - 0.5F, random.nextFloat() - 0.5F).scale(0.8D + (double) (this.random.nextFloat() * 2.0F));
                    ((ServerLevel) this.level()).sendParticles(this.getInkParticle(), vec3.x, vec3.y + 0.5D, vec3.z, 0, vec32.x, vec32.y, vec32.z, 0.1F);
                }
            }
        }
        if (ShiftedLensConfig.BLINDING_SQUIDS.get()) {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.5F))) {
                if (!(entity instanceof Squid) && !(entity instanceof Player player && player.getAbilities().instabuild)) {
                    entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, ShiftedLensConfig.BLINDING_DURATION.get()));
                }
            }
        }
        if (ShiftedLensConfig.GLOWING_SQUIDS.get()) {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.5F))) {
                if (!(entity instanceof Squid) && !(entity instanceof Player player && player.getAbilities().instabuild)) {
                    if (((Squid) (Object) this) instanceof GlowSquid) {
                        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, ShiftedLensConfig.GLOWING_DURATION.get()));
                    }
                }
            }
        }
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(SQUID_FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.entityData.set(SQUID_FROM_BUCKET, fromBucket);
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
    public @NotNull ItemStack getBucketItemStack() {
        return this.getType() == EntityType.GLOW_SQUID ? ItemStack.EMPTY : new ItemStack(SLItems.SQUID_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    protected @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (ShiftedLensConfig.BUCKETABLE_SQUIDS.get()) {
            return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SQUID_VARIANT, 0);
        this.entityData.define(SQUID_FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.shiftedLens$getVariant());
        compoundTag.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.shiftedLens$setVariant(compoundTag.getInt("Variant"));
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
    }

    @Override
    public int shiftedLens$getVariant() {
        return this.entityData.get(SQUID_VARIANT);
    }

    @Override
    public void shiftedLens$setVariant(int variant) {
        this.entityData.set(SQUID_VARIANT, variant);
    }

    @Unique
    private void shiftedLens$spawnSquidVariant() {
        if (ShiftedLensConfig.SQUID_REVAMP.get()) {
            if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_COLD_SQUID)) this.shiftedLens$setVariant(1);
            else this.shiftedLens$setVariant(0);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor level, @NotNull DifficultyInstance difficulty, @NotNull MobSpawnType spawnType, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag compoundTag) {
        this.shiftedLens$spawnSquidVariant();
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }
}
