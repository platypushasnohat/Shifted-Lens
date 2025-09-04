package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLModelLayers {

    public static final ModelLayerLocation BAITFISH = main("baitfish");
    public static final ModelLayerLocation COD = main("cod");
    public static final ModelLayerLocation FLYING_FISH = main("flying_fish");
    public static final ModelLayerLocation GHAST = main("ghast");
    public static final ModelLayerLocation GUARDIAN = main("guardian");
    public static final ModelLayerLocation SALMON = main("salmon");
    public static final ModelLayerLocation SQUID = main("squid");
    public static final ModelLayerLocation SQUILL = main("squill");

    public static final ModelLayerLocation WHIRLICAP = main("whirlicap");
    public static final ModelLayerLocation WHIRLIGIG = main("whirligig");

    private static ModelLayerLocation register(String id, String name) {
        return new ModelLayerLocation(new ResourceLocation(ShiftedLens.MOD_ID, id), name);
    }

    private static ModelLayerLocation main(String id) {
        return register(id, "main");
    }
}
