package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.SalmonRemodelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SalmonRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Salmon;
import org.jetbrains.annotations.NotNull;

public class SLSalmonRenderer extends EntityRenderer<Salmon> {

    private final SalmonRenderer vanilla;
    private final SalmonRemodelRenderer remodel;

    public SLSalmonRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new SalmonRenderer(context);
        this.remodel = new SalmonRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull Salmon salmon, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.SALMON_REMODEL.get()) {
            this.remodel.render(salmon, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(salmon, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Salmon salmon) {
        return ShiftedLensConfig.SALMON_REMODEL.get() ? this.remodel.getTextureLocation(salmon) : this.vanilla.getTextureLocation(salmon);
    }
}
