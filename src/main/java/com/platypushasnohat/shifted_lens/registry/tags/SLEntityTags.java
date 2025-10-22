package com.platypushasnohat.shifted_lens.registry.tags;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class SLEntityTags {

    public static final TagKey<EntityType<?>> GUARDIAN_TARGETS = modEntityTag("guardian_targets");
    public static final TagKey<EntityType<?>> GHAST_TARGETS = modEntityTag("ghast_targets");
    public static final TagKey<EntityType<?>> RABBIT_AVOIDS = modEntityTag("rabbit_avoids");
    public static final TagKey<EntityType<?>> KILLER_RABBIT_TARGETS = modEntityTag("killer_rabbit_targets");

    private static TagKey<EntityType<?>> modEntityTag(String name) {
        return entityTag(ShiftedLens.MOD_ID, name);
    }

    private static TagKey<EntityType<?>> forgeEntityTag(String name) {
        return entityTag("forge", name);
    }

    public static TagKey<EntityType<?>> entityTag(String modid, String name) {
        return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(modid, name));
    }
}
