//package com.platypushasnohat.shifted_lens.mixins.client;
//
//import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
//import net.minecraft.client.model.HierarchicalModel;
//import net.minecraft.client.model.SalmonModel;
//import net.minecraft.client.model.geom.builders.*;
//import net.minecraft.world.entity.Entity;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.*;
//
//@OnlyIn(Dist.CLIENT)
//@Mixin(SalmonModel.class)
//public abstract class SalmonModelMixin<T extends Entity> extends HierarchicalModel<T> {
//
//    @Inject(method = "createBodyLayer", at = @At("RETURN"), cancellable = true)
//    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> callbackInfo) {
//        callbackInfo.setReturnValue(SLSalmonModel.createBodyLayer());
//    }
//
//    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"))
//    private void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo callbackInfo) {
//    }
//}
