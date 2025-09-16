package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.SLGhastGlowLayer;
import com.platypushasnohat.shifted_lens.config.SLClientConfig;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
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
@Mixin(GhastRenderer.class)
public abstract class GhastRendererMixin extends MobRenderer<Ghast, SLGhastModel> {

    @Shadow
    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("textures/entity/ghast/ghast.png");
    @Shadow
    private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    @Unique
    private static final ResourceLocation GHAST_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/ghast.png");
    @Unique
    private static final ResourceLocation RETRO_GHAST_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/retro_ghast.png");

    @Unique
    private SLGhastModel shiftedLens$remodel;

    public GhastRendererMixin(EntityRendererProvider.Context context, SLGhastModel entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void GhastRenderer(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        this.shiftedLens$remodel = new SLGhastModel(context.bakeLayer(SLModelLayers.GHAST));
        this.addLayer(new SLGhastGlowLayer(this));
    }

    @Override
    public void render(Ghast ghast, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int i) {
        this.model = this.shiftedLens$remodel;
        super.render(ghast, f, g, poseStack, multiBufferSource, i);
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Ghast;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(Ghast ghast, CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(SLClientConfig.RETRO_GHAST.get() ? RETRO_GHAST_TEXTURE : GHAST_TEXTURE);
    }

    @Inject(method = "scale(Lnet/minecraft/world/entity/monster/Ghast;Lcom/mojang/blaze3d/vertex/PoseStack;F)V", at = @At("HEAD"), cancellable = true)
    protected void scale(Ghast ghast, PoseStack matrixStack, float partialTicks, CallbackInfo ci) {
        ci.cancel();
    }
}
