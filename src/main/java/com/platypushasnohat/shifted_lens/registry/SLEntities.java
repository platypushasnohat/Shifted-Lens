package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.Baitfish;
import com.platypushasnohat.shifted_lens.entities.FlyingFish;
import com.platypushasnohat.shifted_lens.entities.Squill;
import com.platypushasnohat.shifted_lens.entities.projectile.ToothedSnowball;
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

    public static final RegistryObject<EntityType<Baitfish>> BAITFISH = ENTITY_TYPES.register(
            "baitfish", () ->
            EntityType.Builder.of(Baitfish::new, MobCategory.WATER_AMBIENT)
                    .sized(0.4F, 0.2F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "baitfish").toString())
    );

    public static final RegistryObject<EntityType<FlyingFish>> FLYING_FISH = ENTITY_TYPES.register(
            "flying_fish", () ->
                    EntityType.Builder.of(FlyingFish::new, MobCategory.WATER_AMBIENT)
                            .sized(0.4F, 0.2F)
                            .clientTrackingRange(10)
                            .build(new ResourceLocation(ShiftedLens.MOD_ID, "flying_fish").toString())
    );

    public static final RegistryObject<EntityType<Squill>> SQUILL = ENTITY_TYPES.register(
            "squill", () ->
            EntityType.Builder.of(Squill::new, MobCategory.CREATURE)
                    .sized(0.8F, 0.8F)
                    .clientTrackingRange(10)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "squill").toString())
    );

    public static final RegistryObject<EntityType<ToothedSnowball>> TOOTHED_SNOWBALL = ENTITY_TYPES.register(
            "toothed_snowball", () ->
            EntityType.Builder.<ToothedSnowball>of(ToothedSnowball::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "toothed_snowball").toString())
    );
}
