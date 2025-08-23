package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.config.SLConfig;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(TropicalFishRenderer.class)
public abstract class TropicalFishRendererMixin extends MobRenderer<TropicalFish, ColorableHierarchicalModel<TropicalFish>> {

    public TropicalFishRendererMixin(EntityRendererProvider.Context context, ColorableHierarchicalModel<TropicalFish> model, float f) {
        super(context, model, f);
    }

    @Inject(method = "setupRotations(Lnet/minecraft/world/entity/animal/TropicalFish;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V", at = @At("HEAD"), cancellable = true)
    public void setupRotations(TropicalFish fish, PoseStack poseStack, float p_116228_, float p_116229_, float p_116230_, CallbackInfo ci) {
        ci.cancel();
        if (SLConfig.BETTER_FISH_FLOPPING.get()) {
            super.setupRotations(fish, poseStack, p_116228_, p_116229_, p_116230_);
        } else {
            super.setupRotations(fish, poseStack, p_116228_, p_116229_, p_116230_);
            float f = 4.3F * Mth.sin(0.6F * p_116228_);
            poseStack.mulPose(Axis.YP.rotationDegrees(f));
            if (!fish.isInWater()) {
                poseStack.translate(0.2F, 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
    }
}
