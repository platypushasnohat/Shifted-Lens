package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SquidPanicGoal;
import com.platypushasnohat.shifted_lens.entities.ai.utils.SquidLookControl;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLSoundEvents;
import com.platypushasnohat.shifted_lens.registry.tags.SLBiomeTags;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
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

@Mixin(value = Squid.class, priority = 1001)
public abstract class SquidMixin extends WaterAnimal implements VariantAccess, AnimationStateAccess, Bucketable {

    @Shadow
    protected abstract SoundEvent getSquirtSound();

    @Shadow
    protected abstract ParticleOptions getInkParticle();

    @Unique
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.INT);

    private @Unique final AnimationState swimmingAnimationState = new AnimationState();

    @Override
    public AnimationState getSwimmingAnimationState() {
        return swimmingAnimationState;
    }

    protected SquidMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends WaterAnimal> entityType, Level level, CallbackInfo callbackInfo) {
        this.moveControl = new SmoothSwimmingMoveControl(this, 45, 10, 0.02F, 0.1F, false);
        this.lookControl = new SquidLookControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(1, new SquidPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 40));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WaterBoundPathNavigation(this, level);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() && this.isInWaterOrBubble()) {
            this.moveRelative(this.getSpeed(), travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
        } else {
            super.travel(travelVector);
        }
    }

    @Inject(method = "aiStep()V", at = @At("HEAD"), cancellable = true)
    public void aiStep(CallbackInfo ci) {
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

        if (!isInWaterOrBubble() && this.isAlive()) {
            if (this.onGround() && random.nextFloat() < 0.2F) {
                this.setDeltaMovement(this.getDeltaMovement().add((this.random.nextFloat() * 2.0F - 1.0F) * 0.2F, 0.5D, (this.random.nextFloat() * 2.0F - 1.0F) * 0.2F));
                this.setYRot(this.random.nextFloat() * 360.0F);
                this.playSound(SLSoundEvents.FISH_FLOP.get(), this.getSoundVolume(), this.getVoicePitch() * 0.8F);
            }
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.setupAnimationStates();
        }
    }

    @Unique
    private void setupAnimationStates() {
        this.swimmingAnimationState.animateWhen(this.isAlive(), this.tickCount);
    }

    @Override
    public void calculateEntityAnimation(boolean flying) {
        float f1 = (float) Mth.length(this.getX() - this.xo, this.getY() - this.yo, this.getZ() - this.zo);
        float f2 = Math.min(f1 * 10.0F, 1.0F);
        this.walkAnimation.update(f2, 0.4F);
    }

    @Inject(at = @At("HEAD"), method = "spawnInk", cancellable = true)
    private void spawnInk(CallbackInfo ci) {
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
        if (ShiftedLensConfig.BLINDING_SQUIDS.get()) {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.5F))) {
                if (!(entity instanceof Squid) && !(entity instanceof Player player && player.getAbilities().instabuild)) {
                    if (((Squid) (Object) this) instanceof GlowSquid) {
                        entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
                    } else {
                        entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60));
                    }
                }
            }
        }
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
        if (this.level().getBiome(this.blockPosition()).is(SLBiomeTags.SPAWNS_COLD_SQUID)) this.setVariant(1);
        else this.setVariant(0);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
    }
}
