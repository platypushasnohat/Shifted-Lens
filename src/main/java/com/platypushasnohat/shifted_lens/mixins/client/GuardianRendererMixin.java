package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGuardianModel;
import com.platypushasnohat.shifted_lens.client.renderer.GuardianBeamRenderer;
import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
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
@Mixin(GuardianRenderer.class)
public abstract class GuardianRendererMixin extends MobRenderer<Guardian, SLGuardianModel<Guardian>> {

    @Shadow
    private static final ResourceLocation GUARDIAN_LOCATION = new ResourceLocation("textures/entity/guardian.png");

    @Unique
    private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/guardian.png");

    @Unique
    private SLGuardianModel<Guardian> shiftedLens$remodel;

    public GuardianRendererMixin(EntityRendererProvider.Context context, SLGuardianModel<Guardian> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;FLnet/minecraft/client/model/geom/ModelLayerLocation;)V", at = @At("TAIL"))
    private void GuardianRenderer(EntityRendererProvider.Context context, float p_174162_, ModelLayerLocation layerLocation, CallbackInfo ci) {
        this.shiftedLens$remodel = new SLGuardianModel<>(context.bakeLayer(SLModelLayers.GUARDIAN));
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/monster/Guardian;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    public void render(Guardian guardian, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        ci.cancel();
        this.model = this.shiftedLens$remodel;
        super.render(guardian, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

        LivingEntity target = guardian.getActiveAttackTarget();
        if (target != null) {
            GuardianBeamRenderer.render(guardian, target, poseStack, bufferSource, partialTicks);
        }
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Guardian;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(Guardian guardian, CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(GUARDIAN_TEXTURE);
    }
}
