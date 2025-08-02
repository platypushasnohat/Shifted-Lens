package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.*;
import com.platypushasnohat.shifted_lens.entities.projectiles.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SLEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ShiftedLens.MOD_ID);

    public static final RegistryObject<EntityType<SLGhast>> GHAST = ENTITY_TYPES.register(
            "ghast", () ->
            EntityType.Builder.of(SLGhast::new, MobCategory.MONSTER)
                    .sized(4.0F, 4.0F)
                    .clientTrackingRange(10)
                    .fireImmune()
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "ghast").toString())
    );

    public static final RegistryObject<EntityType<GhastFireball>> GHAST_FIREBALL = ENTITY_TYPES.register(
            "ghast_fireball", () ->
            EntityType.Builder.<GhastFireball>of(GhastFireball::new, MobCategory.MISC)
                    .sized(1.0F, 1.0F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "ghast_fireball").toString())
    );

    public static final RegistryObject<EntityType<SLGuardian>> GUARDIAN = ENTITY_TYPES.register(
            "guardian", () ->
            EntityType.Builder.of(SLGuardian::new, MobCategory.MONSTER)
                    .sized(0.85F, 0.85F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "guardian").toString())
    );

    public static final RegistryObject<EntityType<SLElderGuardian>> ELDER_GUARDIAN = ENTITY_TYPES.register(
            "elder_guardian", () ->
            EntityType.Builder.of(SLElderGuardian::new, MobCategory.MONSTER)
                    .sized(1.9975F, 1.9975F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "elder_guardian").toString())
    );

    public static final RegistryObject<EntityType<Anchovy>> ANCHOVY = ENTITY_TYPES.register(
            "anchovy", () ->
            EntityType.Builder.of(Anchovy::new, MobCategory.WATER_AMBIENT)
                    .sized(0.4F, 0.2F)
                    .clientTrackingRange(4)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "anchovy").toString())
    );

    public static final RegistryObject<EntityType<Squill>> SQUILL = ENTITY_TYPES.register(
            "squill", () ->
            EntityType.Builder.of(Squill::new, MobCategory.AMBIENT)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(8)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "squill").toString())
    );
}
