package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.GuardianRemodelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Guardian;
import org.jetbrains.annotations.NotNull;

public class SLGuardianRenderer extends EntityRenderer<Guardian> {

    private final GuardianRenderer vanilla;
    private final GuardianRemodelRenderer remodel;

    public SLGuardianRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new GuardianRenderer(context);
        this.remodel = new GuardianRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull Guardian guardian, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.GUARDIAN_REMODEL.get()) {
            this.remodel.render(guardian, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(guardian, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Guardian guardian) {
        return ShiftedLensConfig.GUARDIAN_REMODEL.get() ? this.remodel.getTextureLocation(guardian) : this.vanilla.getTextureLocation(guardian);
    }
}
