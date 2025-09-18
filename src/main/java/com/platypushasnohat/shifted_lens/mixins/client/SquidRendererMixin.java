package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLSquidModel;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Squid;
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
@Mixin(SquidRenderer.class)
public abstract class SquidRendererMixin extends MobRenderer<Squid, SLSquidModel> implements VariantAccess {

    @Shadow
    private static final ResourceLocation SQUID_LOCATION = new ResourceLocation("textures/entity/squid/squid.png");

    @Unique
    private static final ResourceLocation SQUID_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/squid.png");
    @Unique
    private static final ResourceLocation COLD_SQUID_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/cold_squid.png");

    @Unique
    private SLSquidModel shiftedLens$remodel;

    public SquidRendererMixin(EntityRendererProvider.Context context, SLSquidModel model, float f) {
        super(context, model, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void SquidRenderer(EntityRendererProvider.Context context, SquidModel<Squid> model, CallbackInfo ci) {
        this.shiftedLens$remodel = new SLSquidModel(context.bakeLayer(SLModelLayers.SQUID));
    }

    @Override
    public void render(Squid entity, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        this.model = this.shiftedLens$remodel;
        super.render(entity, f, g, poseStack, multiBufferSource, i);
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Squid;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(Squid entity, CallbackInfoReturnable<ResourceLocation> cir) {
        int variant = ((VariantAccess) entity).getVariant();
        ResourceLocation texture = variant == 1 ? COLD_SQUID_TEXTURE : SQUID_TEXTURE;
        cir.setReturnValue(texture);
    }

    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/animal/Squid;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("HEAD"), cancellable = true)
    protected void setupRotations(Squid entity, PoseStack poseStack, float i, float g, float partialTicks, CallbackInfo ci) {
        ci.cancel();
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
