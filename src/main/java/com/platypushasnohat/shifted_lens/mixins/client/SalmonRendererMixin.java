package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
import com.platypushasnohat.shifted_lens.config.SLConfig;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    public void render(Salmon fish, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (SLConfig.REPLACE_SALMON.get()) this.model = this.shiftedLens$remodel;
        super.render(fish, f, g, poseStack, multiBufferSource, i);
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Salmon;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(Salmon fish, CallbackInfoReturnable<ResourceLocation> cir) {
        int variant = ((VariantAccess) fish).getVariant();
        ResourceLocation texture = variant == 1 ? OCEAN_SALMON : RIVER_SALMON;
        cir.setReturnValue(SLConfig.REPLACE_SALMON.get() ? texture : SALMON_LOCATION);
    }

    @Override
    protected void setupRotations(Salmon fish, PoseStack poseStack, float i, float g, float h) {
        if (SLConfig.REPLACE_SALMON.get()) {
            super.setupRotations(fish, poseStack, i, g, h);
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
