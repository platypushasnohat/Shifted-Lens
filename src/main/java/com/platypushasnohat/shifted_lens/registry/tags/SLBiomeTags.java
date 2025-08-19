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
