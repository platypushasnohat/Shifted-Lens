package com.platypushasnohat.shifted_lens.entities.ai.goals;

import com.platypushasnohat.shifted_lens.entities.Squill;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;

public class SquillAttackGoal extends Goal {

    private final Squill squill;

    private Vec3 startOrbitFrom;
    private int orbitTime;
    private int maxOrbitTime;
    private int attackTime;

    public SquillAttackGoal(Squill entity) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.squill = entity;
    }

    @Override
    public void start() {
        this.orbitTime = 0;
        this.maxOrbitTime = 80;
        this.startOrbitFrom = null;
        this.squill.setAttacking(false);
    }

    @Override
    public void stop() {
        LivingEntity target = squill.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) {
            squill.setTarget(null);
        }
        this.squill.setAggressive(false);
        this.squill.setAttacking(false);
        this.squill.getNavigation().stop();
    }

    @Override
    public boolean canUse() {
        LivingEntity target = squill.getTarget();
        return target != null && target.isAlive() && !squill.isPassenger() && this.squill.getCombatCooldown() <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = squill.getTarget();
        if (target == null) {
            return false;
        } else if (!target.isAlive()) {
            return false;
        } else if (!squill.isWithinRestriction(target.blockPosition())) {
            return false;
        } else if (this.squill.getCombatCooldown() > 0) {
            return false;
        } else {
            return !(target instanceof Player) || !target.isSpectator() && !((Player) target).isCreative() || !squill.getNavigation().isDone();
        }
    }

    @Override
    public void tick() {
        LivingEntity target = squill.getTarget();
        if (target != null && target.isAlive()) {
            double distance = squill.distanceTo(target);
            if (startOrbitFrom == null) {
                squill.getNavigation().moveTo(target, 4D);
                squill.lookAt(EntityAnchorArgument.Anchor.EYES, target.getEyePosition());
            } else if (orbitTime < maxOrbitTime) {
                orbitTime++;
                float zoomIn = 1F - orbitTime / (float) maxOrbitTime;
                Vec3 orbitPos = orbitAroundPos(3.0F + zoomIn * 5.0F).add(0, 4 + zoomIn * 3, 0);
                squill.getNavigation().moveTo(orbitPos.x, orbitPos.y, orbitPos.z, 3D);
                squill.lookAt(EntityAnchorArgument.Anchor.EYES, orbitPos);
            } else {
                orbitTime = 0;
                startOrbitFrom = null;
            }
            if (distance <= 16.0D && orbitTime <= 0) {
                tickAttack();
            }
        }
    }

    protected void tickAttack() {
        attackTime++;
        LivingEntity target = squill.getTarget();
        double distance = squill.distanceTo(target);
        float attackReach = squill.getBbWidth() + target.getBbWidth();
        squill.setAttacking(true);

        if (attackTime <= 4) {
            squill.getNavigation().stop();
            squill.lookAt(Objects.requireNonNull(target), 360F, 360F);
            squill.getLookControl().setLookAt(target, 360F, 360F);
        }

        if (attackTime > 8 && attackTime <= 32) {
            if (distance < attackReach + 0.5D) {
                squill.doHurtTarget(target);
                squill.swing(InteractionHand.MAIN_HAND);
                maxOrbitTime = 40 + squill.getRandom().nextInt(60);
                startOrbitFrom = target.getEyePosition();
                attackTime = 0;
                squill.setAttacking(false);
            }
        } else if (attackTime > 32) {
            maxOrbitTime = 40 + squill.getRandom().nextInt(60);
            startOrbitFrom = target.getEyePosition();
            attackTime = 0;
            squill.setAttacking(false);
        }
    }

    public Vec3 orbitAroundPos(float circleDistance) {
        final float angle = 3 * (float) (Math.toRadians(orbitTime * 3F));
        final double extraX = circleDistance * Mth.sin((angle));
        final double extraZ = circleDistance * Mth.cos(angle);
        return startOrbitFrom.add(extraX, 0, extraZ);
    }
}
