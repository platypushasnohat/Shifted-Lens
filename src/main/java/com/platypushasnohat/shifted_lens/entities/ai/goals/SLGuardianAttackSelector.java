package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;

import javax.annotation.Nullable;
import java.util.function.Predicate;

public class SLGuardianAttackSelector implements Predicate<LivingEntity> {
    private final Monster guardian;

    public SLGuardianAttackSelector(Monster guardian) {
        this.guardian = guardian;
    }

    public boolean test(@Nullable LivingEntity entity) {
        return entity.distanceToSqr(this.guardian) > 8.0D;
    }
}
