package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SalmonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(SalmonRenderer.class)
public abstract class SalmonRendererMixin extends MobRenderer<Salmon, SLSalmonModel<Salmon>> {

    @Shadow
    private static final ResourceLocation SALMON_LOCATION = new ResourceLocation("textures/entity/fish/salmon.png");

    @Unique
    private static final ResourceLocation RIVER_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/river_salmon.png");

    @Unique
    private SLSalmonModel<Salmon> shiftedLens$remodel;

    public SalmonRendererMixin(EntityRendererProvider.Context context, SLSalmonModel<Salmon> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;)V", at = @At("TAIL"))
    private void SalmonRenderer(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        this.shiftedLens$remodel = new SLSalmonModel<>(context.bakeLayer(SLModelLayers.SALMON));
    }

    @Override
    public void render(Salmon salmon, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (SLCommonConfig.REPLACE_SALMON.get()) this.model = this.shiftedLens$remodel;
        super.render(salmon, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Salmon salmon) {
        if (SLCommonConfig.REPLACE_SALMON.get()) {
            return RIVER_SALMON;
        } else {
            return SALMON_LOCATION;
        }
    }

    @Override
    protected void setupRotations(Salmon salmon, PoseStack poseStack, float i, float g, float h) {
        if (SLCommonConfig.REPLACE_SALMON.get()) {
            super.setupRotations(salmon, poseStack, i, g, h);
        } else {
            super.setupRotations(salmon, poseStack, i, g, h);
            float f = 1.0F;
            float f1 = 1.0F;
            if (!salmon.isInWater()) {
                f = 1.3F;
                f1 = 1.7F;
            }

            float f2 = f * 4.3F * Mth.sin(f1 * 0.6F * i);
            poseStack.mulPose(Axis.YP.rotationDegrees(f2));
            poseStack.translate(0.0F, 0.0F, -0.4F);
            if (!salmon.isInWater()) {
                poseStack.translate(0.2F, 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
    }
}
