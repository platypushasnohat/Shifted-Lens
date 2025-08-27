package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

public final class SLPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, ShiftedLens.MOD_ID);
    public static Map<String, String> PAINTING_TRANSLATIONS = new HashMap<>();

    // Paintings
    public static final RegistryObject<PaintingVariant> SIGNATURE = painting("signature", "magmastrider", 16, 16);

    public static RegistryObject<PaintingVariant> painting(String name, String author, int width, int height) {
        PAINTING_TRANSLATIONS.put(name, author);
        return PAINTING_VARIANTS.register(name, () -> new PaintingVariant(width, height));
    }
}