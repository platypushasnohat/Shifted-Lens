package com.platypushasnohat.shifted_lens.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLGhastGlowLayer<T extends Ghast, M extends SLGhastModel<T>> extends RenderLayer<T, M> {

    private static final RenderType GLOW_TEXTURE = RenderType.entityTranslucentEmissive(new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/ghast_glow.png"));
    private static final RenderType RETRO_GLOW_TEXTURE = RenderType.entityTranslucentEmissive(new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/retro_ghast_glow.png"));

    public SLGhastGlowLayer(RenderLayerParent<T, M> parent) {
        super(parent);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, T entity, float f, float g, float h, float j, float k, float l) {
        VertexConsumer vertexConsumer = multiBufferSource.getBuffer(entity.getName().getString().equalsIgnoreCase("retro") ? RETRO_GLOW_TEXTURE : GLOW_TEXTURE);
        this.getParentModel().renderToBuffer(poseStack, vertexConsumer, 0xF00000, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.75F);
    }
}


