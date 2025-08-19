package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLCodModel;
import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CodRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Cod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(CodRenderer.class)
public abstract class CodRendererMixin extends MobRenderer<Cod, SLCodModel<Cod>> implements VariantAccess {

    @Shadow
    private static final ResourceLocation COD_LOCATION = new ResourceLocation("textures/entity/fish/cod.png");

    @Unique
    private static final ResourceLocation COD = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/cod/cod.png");
    @Unique
    private static final ResourceLocation COLD_COD = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/cod/cold_cod.png");
    @Unique
    private static final ResourceLocation WARM_COD = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/cod/warm_cod.png");

    @Unique
    private SLCodModel<Cod> shiftedLens$remodel;

    public CodRendererMixin(EntityRendererProvider.Context context, SLCodModel<Cod> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void CodRenderer(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        this.shiftedLens$remodel = new SLCodModel<>(context.bakeLayer(SLModelLayers.COD));
    }

    @Override
    public void render(Cod fish, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (SLCommonConfig.REPLACE_COD.get()) this.model = this.shiftedLens$remodel;
        else super.render(fish, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Cod fish) {
        int variant = ((VariantAccess) fish).getVariant();
        if (SLCommonConfig.REPLACE_COD.get()) {
            if (variant == 1) return WARM_COD;
            else if (variant == 2) return COLD_COD;
            else return COD;
        } else {
            return COD_LOCATION;
        }
    }

    @Override
    protected void setupRotations(Cod fish, PoseStack poseStack, float i, float g, float h) {
        if (SLCommonConfig.REPLACE_COD.get()) {
            super.setupRotations(fish, poseStack, i, g, h);
            poseStack.scale(0.75F, 0.75F, 0.75F);

            if (!fish.isInWater()) {
                poseStack.translate(0.2F, 0.1F, 0);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        } else {
            super.setupRotations(fish, poseStack, i, g, h);
            float f = 4.3F * Mth.sin(0.6F * i);
            poseStack.mulPose(Axis.YP.rotationDegrees(f));
            if (!fish.isInWater()) {
                poseStack.translate(0.1F, 0.1F, -0.1F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
    }
}
