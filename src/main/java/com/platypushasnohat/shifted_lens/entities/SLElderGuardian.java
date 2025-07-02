package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class SLElderGuardian extends BaseGuardian {

    private int attackTime = 0;

    public SLElderGuardian(EntityType<? extends BaseGuardian> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 36;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.8F)
                .add(Attributes.ATTACK_DAMAGE, 8.0D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 100.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new ElderGuardianAttackGoal(this));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 10));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0F, 0.01F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new ElderGuardianAttackSelector(this)));
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.ELDER_GUARDIAN_SPAWN_EGG);
    }

    @Override
    public int getAttackDuration() {
        return 60;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_AMBIENT : SoundEvents.ELDER_GUARDIAN_AMBIENT_LAND;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_32468_) {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_HURT : SoundEvents.ELDER_GUARDIAN_HURT_LAND;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.isInWaterOrBubble() ? SoundEvents.ELDER_GUARDIAN_DEATH : SoundEvents.ELDER_GUARDIAN_DEATH_LAND;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ELDER_GUARDIAN_FLOP;
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if ((this.tickCount + this.getId()) % 1200 == 0) {
            List<ServerPlayer> list = MobEffectUtil.addEffectToPlayersAround((ServerLevel)this.level(), this, this.position(), 50.0D, new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 6000, 2), 1200);
            list.forEach((p_289459_) -> p_289459_.connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.GUARDIAN_ELDER_EFFECT, this.isSilent() ? 0.0F : 1.0F)));
        }
        if (!this.hasRestriction()) {
            this.restrictTo(this.blockPosition(), 16);
        }
    }

    // goals
    static class ElderGuardianAttackGoal extends Goal {

        private final SLElderGuardian elderGuardian;

        public ElderGuardianAttackGoal(SLElderGuardian elderGuardian) {
            this.elderGuardian = elderGuardian;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity livingentity = this.elderGuardian.getTarget();
            return livingentity != null && livingentity.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.elderGuardian.getTarget() != null && this.elderGuardian.distanceToSqr(this.elderGuardian.getTarget()) > 16.0D);
        }

        @Override
        public void start() {
            this.elderGuardian.attackTime = -10;
            this.elderGuardian.getNavigation().stop();
            LivingEntity livingentity = this.elderGuardian.getTarget();
            if (livingentity != null) {
                this.elderGuardian.getLookControl().setLookAt(livingentity, 360.0F, 360.0F);
            }
            this.elderGuardian.hasImpulse = true;
            this.elderGuardian.setPose(Pose.SWIMMING);
        }

        @Override
        public void stop() {
            this.elderGuardian.setActiveAttackTarget(0);
            this.elderGuardian.setTarget(null);
            this.elderGuardian.getNavigation().stop();
            this.elderGuardian.setPose(Pose.SWIMMING);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.elderGuardian.getTarget();
            if (target != null) {
                this.elderGuardian.getNavigation().stop();
                this.elderGuardian.getLookControl().setLookAt(target, 360.0F, 360.0F);
                if (!this.elderGuardian.hasLineOfSight(target)) {
                    this.elderGuardian.setTarget(null);
                } else {
                    this.elderGuardian.attackTime++;
                    if (this.elderGuardian.attackTime == 0) {
                        this.elderGuardian.setPose(SLPoses.BEAM_START.get());
                        this.elderGuardian.setActiveAttackTarget(target.getId());

                        if (!this.elderGuardian.isSilent()) {
                            this.elderGuardian.level().broadcastEntityEvent(this.elderGuardian, (byte) 21);
                        }
                    }
                    if (this.elderGuardian.attackTime == 8) {
                        this.elderGuardian.setPose(SLPoses.BEAM.get());
                    }
                    if (this.elderGuardian.attackTime == this.elderGuardian.getAttackDuration() - 2) {
                        this.elderGuardian.setPose(SLPoses.BEAM_END.get());
                    }

                    else if (this.elderGuardian.attackTime >= this.elderGuardian.getAttackDuration()) {

                        float damage = 2.0F;

                        if (this.elderGuardian.level().getDifficulty() == Difficulty.HARD) {
                            damage += 3.0F;
                        }

                        target.hurt(this.elderGuardian.damageSources().indirectMagic(this.elderGuardian, this.elderGuardian), damage);
                        target.hurt(this.elderGuardian.damageSources().mobAttack(this.elderGuardian), (float) this.elderGuardian.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        this.elderGuardian.setTarget(null);
                    }
                    super.tick();
                }
            }
        }
    }

    static class ElderGuardianAttackSelector implements Predicate<LivingEntity> {

        private final SLElderGuardian elderGuardian;

        public ElderGuardianAttackSelector(SLElderGuardian elderGuardian) {
            this.elderGuardian = elderGuardian;
        }

        @Override
        public boolean test(@Nullable LivingEntity target) {
            return (target instanceof Player || target instanceof Squid || target instanceof Axolotl) && target.distanceToSqr(this.elderGuardian) > 16.0D;
        }
    }
}
