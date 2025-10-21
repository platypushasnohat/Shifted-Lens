package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SLGhastLookGoal extends Goal {

    private final Ghast ghast;

    public SLGhastLookGoal(Ghast ghast) {
        this.ghast = ghast;
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        if (this.ghast.getTarget() == null) {
            Vec3 vec3 = this.ghast.getDeltaMovement().scale(0.05F);
            this.ghast.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * (180F / (float)Math.PI));
            this.ghast.yBodyRot = this.ghast.getYRot();
        } else {
            LivingEntity livingentity = this.ghast.getTarget();
            if (livingentity.distanceToSqr(this.ghast) < 4096.0D) {
                double d1 = livingentity.getX() - this.ghast.getX();
                double d2 = livingentity.getZ() - this.ghast.getZ();
                this.ghast.setYRot(-((float)Mth.atan2(d1, d2)) * (180F / (float)Math.PI));
                this.ghast.yBodyRot = this.ghast.getYRot();
            }
        }
    }
}