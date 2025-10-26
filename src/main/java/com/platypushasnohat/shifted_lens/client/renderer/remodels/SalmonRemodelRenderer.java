package com.platypushasnohat.shifted_lens.client.renderer.remodels;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SalmonRemodelRenderer extends MobRenderer<Salmon, SLSalmonModel> {

    private static final ResourceLocation RIVER_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/river_salmon.png");
    private static final ResourceLocation OCEAN_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/ocean_salmon.png");
    private static final ResourceLocation COLD_RIVER_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/cold_river_salmon.png");
    private static final ResourceLocation COLD_OCEAN_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/cold_ocean_salmon.png");

    public SalmonRemodelRenderer(EntityRendererProvider.Context context) {
        super(context, new SLSalmonModel(context.bakeLayer(SLModelLayers.SALMON)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Salmon entity) {
        int variant = ((VariantAccess) entity).shiftedLens$getVariant();
        ResourceLocation texture = RIVER_SALMON;
        if (variant == 1) {
            texture = OCEAN_SALMON;
        }
        if (variant == 2) {
            texture = COLD_RIVER_SALMON;
        }
        if (variant == 3) {
            texture = COLD_OCEAN_SALMON;
        }
        return texture;
    }
}
