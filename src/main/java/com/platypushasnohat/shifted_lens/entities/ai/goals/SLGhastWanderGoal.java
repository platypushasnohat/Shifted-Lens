package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;

import java.util.EnumSet;

public class SLGhastWanderGoal extends Goal {

    private final Ghast ghast;

    public SLGhastWanderGoal(Ghast ghast) {
        this.ghast = ghast;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    public boolean canUse() {
        MoveControl movecontrol = this.ghast.getMoveControl();
        if (!movecontrol.hasWanted()) {
            return true;
        } else {
            double d0 = movecontrol.getWantedX() - this.ghast.getX();
            double d1 = movecontrol.getWantedY() - this.ghast.getY();
            double d2 = movecontrol.getWantedZ() - this.ghast.getZ();
            double d3 = d0 * d0 + d1 * d1 + d2 * d2;
            return d3 < 1.0D || d3 > 3600.0D;
        }
    }

    public boolean canContinueToUse() {
        return false;
    }

    public void start() {
        RandomSource randomsource = this.ghast.getRandom();
        double d0 = this.ghast.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d1 = this.ghast.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        double d2 = this.ghast.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
        this.ghast.getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
    }
}