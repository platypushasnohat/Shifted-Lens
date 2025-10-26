package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SLGhastAttackGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SLGhastFlightGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SLGhastLookGoal;
import com.platypushasnohat.shifted_lens.entities.ai.navigation.SLGhastMoveControl;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Ghast.class)
public abstract class GhastMixin extends Mob implements Enemy, AnimationStateAccess {

    @Shadow
    public abstract boolean isCharging();

    @Unique
    private final AnimationState shiftedLens$ghastIdleAnimationState = new AnimationState();

    @Unique
    private final AnimationState shiftedLens$ghastShootAnimationState = new AnimationState();

    @Override
    public AnimationState shiftedLens$getIdleAnimationState() {
        return shiftedLens$ghastIdleAnimationState;
    }

    @Override
    public AnimationState shiftedLens$getShootAnimationState() {
        return shiftedLens$ghastShootAnimationState;
    }

    protected GhastMixin(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(EntityType<? extends FlyingMob> entityType, Level level, CallbackInfo ci) {
        if (ShiftedLensConfig.BETTER_GHAST_AI.get()) {
            this.moveControl = new SLGhastMoveControl(this);
        }
        this.xpReward = 10;
    }

    @Inject(method = "registerGoals()V", at = @At("HEAD"), cancellable = true)
    protected void registerGoals(CallbackInfo ci) {
        if (ShiftedLensConfig.BETTER_GHAST_AI.get()) {
            ci.cancel();
            this.goalSelector.addGoal(0, new SLGhastLookGoal((Ghast) (Object) this));
            this.goalSelector.addGoal(1, new SLGhastAttackGoal((Ghast) (Object) this));
            this.goalSelector.addGoal(2, new SLGhastFlightGoal((Ghast) (Object) this, 10, 10, 10, 1.0F));
            this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
            this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, (entity) -> entity.getType().is(SLEntityTags.GHAST_TARGETS)));
        }
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        if (ShiftedLensConfig.BETTER_GHAST_AI.get()) {
            if (this.isEffectiveAi()) {
                this.moveRelative(0.1F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
            } else {
                super.travel(travelVector);
            }
        }
        super.travel(travelVector);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        if (ShiftedLensConfig.BETTER_GHAST_AI.get()) {
            FlyingPathNavigation pathNavigation = new FlyingPathNavigation(this, level) {
                public boolean isStableDestination(@NotNull BlockPos pos) {
                    return this.level.getBlockState(pos).isAir();
                }
            };
            pathNavigation.setCanOpenDoors(false);
            pathNavigation.setCanFloat(true);
            pathNavigation.setCanPassDoors(true);
            return pathNavigation;
        }
        return super.createNavigation(level);
    }

    @Override
    public boolean isNoGravity() {
        if (ShiftedLensConfig.BETTER_GHAST_AI.get()) {
            return true;
        }
        return super.isNoGravity();
    }

    @Override
    public int getMaxHeadXRot() {
        if (ShiftedLensConfig.BETTER_GHAST_AI.get()) {
            return 500;
        }
        return super.getMaxHeadXRot();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            this.shiftedLens$ghastSetupAnimationStates();
        }
    }

    @Unique
    private void shiftedLens$ghastSetupAnimationStates() {
        this.shiftedLens$ghastIdleAnimationState.animateWhen(this.isAlive(), this.tickCount);
        this.shiftedLens$ghastShootAnimationState.animateWhen(this.isAlive() && this.isCharging(), this.tickCount);
    }

//    @Override
//    protected void tickDeath() {
//        boolean flag = ForgeEventFactory.getMobGriefingEvent(this.level(), this);
//        this.deathTime++;
//        if (this.deathTime >= 20 && !this.level().isClientSide() && !this.isRemoved()) {
//            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.getExplosionPower(), flag, Level.ExplosionInteraction.MOB);
//            this.level().broadcastEntityEvent(this, (byte) 60);
//            this.remove(Entity.RemovalReason.KILLED);
//        }
//    }
}
