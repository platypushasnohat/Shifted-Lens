package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SteelChariotModel;
import com.platypushasnohat.shifted_lens.entities.SteelChariot;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class SteelChariotRenderer extends EntityRenderer<SteelChariot> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/steel_chariot/steel_chariot.png");
    protected final SteelChariotModel<SteelChariot> model;

    public SteelChariotRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SteelChariotModel<>(context.bakeLayer(SLModelLayers.STEEL_CHARIOT));
    }

    @Override
    public void render(SteelChariot vehicle, float vehicleYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(vehicle, vehicleYaw, partialTicks, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.translate(0.0D, 1.5F, 0.0D);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));

        float hurtTime = (float) vehicle.getHurtTime() - partialTicks;
        float damage = vehicle.getDamage() - partialTicks;
        if (damage < 0.0F) {
            damage = 0.0F;
        }
        if (hurtTime > 0.0F) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(hurtTime) * hurtTime * damage / 96.0F * (float) vehicle.getHurtDuration()));
        }

        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.model.renderType(this.getTextureLocation(vehicle)));
        this.model.renderToBuffer(poseStack, vertexconsumer, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    @Override
    public ResourceLocation getTextureLocation(SteelChariot vehicle) {
        return TEXTURE;
    }
}
