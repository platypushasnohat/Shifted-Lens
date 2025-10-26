package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.ElderGuardianRemodelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ElderGuardian;
import org.jetbrains.annotations.NotNull;

public class SLElderGuardianRenderer extends EntityRenderer<ElderGuardian> {

    private final ElderGuardianRenderer vanilla;
    private final ElderGuardianRemodelRenderer remodel;

    public SLElderGuardianRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new ElderGuardianRenderer(context);
        this.remodel = new ElderGuardianRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull ElderGuardian elderGuardian, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.ELDER_GUARDIAN_REMODEL.get()) {
            this.remodel.render(elderGuardian, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(elderGuardian, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull ElderGuardian elderGuardian) {
        return ShiftedLensConfig.ELDER_GUARDIAN_REMODEL.get() ? this.remodel.getTextureLocation(elderGuardian) : this.vanilla.getTextureLocation(elderGuardian);
    }
}
