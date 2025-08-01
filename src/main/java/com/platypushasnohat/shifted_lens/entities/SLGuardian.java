package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
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
import java.util.function.Predicate;

public class SLGuardian extends BaseGuardian {

    public SLGuardian(EntityType<? extends BaseGuardian> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0F)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.MAX_HEALTH, 32.0D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new GuardianAttackGoal(this));
        this.goalSelector.addGoal(2, new RandomSwimmingGoal(this, 1, 10));
        this.goalSelector.addGoal(4, new GuardianMoveTowardsRestrictionGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Guardian.class, 12.0F, 0.01F));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, LivingEntity.class, 10, true, false, new GuardianAttackSelector(this)));
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.GUARDIAN_SPAWN_EGG);
    }

    // goals
    static class GuardianAttackGoal extends Goal {

        private final SLGuardian guardian;
        private int attackTime;

        public GuardianAttackGoal(SLGuardian guardian) {
            this.guardian = guardian;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = this.guardian.getTarget();
            return target != null && target.isAlive();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && (this.guardian.getTarget() != null && this.guardian.distanceToSqr(this.guardian.getTarget()) > 12.0D);
        }

        @Override
        public void start() {
            this.attackTime = -10;
            this.guardian.getNavigation().stop();
            this.guardian.hasImpulse = true;
            this.guardian.setPose(Pose.SWIMMING);
        }

        @Override
        public void stop() {
            this.guardian.setActiveAttackTarget(0);
            this.guardian.setTarget(null);
            this.guardian.getNavigation().stop();
            this.guardian.setPose(Pose.SWIMMING);
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.guardian.getTarget();
            if (target != null) {
                this.guardian.getNavigation().stop();
                this.guardian.getLookControl().setLookAt(target, 360.0F, 360.0F);
                if (!this.guardian.hasLineOfSight(target)) {
                    this.guardian.setTarget(null);
                } else {
                    this.attackTime++;

                    if (this.attackTime == 0) {
                        this.guardian.setPose(SLPoses.BEAM_START.get());
                        this.guardian.setActiveAttackTarget(target.getId());
//                        if (!this.guardian.isSilent()) {
//                            this.guardian.level().broadcastEntityEvent(this.guardian, (byte) 21);
//                        }
                    }

                    if (this.attackTime == 8) {
                        this.guardian.setPose(SLPoses.BEAM.get());
                    }
                    if (this.attackTime == this.guardian.getAttackDuration() - 2) {
                        this.guardian.setPose(SLPoses.BEAM_END.get());
                    }

                    else if (this.attackTime >= this.guardian.getAttackDuration()) {

                        float damage = 1.0F;

                        if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
                            damage += 2.0F;
                        }

                        target.hurt(this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), damage);
                        target.hurt(this.guardian.damageSources().mobAttack(this.guardian), (float) this.guardian.getAttributeValue(Attributes.ATTACK_DAMAGE));
                        this.guardian.setTarget(null);
                    }
                    super.tick();
                }
            }
        }
    }

    static class GuardianAttackSelector implements Predicate<LivingEntity> {

        private final SLGuardian guardian;

        public GuardianAttackSelector(SLGuardian guardian) {
            this.guardian = guardian;
        }

        @Override
        public boolean test(@Nullable LivingEntity target) {
            return (target instanceof Player || target instanceof Squid || target instanceof Axolotl) && target.distanceToSqr(this.guardian) > 12.0D;
        }
    }
}
