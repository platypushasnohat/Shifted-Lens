package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.SLPaintings;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.PaintingVariantTagsProvider;
import net.minecraft.tags.PaintingVariantTags;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class SLPaintingTagProvider extends PaintingVariantTagsProvider {

    public SLPaintingTagProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider, ExistingFileHelper helper) {
        super(packOutput, lookupProvider, ShiftedLens.MOD_ID, helper);
    }

    @Override
    protected void addTags(@NotNull Provider provider) {
        TagAppender<PaintingVariant> appender = this.tag(PaintingVariantTags.PLACEABLE);
        for (RegistryObject<PaintingVariant> variant : SLPaintings.PAINTING_VARIANTS.getEntries()) {
            appender.add(variant.getKey());
        }
    }
}
