package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.mixin_utils.CamelExtension;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerVanillaSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(EntityType.CAMEL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, CamelExtension::checkCamelSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.COD, Attributes.MAX_HEALTH, 4.0D);
        event.add(EntityType.COD, Attributes.MOVEMENT_SPEED, 0.8D);

        event.add(EntityType.ELDER_GUARDIAN, Attributes.MOVEMENT_SPEED, 0.8D);

        event.add(EntityType.GHAST, Attributes.MAX_HEALTH, 20.0D);
        event.add(EntityType.GHAST, Attributes.FLYING_SPEED, 0.11D);
        event.add(EntityType.GHAST, Attributes.FOLLOW_RANGE, 96.0D);

        event.add(EntityType.GUARDIAN, Attributes.MOVEMENT_SPEED, 1.0D);

        event.add(EntityType.PUFFERFISH, Attributes.MOVEMENT_SPEED, 0.5D);

        event.add(EntityType.RABBIT, Attributes.ATTACK_DAMAGE, 1.0D);

        event.add(EntityType.SALMON, Attributes.MAX_HEALTH, 6.0D);
        event.add(EntityType.SALMON, Attributes.MOVEMENT_SPEED, 0.8D);

        event.add(EntityType.SQUID, Attributes.MOVEMENT_SPEED, 0.64D);
        event.add(EntityType.GLOW_SQUID, Attributes.MOVEMENT_SPEED, 0.64D);

        event.add(EntityType.TROPICAL_FISH, Attributes.MOVEMENT_SPEED, 0.9D);
    }
}
