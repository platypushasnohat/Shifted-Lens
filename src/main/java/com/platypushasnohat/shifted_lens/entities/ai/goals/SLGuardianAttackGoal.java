package com.platypushasnohat.shifted_lens.entities.ai.goals;

import com.platypushasnohat.shifted_lens.entities.utils.SLPoses;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;

import java.util.EnumSet;

public class SLGuardianAttackGoal extends Goal {

    private final Guardian guardian;
    private int attackTime;
    private final boolean elder;

    public SLGuardianAttackGoal(Guardian guardian) {
        this.guardian = guardian;
        this.elder = guardian instanceof ElderGuardian;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        LivingEntity livingentity = this.guardian.getTarget();
        return livingentity != null && livingentity.isAlive();
    }

    public boolean canContinueToUse() {
        return super.canContinueToUse() && (this.elder || this.guardian.getTarget() != null && this.guardian.distanceToSqr(this.guardian.getTarget()) > 7.0D);
    }

    public void start() {
        this.attackTime = -10;
        this.guardian.getNavigation().stop();
        this.guardian.hasImpulse = true;
        this.guardian.setPose(Pose.SWIMMING);
        LivingEntity target = this.guardian.getTarget();
        if (target != null) {
            this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
        }
    }

    public void stop() {
        this.guardian.setActiveAttackTarget(0);
        this.guardian.setTarget(null);
        this.guardian.randomStrollGoal.trigger();
        this.guardian.setPose(Pose.SWIMMING);
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void tick() {
        LivingEntity target = this.guardian.getTarget();
        if (target != null) {
            this.guardian.getNavigation().stop();
            this.guardian.getLookControl().setLookAt(target, 90.0F, 90.0F);
            if (!this.guardian.hasLineOfSight(target)) {
                this.guardian.setTarget(null);
            } else {
                this.attackTime++;

                if (this.attackTime == 0) {
                    this.guardian.setPose(SLPoses.BEAM_START.get());
                }
                if (this.attackTime == 8) {
                    this.guardian.setPose(SLPoses.BEAM.get());
                }
                if (this.attackTime == this.guardian.getAttackDuration() - 2) {
                    this.guardian.setPose(SLPoses.BEAM_END.get());
                }

                if (this.attackTime == 0) {
                    this.guardian.setActiveAttackTarget(target.getId());
//                    if (!this.guardian.isSilent()) {
//                        this.guardian.level().broadcastEntityEvent(this.guardian, (byte) 21);
//                    }
                } else if (this.attackTime >= this.guardian.getAttackDuration()) {
                    float f = 1.0F;
                    if (this.guardian.level().getDifficulty() == Difficulty.HARD) {
                        f += 2.0F;
                    }

                    if (this.elder) {
                        f += 2.0F;
                    }

                    target.hurt(this.guardian.damageSources().indirectMagic(this.guardian, this.guardian), f);
                    target.hurt(this.guardian.damageSources().mobAttack(this.guardian), (float)this.guardian.getAttributeValue(Attributes.ATTACK_DAMAGE));
                    this.guardian.setTarget(null);
                }
                super.tick();
            }
        }
    }
}
