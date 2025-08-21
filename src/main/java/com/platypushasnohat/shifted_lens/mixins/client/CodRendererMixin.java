package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLCodModel;
import com.platypushasnohat.shifted_lens.config.SLConfig;
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
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(CodRenderer.class)
public abstract class CodRendererMixin extends MobRenderer<Cod, SLCodModel<Cod>> implements VariantAccess {

    @Shadow
    private static final ResourceLocation COD_LOCATION = new ResourceLocation("textures/entity/fish/cod.png");

    @Unique
    private static final ResourceLocation COD_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/cod/cod.png");
    @Unique
    private static final ResourceLocation COLD_COD_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/cod/cold_cod.png");
    @Unique
    private static final ResourceLocation WARM_COD_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/cod/warm_cod.png");

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
    public void render(@NotNull Cod fish, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        if (SLConfig.REPLACE_COD.get()) this.model = this.shiftedLens$remodel;
        super.render(fish, f, g, poseStack, multiBufferSource, i);
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Cod;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(Cod cod, CallbackInfoReturnable<ResourceLocation> cir) {
        int variant = ((VariantAccess) cod).getVariant();
        ResourceLocation texture = COD_TEXTURE;
        if (variant == 1) {
            texture = COLD_COD_TEXTURE;
        }
        if (variant == 2) {
            texture = WARM_COD_TEXTURE;
        }
        cir.setReturnValue(SLConfig.REPLACE_COD.get() ? texture : COD_LOCATION);
    }

    @Override
    protected void setupRotations(@NotNull Cod fish, @NotNull PoseStack poseStack, float i, float g, float h) {
        super.setupRotations(fish, poseStack, i, g, h);
        if (SLConfig.REPLACE_COD.get()) {
            poseStack.scale(0.75F, 0.75F, 0.75F);
        } else {
            float f = 4.3F * Mth.sin(0.6F * i);
            poseStack.mulPose(Axis.YP.rotationDegrees(f));
        }
        if (!fish.isInWater()) {
            poseStack.translate(0.1F, 0.1F, -0.1F);
            poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
