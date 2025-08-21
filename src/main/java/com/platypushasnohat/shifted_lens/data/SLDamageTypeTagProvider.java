package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.tags.SLDamageTypeTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class SLDamageTypeTagProvider extends TagsProvider<DamageType> {

    public SLDamageTypeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, ExistingFileHelper helper) {
        super(output, Registries.DAMAGE_TYPE, provider, ShiftedLens.MOD_ID, helper);
    }

    protected void addTags(HolderLookup.Provider provider) {

        this.tag(SLDamageTypeTags.CAMEL_IMMUNE_TO).add(
                DamageTypes.CACTUS
        );

    }
}
