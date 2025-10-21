package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.models.SLElderGuardianModel;
import com.platypushasnohat.shifted_lens.client.renderer.SLElderGuardianRenderer;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.MobAppearanceParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobAppearanceParticle.class)
public abstract class MobAppearanceParticleMixin extends Particle {

    @Mutable
    @Shadow
    @Final
    private Model model;

    @Mutable
    @Shadow
    @Final
    private RenderType renderType;

    protected MobAppearanceParticleMixin(ClientLevel level, double x, double y, double z) {
        super(level, x, y, z);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(ClientLevel level, double x, double y, double z, CallbackInfo ci) {
        this.model = new SLElderGuardianModel(Minecraft.getInstance().getEntityModels().bakeLayer(SLModelLayers.ELDER_GUARDIAN));
        this.renderType = RenderType.entityTranslucent(ShiftedLensConfig.STAINLESS_GUARDIANS.get() ? SLElderGuardianRenderer.STAINLESS_ELDER_GUARDIAN : SLElderGuardianRenderer.ELDER_GUARDIAN);
    }

    @Inject(method = "render(Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/client/Camera;F)V", at = @At("TAIL"), cancellable = true)
    public void render(VertexConsumer consumer, Camera camera, float partialTicks, CallbackInfo ci) {
        ci.cancel();
        float f = ((float)this.age + partialTicks) / (float) this.lifetime;
        float f1 = 0.05F + 0.5F * Mth.sin(f * (float) Math.PI);
        PoseStack posestack = new PoseStack();
        posestack.mulPose(camera.rotation());
        posestack.mulPose(Axis.XP.rotationDegrees(150.0F * f - 60.0F));
        posestack.scale(-1.0F, -1.0F, 1.0F);
        posestack.translate(0.0F, -1.101F, 1.5F);
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(this.renderType);
        this.model.renderToBuffer(posestack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, f1);
        bufferSource.endBatch();
    }
}
