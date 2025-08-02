package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SquillModel;
import com.platypushasnohat.shifted_lens.entities.Squill;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SquillRenderer extends MobRenderer<Squill, SquillModel<Squill>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squill.png");

    public SquillRenderer(EntityRendererProvider.Context context) {
        super(context, new SquillModel<>(context.bakeLayer(SLModelLayers.SQUILL)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Squill entity) {
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(Squill entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutout(getTextureLocation(entity));
    }
}
