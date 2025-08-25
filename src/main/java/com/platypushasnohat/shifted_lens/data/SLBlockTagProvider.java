package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.tags.SLBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

import static com.platypushasnohat.shifted_lens.registry.SLBlocks.*;

public class SLBlockTagProvider extends BlockTagsProvider {

    public SLBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, provider, ShiftedLens.MOD_ID, helper);
    }

    @Override
    protected void addTags(@NotNull HolderLookup.Provider provider) {
        this.tag(SLBlockTags.CAMEL_SPAWNABLE_ON).addTag(Tags.Blocks.SAND);

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                WHIRLIBOX.get()
        );
    }
}