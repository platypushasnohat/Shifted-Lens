package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.*;
import com.platypushasnohat.shifted_lens.entities.ai.goals.*;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.*;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide && entity instanceof Squill squill) {
            squill.updatePull(entity.position());
        }

        if (entity instanceof Mob mob) {
            if (mob instanceof Ghast ghast) {
                ghast.goalSelector.addGoal(0, new SLGhastWanderGoal(ghast));
                ghast.goalSelector.addGoal(1, new Ghast.GhastLookGoal(ghast));
                ghast.goalSelector.addGoal(2, new SLGhastAttackGoal(ghast));
            }

            if (mob instanceof Guardian guardian) {
                guardian.goalSelector.addGoal(2, new SLGuardianAttackGoal(guardian));
                guardian.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(guardian, LivingEntity.class, 20, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.GUARDIAN_TARGETS) && livingEntity.distanceToSqr(guardian) > 7.0D));
            }
            if (mob instanceof ElderGuardian guardian) {
                guardian.goalSelector.addGoal(2, new SLGuardianAttackGoal(guardian));
                guardian.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(guardian, LivingEntity.class, 20, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.GUARDIAN_TARGETS) && livingEntity.distanceToSqr(guardian) > 7.0D));
            }
        }
    }
}
