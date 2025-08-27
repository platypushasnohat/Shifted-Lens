package com.platypushasnohat.shifted_lens.entities.ai.goals;

import com.mojang.datafixers.DataFixUtils;
import com.platypushasnohat.shifted_lens.entities.FlyingFish;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.List;
import java.util.function.Predicate;

public class FlyingFishFollowLeaderGoal extends Goal {

    private final FlyingFish fish;
    private int timeToRecalcPath;
    private int nextStartTick;

    public FlyingFishFollowLeaderGoal(FlyingFish fish) {
        this.fish = fish;
        this.nextStartTick = this.nextStartTick(fish);
    }

    protected int nextStartTick(FlyingFish fish) {
        return reducedTickDelay(200 + fish.getRandom().nextInt(200) % 20);
    }

    public boolean canUse() {
        if (this.fish.hasFollowers()) {
            return false;
        } else if (this.fish.isFollower()) {
            return true;
        } else if (this.nextStartTick > 0) {
            --this.nextStartTick;
            return false;
        } else {
            this.nextStartTick = this.nextStartTick(this.fish);
            Predicate<FlyingFish> predicate = (fish1) -> fish1.canBeFollowed() || !fish1.isFollower();
            List<? extends FlyingFish> list = this.fish.level().getEntitiesOfClass(this.fish.getClass(), this.fish.getBoundingBox().inflate(10.0D, 10.0D, 10.0D), predicate);
            FlyingFish schoolingWaterAnimal = DataFixUtils.orElse(list.stream().filter(FlyingFish::canBeFollowed).findAny(), this.fish);
            schoolingWaterAnimal.addFollowers(list.stream().filter((fish2) -> !fish2.isFollower()));
            return this.fish.isFollower();
        }
    }

    public boolean canContinueToUse() {
        return this.fish.isFollower() && this.fish.inRangeOfLeader();
    }

    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        this.fish.stopFollowing();
    }

    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            this.fish.pathToLeader();
        }
    }
}
