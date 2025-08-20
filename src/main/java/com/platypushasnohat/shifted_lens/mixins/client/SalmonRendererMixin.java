package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
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
public abstract class SalmonRendererMixin extends MobRenderer<Salmon, SLSalmonModel<Salmon>> implements VariantAccess {

    @Shadow
    private static final ResourceLocation SALMON_LOCATION = new ResourceLocation("textures/entity/fish/salmon.png");

    @Unique
    private static final ResourceLocation RIVER_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/river_salmon.png");
    @Unique
    private static final ResourceLocation OCEAN_SALMON = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/salmon/ocean_salmon.png");

    @Unique
    private SLSalmonModel<Salmon> shiftedLens$remodel;

    public SalmonRendererMixin(EntityRendererProvider.Context context, SLSalmonModel<Salmon> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
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
        int variant = ((VariantAccess) salmon).getVariant();
        if (SLCommonConfig.REPLACE_SALMON.get()) {
            if (variant == 1) return OCEAN_SALMON;
            else return RIVER_SALMON;
        } else {
            return SALMON_LOCATION;
        }
    }

    @Override
    protected void setupRotations(Salmon fish, PoseStack poseStack, float i, float g, float h) {
        if (SLCommonConfig.REPLACE_SALMON.get()) {
            super.setupRotations(fish, poseStack, i, g, h);
            poseStack.scale(0.75F, 0.75F, 0.75F);

            if (!fish.isInWater()) {
                poseStack.translate(0.2F, 0.1F, -0.1F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        } else {
            super.setupRotations(fish, poseStack, i, g, h);
            float f = 1.0F;
            float f1 = 1.0F;
            if (!fish.isInWater()) {
                f = 1.3F;
                f1 = 1.7F;
            }

            float f2 = f * 4.3F * Mth.sin(f1 * 0.6F * i);
            poseStack.mulPose(Axis.YP.rotationDegrees(f2));
            poseStack.translate(0.0F, 0.0F, -0.4F);
            if (!fish.isInWater()) {
                poseStack.translate(0.2F, 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
    }
}
