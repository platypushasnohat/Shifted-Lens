package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;

public class SLGhastAttackGoal extends Goal {

    private final Ghast ghast;
    public int chargeTime;

    public SLGhastAttackGoal(Ghast ghast) {
        this.ghast = ghast;
    }

    public boolean canUse() {
        return this.ghast.getTarget() != null;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.ghast.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!this.ghast.isWithinRestriction(target.blockPosition())) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative() || !this.ghast.getNavigation().isDone();
        }
    }

    public void start() {
        this.chargeTime = 0;
        this.ghast.setCharging(false);
        this.ghast.setAggressive(true);
    }

    public void stop() {
        this.ghast.setCharging(false);
        LivingEntity target = this.ghast.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.ghast.setTarget(null);
        }
        this.ghast.setAggressive(false);
        this.ghast.getNavigation().stop();
    }

    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = this.ghast.getTarget();

        if (target != null) {
            if (target.distanceToSqr(this.ghast) < 4096.0D && this.ghast.getSensing().hasLineOfSight(target)) {
                this.ghast.getNavigation().stop();
                this.chargeTime++;

                this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());

                if (this.chargeTime == 32) {
                    ghast.playSound(SoundEvents.GHAST_WARN, 10.0F, ghast.getVoicePitch());
                }

                if (this.chargeTime == 40) {
                    if (!this.ghast.isSilent()) {
                        this.ghast.playSound(SoundEvents.GHAST_SHOOT, 10.0F, this.ghast.getVoicePitch());
                    }

                    Vec3 vec3 = this.ghast.getViewVector(1.0F);
                    double targetX = target.getX() - (this.ghast.getX() + vec3.x * 4.0D);
                    double targetY = target.getY(0.5D) - (0.5D + this.ghast.getY(0.5D));
                    double targetZ = target.getZ() - (this.ghast.getZ() + vec3.z * 4.0D);

                    LargeFireball largefireball = new LargeFireball(this.ghast.level(), this.ghast, targetX, targetY, targetZ, this.ghast.getExplosionPower());
                    largefireball.setPos(this.ghast.getX() + vec3.x * 4.0D, this.ghast.getY(0.5D) + 0.5D, largefireball.getZ() + vec3.z * 4.0D);
                    this.ghast.level().addFreshEntity(largefireball);
                    this.chargeTime = -40;
                }
            } else if (this.chargeTime > 0) {
                this.chargeTime--;
            }

            this.ghast.setCharging(this.chargeTime > 10);
        }
    }
}