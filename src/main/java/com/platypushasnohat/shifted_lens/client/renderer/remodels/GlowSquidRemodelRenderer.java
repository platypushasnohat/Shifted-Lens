package com.platypushasnohat.shifted_lens.client.renderer.remodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.client.models.SLSquidModel;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.GlowSquid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GlowSquidRemodelRenderer extends MobRenderer<GlowSquid, SLSquidModel<GlowSquid>> {

    private static final ResourceLocation GLOW_SQUID = new ResourceLocation("textures/entity/squid/glow_squid.png");

    public GlowSquidRemodelRenderer(EntityRendererProvider.Context context) {
        super(context, new SLSquidModel<>(context.bakeLayer(SLModelLayers.GLOW_SQUID)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(GlowSquid entity) {
        return GLOW_SQUID;
    }

    @Override
    protected void setupRotations(GlowSquid entity, PoseStack poseStack, float i, float g, float partialTicks) {
        if (isEntityUpsideDown(entity)) {
            poseStack.translate(0.0D, entity.getBbHeight() + 0.1F, 0.0D);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
        float translateY = entity.getBbHeight() * 0.5F;
        poseStack.translate(0.0F, translateY, 0.0F);
        poseStack.mulPose(Axis.YP.rotationDegrees(360.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())));
        poseStack.mulPose(Axis.XP.rotationDegrees((Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F) % 360.0F));
        poseStack.translate(0.0F, -translateY, 0.0F);
    }

    @Override
    protected float getBob(GlowSquid entity, float f) {
        return super.getBob(entity, f);
    }
}
