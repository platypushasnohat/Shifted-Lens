package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.SLGhastGlowLayer;
import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(GhastRenderer.class)
public abstract class GhastRendererMixin extends MobRenderer<Ghast, SLGhastModel<Ghast>> {

    @Shadow
    private static final ResourceLocation GHAST_LOCATION = new ResourceLocation("textures/entity/ghast/ghast.png");
    @Shadow
    private static final ResourceLocation GHAST_SHOOTING_LOCATION = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

    @Unique
    private static final ResourceLocation GHAST_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/ghast.png");
    @Unique
    private static final ResourceLocation RETRO_GHAST_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/ghast/retro_ghast.png");

    @Unique
    private SLGhastModel<Ghast> shiftedLens$remodel;

    public GhastRendererMixin(EntityRendererProvider.Context context, SLGhastModel<Ghast> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void GhastRenderer(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        this.shiftedLens$remodel = new SLGhastModel<>(context.bakeLayer(SLModelLayers.GHAST));
        this.addLayer(new SLGhastGlowLayer<>(this));
    }

    @Override
    public void render(Ghast ghast, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        if (SLConfig.REPLACE_GHAST.get()) this.model = this.shiftedLens$remodel;
        super.render(ghast, f, g, poseStack, multiBufferSource, i);
    }

    @Override
    public ResourceLocation getTextureLocation(Ghast ghast) {
        if (SLConfig.REPLACE_GHAST.get()) return GHAST_TEXTURE;
        else return ghast.isCharging() ? GHAST_SHOOTING_LOCATION : GHAST_LOCATION;
    }

    @Override
    protected void scale(Ghast ghast, PoseStack poseStack, float f) {
        if (SLConfig.REPLACE_GHAST.get()) {
            poseStack.scale(1.0F, 1.0F, 1.0F);
        } else {
            poseStack.scale(4.5F, 4.5F, 4.5F);
        }
    }
}
