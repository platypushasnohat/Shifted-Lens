package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.*;
import com.platypushasnohat.shifted_lens.entities.utils.CamelExtension;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEvents {

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(SLEntities.BAITFISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Baitfish::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(SLEntities.FLYING_FISH.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FlyingFish::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
        event.register(SLEntities.SQUILL.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING, Squill::canSpawn, SpawnPlacementRegisterEvent.Operation.OR);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerVanillaSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(EntityType.CAMEL, SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE_WG, CamelExtension::checkCamelSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(SLEntities.BAITFISH.get(), Baitfish.createAttributes().build());
        event.put(SLEntities.FLYING_FISH.get(), FlyingFish.createAttributes().build());
        event.put(SLEntities.SQUILL.get(), Squill.createAttributes().build());
    }

    @SubscribeEvent
    public static void modifyAttributes(EntityAttributeModificationEvent event) {
        event.add(EntityType.COD, Attributes.MAX_HEALTH, 4.0D);
        event.add(EntityType.COD, Attributes.MOVEMENT_SPEED, 0.8D);

        event.add(EntityType.ELDER_GUARDIAN, Attributes.MOVEMENT_SPEED, 0.8D);
        event.add(EntityType.ELDER_GUARDIAN, Attributes.FOLLOW_RANGE, 24.0D);

        event.add(EntityType.GHAST, Attributes.MAX_HEALTH, 20.0D);
        event.add(EntityType.GHAST, Attributes.FLYING_SPEED, 0.11D);

        event.add(EntityType.GUARDIAN, Attributes.MOVEMENT_SPEED, 1.0D);
        event.add(EntityType.GUARDIAN, Attributes.FOLLOW_RANGE, 24.0D);

        event.add(EntityType.PUFFERFISH, Attributes.MOVEMENT_SPEED, 0.5D);

        event.add(EntityType.SALMON, Attributes.MAX_HEALTH, 6.0D);
        event.add(EntityType.SALMON, Attributes.MOVEMENT_SPEED, 0.8D);

        event.add(EntityType.SQUID, Attributes.MOVEMENT_SPEED, 0.64D);

        event.add(EntityType.TROPICAL_FISH, Attributes.MOVEMENT_SPEED, 0.9D);
    }
}
