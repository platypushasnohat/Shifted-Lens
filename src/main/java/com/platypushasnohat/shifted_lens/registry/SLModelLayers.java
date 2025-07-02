package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLModelLayers {

    public static final ModelLayerLocation GHAST_LAYER = main("ghast");

    public static final ModelLayerLocation GUARDIAN_LAYER = main("guardian");
    public static final ModelLayerLocation ELDER_GUARDIAN_LAYER = main("elder_guardian");

    private static ModelLayerLocation register(String id, String name) {
        return new ModelLayerLocation(new ResourceLocation(ShiftedLens.MOD_ID, id), name);
    }

    private static ModelLayerLocation main(String id) {
        return register(id, "main");
    }
}
