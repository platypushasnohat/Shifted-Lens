package com.platypushasnohat.shifted_lens.client.renderer.remodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
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
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SquidRemodelRenderer extends MobRenderer<Squid, SLSquidModel<Squid>> {

    private static final ResourceLocation SQUID = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/squid.png");
    private static final ResourceLocation COLD_SQUID = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/cold_squid.png");

    public SquidRemodelRenderer(EntityRendererProvider.Context context) {
        super(context, new SLSquidModel<>(context.bakeLayer(SLModelLayers.SQUID)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(@NotNull Squid entity) {
        int variant = ((VariantAccess) entity).shiftedLens$getVariant();
        return variant == 1 ? COLD_SQUID : SQUID;
    }

    @Override
    protected void setupRotations(@NotNull Squid entity, @NotNull PoseStack poseStack, float i, float g, float partialTicks) {
        if (ShiftedLensConfig.BETTER_SQUID_AI.get()) {
            if (isEntityUpsideDown(entity)) {
                poseStack.translate(0.0D, entity.getBbHeight() + 0.1F, 0.0D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
            float translateY = entity.getBbHeight() * 0.5F;
            poseStack.translate(0.0F, translateY, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(360.0F - Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot())));
            poseStack.mulPose(Axis.XP.rotationDegrees((Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot()) + 90.0F) % 360.0F));
            poseStack.translate(0.0F, -translateY, 0.0F);
        } else {
            float f = Mth.lerp(partialTicks, entity.xBodyRotO, entity.xBodyRot);
            float spin = Mth.lerp(partialTicks, entity.zBodyRotO, entity.zBodyRot);
            poseStack.translate(0.0F, 0.5F, 0.0F);
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - g));
            poseStack.mulPose(Axis.XP.rotationDegrees(f));
            poseStack.mulPose(Axis.YP.rotationDegrees(spin));
            poseStack.translate(0.0F, -1.2F, 0.0F);
        }
    }

    @Override
    protected float getBob(@NotNull Squid entity, float f) {
        if (!ShiftedLensConfig.BETTER_SQUID_AI.get()) {
            return Mth.lerp(f, entity.oldTentacleAngle, entity.tentacleAngle);
        }
        return super.getBob(entity, f);
    }
}
