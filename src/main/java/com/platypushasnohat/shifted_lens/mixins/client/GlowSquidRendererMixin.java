package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.client.models.SLSquidModel;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GlowSquidRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.animal.Squid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(GlowSquidRenderer.class)
public abstract class GlowSquidRendererMixin extends MobRenderer<Squid, SLSquidModel> implements VariantAccess {

    @Unique
    private SLSquidModel shiftedLens$remodel;

    public GlowSquidRendererMixin(EntityRendererProvider.Context context, SLSquidModel model, float f) {
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
}
