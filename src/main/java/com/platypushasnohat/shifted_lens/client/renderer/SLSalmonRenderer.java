package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
import com.platypushasnohat.shifted_lens.entities.SLSalmon;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SLSalmonRenderer extends MobRenderer<SLSalmon, SLSalmonModel<SLSalmon>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/river_salmon.png");

    public SLSalmonRenderer(EntityRendererProvider.Context context) {
        super(context, new SLSalmonModel<>(context.bakeLayer(SLModelLayers.SALMON)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(SLSalmon entity) {
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(SLSalmon entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutoutNoCull(TEXTURE);
    }
}
