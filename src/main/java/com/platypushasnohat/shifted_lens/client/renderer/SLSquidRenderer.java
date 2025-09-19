package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSquidModel;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Squid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLSquidRenderer extends MobRenderer<Squid, SLSquidModel<Squid>> {

    private static final ResourceLocation SQUID = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/squid.png");
    private static final ResourceLocation COLD_SQUID = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/cold_squid.png");

    public SLSquidRenderer(EntityRendererProvider.Context context) {
        super(context, new SLSquidModel<>(context.bakeLayer(SLModelLayers.SQUID)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(Squid entity) {
        int variant = ((VariantAccess) entity).getVariant();
        return variant == 1 ? COLD_SQUID : SQUID;
    }

    @Override
    protected void setupRotations(Squid entity, PoseStack poseStack, float i, float g, float partialTicks) {
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
}
