package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraft.world.entity.animal.Rabbit;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(RabbitRenderer.class)
public abstract class RabbitRendererMixin extends MobRenderer<Rabbit, RabbitModel<Rabbit>> {
    public RabbitRendererMixin(EntityRendererProvider.Context context, RabbitModel<Rabbit> model, float shadow) {
        super(context, model, shadow);
    }

    @Override
    public void render(@NotNull Rabbit rabbit, float f0, float f1, @NotNull PoseStack pose, @NotNull MultiBufferSource source, int i) {
        if (ShiftedLensConfig.RABBIT_REVAMP.get()) pose.scale(1.3F, 1.3F, 1.3F);
        super.render(rabbit, f0, f1, pose, source, i);
    }

}