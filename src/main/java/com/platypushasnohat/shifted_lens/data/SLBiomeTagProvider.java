package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.platypushasnohat.shifted_lens.registry.tags.SLBiomeTags.*;

public class SLBiomeTagProvider extends BiomeTagsProvider {

    public SLBiomeTagProvider(PackOutput output, CompletableFuture<Provider> provider, @Nullable ExistingFileHelper helper) {
        super(output, provider, ShiftedLens.MOD_ID, helper);
    }

    @Override
    public void addTags(@NotNull Provider provider) {

        this.tag(HAS_CAMELS).addTag(Tags.Biomes.IS_DESERT);

        this.tag(SPAWNS_WARM_COD).add(
                Biomes.LUKEWARM_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN,
                Biomes.WARM_OCEAN
        );

        this.tag(SPAWNS_COLD_COD).add(
                Biomes.COLD_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.FROZEN_OCEAN,
                Biomes.DEEP_FROZEN_OCEAN,
                Biomes.FROZEN_RIVER
        );

        this.tag(SPAWNS_OCEAN_SALMON).add(
                Biomes.LUKEWARM_OCEAN,
                Biomes.DEEP_LUKEWARM_OCEAN,
                Biomes.WARM_OCEAN,
                Biomes.OCEAN,
                Biomes.DEEP_OCEAN
        );

        this.tag(SPAWNS_COLD_OCEAN_SALMON).add(
                Biomes.COLD_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.FROZEN_OCEAN,
                Biomes.DEEP_FROZEN_OCEAN
        );

        this.tag(SPAWNS_COLD_RIVER_SALMON).add(
                Biomes.FROZEN_RIVER
        );

        this.tag(SPAWNS_COLD_SQUID).add(
                Biomes.COLD_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.FROZEN_OCEAN,
                Biomes.DEEP_FROZEN_OCEAN,
                Biomes.FROZEN_RIVER
        );
    }
}
