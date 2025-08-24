package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.tags.SLBiomeTags;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(Squid.class)
@SuppressWarnings("FieldCanBeLocal, unused")
public abstract class SquidMixin extends WaterAnimal implements VariantAccess {

    @Unique
    private static final EntityDataAccessor<Integer> VARIANT = SynchedEntityData.defineId(Squid.class, EntityDataSerializers.INT);

    protected SquidMixin(EntityType<? extends WaterAnimal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
            this.move(MoverType.SELF, this.getDeltaMovement());
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.FAIL;
        if (itemstack.is(Items.BUCKET) && SLConfig.MILKABLE_SQUIDS.get()) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack result = ItemUtils.createFilledResult(itemstack, player, Items.MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(hand, result);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        else return InteractionResult.FAIL;
    }

    @Mixin(targets = "net.minecraft.world.entity.animal.Squid$SquidRandomMovementGoal")
    public abstract static class SquidRandomMovementGoal extends Goal {
        @Shadow
        @Final
        private Squid squid;

        @Override
        public void tick() {
            if (this.squid.getRandom().nextInt(reducedTickDelay(60)) == 0 || !this.squid.isInWater() || !this.squid.hasMovementVector()) {
                var f = this.squid.getRandom().nextFloat() * (float) (Math.PI * 2);
                var tx = Mth.cos(f) * 0.2F;
                var ty = -0.1F + this.squid.getRandom().nextFloat() * 0.18F;
                var tz = Mth.sin(f) * 0.2F;
                this.squid.setMovementVector(tx, ty, tz);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "spawnInk")
    private void spawnInk(CallbackInfo info) {
        if (!this.level().isClientSide()) {
            for (LivingEntity entity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(2.25F, 2.25F, 2.25F))) {
                if (!(entity instanceof Squid)) {
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
