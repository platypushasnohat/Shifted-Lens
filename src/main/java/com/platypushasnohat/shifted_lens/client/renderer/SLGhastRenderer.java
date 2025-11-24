package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.GhastRemodelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GhastRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import org.jetbrains.annotations.NotNull;

public class SLGhastRenderer extends EntityRenderer<Ghast> {

    private final GhastRenderer vanilla;
    private final GhastRemodelRenderer remodel;

    public SLGhastRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new GhastRenderer(context);
        this.remodel = new GhastRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull Ghast ghast, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.GHAST_REVAMP.get()) {
            this.remodel.render(ghast, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(ghast, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Ghast ghast) {
        return ShiftedLensConfig.GHAST_REVAMP.get() ? this.remodel.getTextureLocation(ghast) : this.vanilla.getTextureLocation(ghast);
    }
}
