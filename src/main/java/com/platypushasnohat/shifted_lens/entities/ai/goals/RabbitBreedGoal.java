package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;

public class RabbitBreedGoal extends BreedGoal {

    public RabbitBreedGoal(Animal rabbit) {
        super(rabbit, 1.1D);
    }

    @Override
    protected void breed() {
        if (this.partner != null) {
            this.level.addFreshEntity(new ExperienceOrb(level, animal.getX(), animal.getY(), animal.getZ(), animal.getRandom().nextInt(7) + 1));
            for (int i = 0; i < 4 + this.level.getRandom().nextInt(2); i++) {
                this.animal.spawnChildFromBreeding((ServerLevel) this.level, this.partner);
            }
        }
    }
}
