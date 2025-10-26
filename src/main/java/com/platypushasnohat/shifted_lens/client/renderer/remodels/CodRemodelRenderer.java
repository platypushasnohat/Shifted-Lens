package com.platypushasnohat.shifted_lens.client.renderer.remodels;

import com.platypushasnohat.shifted_lens.client.models.SLCodModel;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.platypushasnohat.shifted_lens.ShiftedLens.modPrefix;

@OnlyIn(Dist.CLIENT)
public class CodRemodelRenderer extends MobRenderer<Cod, SLCodModel> {

    private static final ResourceLocation COD = modPrefix("textures/entity/cod/cod.png");
    private static final ResourceLocation COLD_COD = modPrefix("textures/entity/cod/cold_cod.png");
    private static final ResourceLocation WARM_COD = modPrefix("textures/entity/cod/warm_cod.png");

    public CodRemodelRenderer(EntityRendererProvider.Context context) {
        super(context, new SLCodModel(context.bakeLayer(SLModelLayers.COD)), 0.3F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Cod entity) {
        int variant = ((VariantAccess) entity).shiftedLens$getVariant();
        ResourceLocation texture = COD;
        if (variant == 1) {
            texture = COLD_COD;
        }
        if (variant == 2) {
            texture = WARM_COD;
        }
        return texture;
    }
}
