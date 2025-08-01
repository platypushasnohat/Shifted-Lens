package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public class CustomRandomSwimGoal extends RandomStrollGoal {

    PathfinderMob fish;
    Vec3 wantedPos;

    int radius;
    int height;
    int prox;

    public CustomRandomSwimGoal(PathfinderMob fish, double speedMultiplier, int interval, int radius, int height, int proximity) {
        super(fish, speedMultiplier, interval);
        this.fish = fish;
        this.radius = radius;
        this.height = height;
        this.prox = proximity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.fish.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        wantedPos = new Vec3(this.wantedX, this.wantedY, this.wantedZ);
        return super.canContinueToUse() && !(this.wantedPos.distanceTo(this.fish.position()) <= this.fish.getBbWidth() * prox) && this.fish.isInWater();
    }

    @Nullable
    protected Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.mob, radius, height);
    }
}
