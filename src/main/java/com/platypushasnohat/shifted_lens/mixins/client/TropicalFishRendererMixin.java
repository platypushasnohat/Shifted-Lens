package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
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
        if (ShiftedLensConfig.TROPICAL_FISH_REVAMP.get()) {
            ci.cancel();
            super.setupRotations(fish, poseStack, p_116228_, p_116229_, p_116230_);
        }
    }
}
