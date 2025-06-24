package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.ShiftedGhastModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.GhastGlowLayer;
import com.platypushasnohat.shifted_lens.entities.ShiftedGhast;
import com.platypushasnohat.shifted_lens.registry.ShiftedModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ShiftedGhastRenderer extends MobRenderer<ShiftedGhast, ShiftedGhastModel<ShiftedGhast>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast.png");
    private static final ResourceLocation GLOW_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast_glow.png");

    public ShiftedGhastRenderer(EntityRendererProvider.Context context) {
        super(context, new ShiftedGhastModel<>(context.bakeLayer(ShiftedModelLayers.GHAST_LAYER)), 1.5F);
        this.addLayer(new GhastGlowLayer<>(this, GLOW_TEXTURE, (entity, p_234802_, p_234803_) -> 0.75F, ShiftedGhastModel::getGlowingLayerModelParts));
    }

    @Override
    public ResourceLocation getTextureLocation(ShiftedGhast entity) {
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(ShiftedGhast entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutoutNoCull(TEXTURE);
    }
}
