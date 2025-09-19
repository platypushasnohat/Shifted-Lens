package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.SLGhastGlowLayer;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLGhastRenderer extends MobRenderer<Ghast, SLGhastModel> {

    private static final ResourceLocation GHAST = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/ghast.png");
    private static final ResourceLocation RETRO_GHAST = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/retro_ghast.png");

    public SLGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new SLGhastModel(context.bakeLayer(SLModelLayers.GHAST)), 1.5F);
        this.addLayer(new SLGhastGlowLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Ghast entity) {
        return ShiftedLensConfig.RETRO_GHAST.get() ? RETRO_GHAST : GHAST;
    }
}
