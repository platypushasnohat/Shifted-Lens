//package com.platypushasnohat.shifted_lens.mixins.client;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.platypushasnohat.shifted_lens.client.models.SLSalmonModel;
//import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
//import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
//import net.minecraft.client.model.SalmonModel;
//import net.minecraft.client.renderer.MultiBufferSource;
//import net.minecraft.client.renderer.entity.EntityRendererProvider;
//import net.minecraft.client.renderer.entity.MobRenderer;
//import net.minecraft.client.renderer.entity.SalmonRenderer;
//import net.minecraft.world.entity.animal.Salmon;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.api.distmarker.OnlyIn;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.Inject;
//import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//
//@OnlyIn(Dist.CLIENT)
//@Mixin(SalmonRenderer.class)
//public abstract class SalmonRendererMixin extends MobRenderer<Salmon, SalmonModel<Salmon>> {
//
//    private SLSalmonModel remodel;
//
//    public SalmonRendererMixin(EntityRendererProvider.Context context, SalmonModel entityModel, float f) {
//        super(context, entityModel, f);
//    }
//
//    @Inject(method = "<init>", at = @At("TAIL"))
//    private void init(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
//        this.remodel = new SLSalmonModel(context.bakeLayer(SLModelLayers.SALMON));
//    }
//
//    @Override
//    public void render(Salmon mob, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
//        if (SLCommonConfig.REPLACE_SALMON.get()) this.model = this.remodel;
//        super.render(mob, f, g, poseStack, multiBufferSource, i);
//    }
//}
