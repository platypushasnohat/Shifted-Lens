package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLModelLayers {

    public static final ModelLayerLocation GHAST = main("ghast");

    public static final ModelLayerLocation GUARDIAN = main("guardian");
    public static final ModelLayerLocation ELDER_GUARDIAN = main("elder_guardian");

    public static final ModelLayerLocation STEEL_CHARIOT = main("steel_chariot");

    private static ModelLayerLocation register(String id, String name) {
        return new ModelLayerLocation(new ResourceLocation(ShiftedLens.MOD_ID, id), name);
    }

    private static ModelLayerLocation main(String id) {
        return register(id, "main");
    }
}
