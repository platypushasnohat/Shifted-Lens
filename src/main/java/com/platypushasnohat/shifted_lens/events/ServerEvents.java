package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.*;
import com.platypushasnohat.shifted_lens.entities.ai.goals.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Cod;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
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
            if (mob instanceof Guardian guardian) {
                AttributeInstance movementSpeed = guardian.getAttribute(Attributes.MOVEMENT_SPEED);
                if (movementSpeed != null) movementSpeed.setBaseValue(1.0F);

                AttributeInstance followRange = guardian.getAttribute(Attributes.FOLLOW_RANGE);
                if (followRange != null) followRange.setBaseValue(24.0D);

                guardian.goalSelector.addGoal(2, new SLGuardianAttackGoal(guardian));
            }
            if (mob instanceof ElderGuardian guardian) {
                AttributeInstance movementSpeed = guardian.getAttribute(Attributes.MOVEMENT_SPEED);
                if (movementSpeed != null) movementSpeed.setBaseValue(0.8F);

                AttributeInstance followRange = guardian.getAttribute(Attributes.FOLLOW_RANGE);
                if (followRange != null) followRange.setBaseValue(24.0D);

                guardian.goalSelector.addGoal(2, new SLGuardianAttackGoal(guardian));
            }

            if (mob instanceof Cod cod) {
                AttributeInstance movementSpeed = cod.getAttribute(Attributes.MOVEMENT_SPEED);
                if (movementSpeed != null) movementSpeed.setBaseValue(1.0F);
            }
            if (mob instanceof Salmon salmon) {
                AttributeInstance movementSpeed = salmon.getAttribute(Attributes.MOVEMENT_SPEED);
                if (movementSpeed != null) movementSpeed.setBaseValue(0.9F);
            }
            if (mob instanceof TropicalFish tropicalFish) {
                AttributeInstance movementSpeed = tropicalFish.getAttribute(Attributes.MOVEMENT_SPEED);
                if (movementSpeed != null) movementSpeed.setBaseValue(1.0F);
            }
        }
    }
}
