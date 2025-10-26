package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.GlowSquidRemodelRenderer;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.GlowSquid;
import org.jetbrains.annotations.NotNull;

public class SLGlowSquidRenderer extends EntityRenderer<GlowSquid> {

    private final GlowSquidRenderer vanilla;
    private final GlowSquidRemodelRenderer remodel;

    public SLGlowSquidRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new GlowSquidRenderer(context, new SquidModel<>(context.bakeLayer(ModelLayers.GLOW_SQUID)));
        this.remodel = new GlowSquidRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull GlowSquid glowSquid, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.GLOW_SQUID_REMODEL.get()) {
            this.remodel.render(glowSquid, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(glowSquid, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GlowSquid glowSquid) {
        return ShiftedLensConfig.GLOW_SQUID_REMODEL.get() ? this.remodel.getTextureLocation(glowSquid) : this.vanilla.getTextureLocation(glowSquid);
    }
}
