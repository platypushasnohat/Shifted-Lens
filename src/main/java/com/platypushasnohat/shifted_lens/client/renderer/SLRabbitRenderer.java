package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.RabbitRemodelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RabbitRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;
import org.jetbrains.annotations.NotNull;

public class SLRabbitRenderer extends EntityRenderer<Rabbit> {

    private final RabbitRenderer vanilla;
    private final RabbitRemodelRenderer remodel;

    public SLRabbitRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new RabbitRenderer(context);
        this.remodel = new RabbitRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull Rabbit rabbit, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.RABBIT_REMODEL.get()) {
            this.remodel.render(rabbit, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(rabbit, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Rabbit rabbit) {
        return ShiftedLensConfig.RABBIT_REMODEL.get() ? this.remodel.getTextureLocation(rabbit) : this.vanilla.getTextureLocation(rabbit);
    }
}
