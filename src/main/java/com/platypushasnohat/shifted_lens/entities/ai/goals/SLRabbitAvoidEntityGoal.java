package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Rabbit;

import java.util.function.Predicate;

public class SLRabbitAvoidEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    private final Rabbit rabbit;

    public SLRabbitAvoidEntityGoal(Rabbit rabbit, Class<T> entity, float distance) {
        super(rabbit, entity, distance, 2.2D, 2.2D);
        this.rabbit = rabbit;
    }

    public SLRabbitAvoidEntityGoal(Rabbit rabbit, Class<T> entity, Predicate<LivingEntity> predicate) {
        super(rabbit, entity, predicate, 10.0F, 2.2D, 2.2D, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.rabbit = rabbit;
    }

    @Override
    public boolean canUse() {
        return this.rabbit.getVariant() != Rabbit.Variant.EVIL && super.canUse();
    }
}