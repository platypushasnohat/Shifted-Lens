package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.ai.goals.RabbitBreedGoal;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Optional;

@Mixin(Rabbit.class)
public abstract class RabbitMixin extends Animal implements VariantHolder<Rabbit.Variant> {

    @Shadow
    @Final
    private static EntityDataAccessor<Integer> DATA_TYPE_ID = SynchedEntityData.defineId(Rabbit.class, EntityDataSerializers.INT);

    protected RabbitMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends Rabbit> entityType, Level level, CallbackInfo ci) {
        this.setMaxUpStep(1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(1, new ClimbOnTopOfPowderSnowGoal(this, this.level()));
        this.goalSelector.addGoal(2, new RabbitBreedGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.2D, Ingredient.of(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), false));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
    }

    @Inject(method = "getJumpPower()F", at = @At("HEAD"), cancellable = true)
    private void jumpPower(CallbackInfoReturnable<Float> cir) {
        cir.cancel();
        float f = 0.3F;

        if (this.horizontalCollision || this.moveControl.hasWanted() && this.moveControl.getWantedY() > this.getY() + 0.5D) {
            f = 0.6F;
        }

        Path path = this.navigation.getPath();
        if (path != null && !path.isDone()) {
            Vec3 vec3 = path.getNextEntityPos(this);
            if (vec3.y > this.getY() + 0.5D) {
                f = 0.6F;
            }
        }

        if (this.moveControl.getSpeedModifier() <= 0.6D) {
            f = 0.3F;
        }
        cir.setReturnValue(f + this.getJumpBoostPower());
    }

    @Override
    protected int calculateFallDamage(float f, float f1) {
        return super.calculateFallDamage(f, f1) - 12;
    }

    @Override
    public void setVariant(Rabbit.Variant variant) {
        Rabbit rabbit = (Rabbit) (Object) this;
        if (variant == Rabbit.Variant.EVIL) {
            this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(5.0D);
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(8.0D);
            this.heal(this.getMaxHealth());
            this.goalSelector.addGoal(4, new MeleeAttackGoal(rabbit, 1.75D, false));
            this.targetSelector.addGoal(1, (new HurtByTargetGoal(rabbit)).setAlertOthers());
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(rabbit, Player.class, true));
            this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(rabbit, LivingEntity.class, 10, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.KILLER_RABBIT_TARGETS)));
        }
        this.entityData.set(DATA_TYPE_ID, variant.id());
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        if (this.getVariant() == Rabbit.Variant.EVIL) {
            this.playSound(SoundEvents.RABBIT_ATTACK, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
            return super.doHurtTarget(entity);
        }
        return super.doHurtTarget(entity);
    }

    @Inject(method = "getRandomRabbitVariant(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;)Lnet/minecraft/world/entity/animal/Rabbit$Variant;", at = @At("TAIL"), cancellable = true)
    private static void getRabbitVariant(LevelAccessor level, BlockPos pos, CallbackInfoReturnable<Rabbit.Variant> cir) {
        int i = level.getRandom().nextInt(2500);
        if (i < 1) {
            cir.setReturnValue(Rabbit.Variant.EVIL);
        }
    }

    @Override
    public void finalizeSpawnChildFromBreeding(ServerLevel level, Animal animal, @Nullable AgeableMob ageableMob) {
        Optional.ofNullable(this.getLoveCause()).or(() -> Optional.ofNullable(animal.getLoveCause())).ifPresent((player) -> {
            player.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(player, this, animal, ageableMob);
        });
        this.setAge(6000);
        animal.setAge(6000);
        this.resetLove();
        animal.resetLove();
        level.broadcastEntityEvent(this, (byte) 18);
    }
}
