package com.platypushasnohat.shifted_lens.registry.tags;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public class SLDamageTypeTags {

    public static final TagKey<DamageType> CAMEL_IMMUNE_TO = damageTypeTag("camel_immune_to");

    public static TagKey<DamageType> damageTypeTag(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(ShiftedLens.MOD_ID, name));
    }
}
