package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.CodRemodelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.CodRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Cod;
import org.jetbrains.annotations.NotNull;

public class SLCodRenderer extends EntityRenderer<Cod> {

    private final CodRenderer vanilla;
    private final CodRemodelRenderer remodel;

    public SLCodRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new CodRenderer(context);
        this.remodel = new CodRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull Cod cod, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.COD_REMODEL.get()) {
            this.remodel.render(cod, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(cod, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Cod cod) {
        return ShiftedLensConfig.COD_REMODEL.get() ? this.remodel.getTextureLocation(cod) : this.vanilla.getTextureLocation(cod);
    }
}
