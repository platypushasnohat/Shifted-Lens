package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SLGhastAttackGoal extends Goal {

    private final Ghast ghast;
    public int chargeTime;
    private Vec3 startOrbitFrom;
    private int orbitTime;
    private int maxOrbitTime;
    private boolean clockwise;

    public SLGhastAttackGoal(Ghast ghast) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        this.ghast = ghast;
    }

    @Override
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

    @Override
    public void start() {
        this.chargeTime = 0;
        this.orbitTime = 0;
        this.maxOrbitTime = 40;
        this.startOrbitFrom = null;
        this.ghast.setCharging(false);
        this.ghast.setAggressive(true);
    }

    @Override
    public void stop() {
        this.ghast.setCharging(false);
        LivingEntity target = this.ghast.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            this.ghast.setTarget(null);
        }
        this.ghast.setAggressive(false);
        this.ghast.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = ghast.getTarget();
        RandomSource random = ghast.getRandom();
        if (target != null) {
            this.ghast.getLookControl().setLookAt(target, 10.0F, this.ghast.getMaxHeadXRot());

            if (startOrbitFrom == null) {
                this.ghast.getNavigation().moveTo(target, 1.0D);
            } else if (orbitTime < maxOrbitTime) {
                this.orbitTime++;
                Vec3 orbitPos = orbitAroundPos(32.0F);
                this.ghast.getNavigation().moveTo(orbitPos.x, orbitPos.y + 3 + random.nextInt(6), orbitPos.z, 1.0D);
            } else {
                this.orbitTime = 0;
                this.startOrbitFrom = null;
            }

            if (orbitTime == 0) {
                this.ghast.getNavigation().stop();
                this.chargeTime++;

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
                }

                if (chargeTime > 40) {
                    this.chargeTime = 0;
                    this.clockwise = random.nextBoolean();
                    this.maxOrbitTime = 40 + ghast.getRandom().nextInt(20);
                    this.startOrbitFrom = target.getEyePosition();
                }
            }
        }
    }

//    @Override
//    public void tick() {
//        LivingEntity target = this.ghast.getTarget();
//
//
//        if (target != null) {
//            if (this.ghast.distanceTo(target) > 30) {
//                this.ghast.getNavigation().moveTo(target, 1.0F);
//            }
//
//            if (this.ghast.getSensing().hasLineOfSight(target)) {
//                this.ghast.getNavigation().stop();
//                this.chargeTime++;
//
//                this.ghast.getLookControl().setLookAt(target, 30.0F, this.ghast.getMaxHeadXRot());
//
//                if (this.chargeTime == 32) {
//                    ghast.playSound(SoundEvents.GHAST_WARN, 10.0F, ghast.getVoicePitch());
//                }
//
//                if (this.chargeTime == 40) {
//                    if (!this.ghast.isSilent()) {
//                        this.ghast.playSound(SoundEvents.GHAST_SHOOT, 10.0F, this.ghast.getVoicePitch());
//                    }
//
//                    Vec3 vec3 = this.ghast.getViewVector(1.0F);
//                    double targetX = target.getX() - (this.ghast.getX() + vec3.x * 4.0D);
//                    double targetY = target.getY(0.5D) - (0.5D + this.ghast.getY(0.5D));
//                    double targetZ = target.getZ() - (this.ghast.getZ() + vec3.z * 4.0D);
//
//                    LargeFireball largefireball = new LargeFireball(this.ghast.level(), this.ghast, targetX, targetY, targetZ, this.ghast.getExplosionPower());
//                    largefireball.setPos(mouth);
//                    this.ghast.level().addFreshEntity(largefireball);
//                    this.chargeTime = -20;
//                }
//            } else if (this.chargeTime > 0) {
//                this.chargeTime--;
//            }
//
//            this.ghast.setCharging(this.chargeTime > 10);
//        }
//    }

    public Vec3 orbitAroundPos(float circleDistance) {
        final float angle = 2.0F * (float) (Math.toRadians((clockwise ? -orbitTime : orbitTime) * 2.0F));
        final double extraX = circleDistance * Mth.sin((angle));
        final double extraZ = circleDistance * Mth.cos(angle);
        return startOrbitFrom.add(extraX, 0.0F, extraZ);
    }
}