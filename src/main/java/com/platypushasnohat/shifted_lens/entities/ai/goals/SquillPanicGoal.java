package com.platypushasnohat.shifted_lens.entities.ai.goals;

import com.platypushasnohat.shifted_lens.entities.Squill;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class SquillPanicGoal extends Goal {

    protected final Squill squill;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public SquillPanicGoal(Squill squill) {
        this.squill = squill;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        if (!this.shouldPanic()) {
            return false;
        } else {
            return this.findRandomPosition();
        }
    }

    protected boolean shouldPanic() {
        return this.squill.getLastHurtByMob() != null && this.squill.getCombatCooldown() > 0;
    }

    protected boolean findRandomPosition() {
        Vec3 view = squill.getViewVector(0.0F);
        Vec3 vec3 = HoverRandomPos.getPos(squill, 12, 12, view.x, view.z, ((float) Math.PI / 2F), 3, 1);
        if (vec3 == null) {
            return false;
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }
    }

    public void start() {
        this.squill.getNavigation().moveTo(this.posX, this.posY, this.posZ, 3.0D);
        this.isRunning = true;
    }

    public void stop() {
        this.isRunning = false;
    }

    public boolean canContinueToUse() {
        return !this.squill.getNavigation().isDone();
    }
}
