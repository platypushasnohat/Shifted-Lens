package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.animal.Rabbit;

public class KillerRabbitAttackGoal extends MeleeAttackGoal {

  public KillerRabbitAttackGoal(Rabbit rabbit) {
     super(rabbit, 1.4D, true);
  }

  protected double getAttackReachSqr(LivingEntity entity) {
     return 2.0F + entity.getBbWidth();
  }
}