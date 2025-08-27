package com.platypushasnohat.shifted_lens.registry.tags;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class SLBiomeTags {

    public static final TagKey<Biome> SPAWNS_WARM_COD = modBiomeTag("spawns_warm_cod");
    public static final TagKey<Biome> SPAWNS_COLD_COD = modBiomeTag("spawns_cold_cod");

    public static final TagKey<Biome> SPAWNS_OCEAN_SALMON = modBiomeTag("spawns_ocean_salmon");
    public static final TagKey<Biome> SPAWNS_COLD_RIVER_SALMON = modBiomeTag("spawns_cold_river_salmon");
    public static final TagKey<Biome> SPAWNS_COLD_OCEAN_SALMON = modBiomeTag("spawns_cold_ocean_salmon");

    public static final TagKey<Biome> SPAWNS_COLD_SQUID = modBiomeTag("spawns_cold_squid");

    public static final TagKey<Biome> HAS_SQUILL = modBiomeTag("has_mob/squill");
    public static final TagKey<Biome> HAS_BAITFISH = modBiomeTag("has_mob/baitfish");
    public static final TagKey<Biome> HAS_FLYING_FISH = modBiomeTag("has_mob/flying_fish");
    public static final TagKey<Biome> HAS_CAMELS = modBiomeTag("has_mob/camel");

    private static TagKey<Biome> modBiomeTag(String name) {
        return biomeTag(ShiftedLens.MOD_ID, name);
    }

    private static TagKey<Biome> forgeBiomeTag(String name) {
        return biomeTag("forge", name);
    }

    public static TagKey<Biome> biomeTag(String modid, String name) {
        return TagKey.create(Registries.BIOME, new ResourceLocation(modid, name));
    }
}
