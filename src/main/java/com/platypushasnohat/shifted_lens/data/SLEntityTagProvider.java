package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class SLEntityTagProvider extends EntityTypeTagsProvider {

    public SLEntityTagProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, provider, ShiftedLens.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        tag(SLEntityTags.GHAST_TARGETS);

        tag(SLEntityTags.GUARDIAN_TARGETS).add(
                SLEntities.SQUILL.get(),
                EntityType.SQUID,
                EntityType.GLOW_SQUID,
                EntityType.AXOLOTL
        );

        tag(SLEntityTags.SQUILL_TARGETS).add(
                EntityType.PHANTOM
        );

        tag(SLEntityTags.RABBIT_AVOIDS).add(
                EntityType.WOLF,
                EntityType.ZOMBIE,
                EntityType.CREEPER,
                EntityType.SKELETON,
                EntityType.SPIDER,
                EntityType.HUSK,
                EntityType.STRAY,
                EntityType.CAVE_SPIDER,
                EntityType.CREEPER,
                EntityType.ZOMBIE_VILLAGER,
                EntityType.ZOMBIFIED_PIGLIN,
                EntityType.PIGLIN,
                EntityType.PIGLIN_BRUTE,
                EntityType.SLIME,
                EntityType.MAGMA_CUBE,
                EntityType.BLAZE,
                EntityType.POLAR_BEAR,
                EntityType.FOX,
                EntityType.CAT,
                EntityType.OCELOT,
                EntityType.DROWNED,
                EntityType.GUARDIAN,
                EntityType.ELDER_GUARDIAN,
                EntityType.ENDERMAN,
                EntityType.EVOKER,
                EntityType.GHAST,
                EntityType.PHANTOM,
                EntityType.PILLAGER,
                EntityType.SHULKER,
                EntityType.VEX,
                EntityType.VILLAGER,
                EntityType.VINDICATOR,
                EntityType.WANDERING_TRADER,
                EntityType.WARDEN,
                EntityType.WITCH,
                EntityType.WITHER_SKELETON,
                EntityType.ZOGLIN
        );

        tag(SLEntityTags.KILLER_RABBIT_TARGETS).add(
                EntityType.WOLF,
                EntityType.FOX,
                EntityType.CAT,
                EntityType.OCELOT,
                EntityType.RAVAGER
        );
    }
}
