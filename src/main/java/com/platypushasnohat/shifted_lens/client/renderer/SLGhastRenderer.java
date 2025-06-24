package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.GhastGlowLayer;
import com.platypushasnohat.shifted_lens.entities.SLGhast;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SLGhastRenderer extends MobRenderer<SLGhast, SLGhastModel<SLGhast>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/ghast.png");
    private static final ResourceLocation RETRO_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/retro_ghast.png");

    public SLGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new SLGhastModel<>(context.bakeLayer(SLModelLayers.GHAST_LAYER)), 1.5F);
        this.addLayer(new GhastGlowLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(SLGhast entity) {
        if (entity.getName().getString().equalsIgnoreCase("retro")) return RETRO_TEXTURE;
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(SLGhast entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (entity.getName().getString().equalsIgnoreCase("retro")) return RenderType.entityCutoutNoCull(RETRO_TEXTURE);
        return RenderType.entityCutoutNoCull(TEXTURE);
    }
}
