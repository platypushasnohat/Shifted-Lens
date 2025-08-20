package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.entities.ai.goals.SLGuardianAttackSelector;
import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import com.platypushasnohat.shifted_lens.mixin_utils.GuardianAnimationAccess;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Guardian.class)
public abstract class GuardianMixin extends Monster implements GuardianAnimationAccess {

    @Shadow
    private static final EntityDataAccessor<Integer> DATA_ID_ATTACK_TARGET = SynchedEntityData.defineId(Guardian.class, EntityDataSerializers.INT);

    @Shadow
    public boolean hasActiveAttackTarget() {
        return this.entityData.get(DATA_ID_ATTACK_TARGET) != 0;
    }

    @Unique
    private final AnimationState idleAnimationState = new AnimationState();
    @Unique
    private final AnimationState eyeAnimationState = new AnimationState();
    @Unique
    private final AnimationState beamStartAnimationState = new AnimationState();
    @Unique
    private final AnimationState beamAnimationState = new AnimationState();
    @Unique
    private final AnimationState beamEndAnimationState = new AnimationState();

    @Unique
    public AnimationState getIdleAnimationState() {
        return idleAnimationState;
    }
    @Unique
    public AnimationState getEyeAnimationState() {
        return eyeAnimationState;
    }
    @Unique
    public AnimationState getBeamStartAnimationState() {
        return beamStartAnimationState;
    }
    @Unique
    public AnimationState getBeamAnimationState() {
        return beamAnimationState;
    }
    @Unique
    public AnimationState getBeamEndAnimationState() {
        return beamEndAnimationState;
    }

    @Unique
    private int idleAnimationTimeout = 0;

    protected GuardianMixin(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1.0, 80));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0F, 0.01F));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, new SLGuardianAttackSelector(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Squid.class, 20, true, false, new SLGuardianAttackSelector(this)));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Axolotl.class, 20, true, false, new SLGuardianAttackSelector(this)));
    }

    @Inject(method = "<init>", at = @At("TAIL"), cancellable = true)
    private void init(EntityType<? extends Guardian> entityType, Level level, CallbackInfo callbackInfo) {
        callbackInfo.cancel();
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.xpReward = 10;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    public void travel(@NotNull Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWater()) {
            this.moveRelative(this.getSpeed(), vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
            this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.005, 0.0));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public int getMaxHeadXRot() {
        return 500;
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
        if (this.idleAnimationTimeout == 0) {
            this.idleAnimationTimeout = 160;
            this.idleAnimationState.start(this.tickCount);
        } else {
            --this.idleAnimationTimeout;
        }
        this.eyeAnimationState.animateWhen(this.isAlive() && this.getTarget() == null && !this.hasActiveAttackTarget(), this.tickCount);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> accessor) {
        super.onSyncedDataUpdated(accessor);
        if (this.getPose() == SLPoses.BEAM_START.get()) {
            this.beamStartAnimationState.start(this.tickCount);
        }
        else if (this.getPose() == SLPoses.BEAM.get()) {
            this.beamStartAnimationState.stop();
            this.beamAnimationState.start(this.tickCount);
        }
        else if (this.getPose() == SLPoses.BEAM_END.get()) {
            this.beamAnimationState.stop();
            this.beamStartAnimationState.stop();
            this.beamEndAnimationState.start(this.tickCount);
        }
        else if (this.getPose() == Pose.SWIMMING) {
            this.beamStartAnimationState.stop();
            this.beamAnimationState.stop();
            this.beamEndAnimationState.stop();
        }
    }
}
