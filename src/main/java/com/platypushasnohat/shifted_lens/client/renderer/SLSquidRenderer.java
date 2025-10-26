package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.SquidRemodelRenderer;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Squid;
import org.jetbrains.annotations.NotNull;

public class SLSquidRenderer extends EntityRenderer<Squid> {

    private final SquidRenderer<Squid> vanilla;
    private final SquidRemodelRenderer remodel;

    public SLSquidRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.vanilla = new SquidRenderer<>(context, new SquidModel<>(context.bakeLayer(ModelLayers.SQUID)));
        this.remodel = new SquidRemodelRenderer(context);
    }

    @Override
    public void render(@NotNull Squid squid, float yaw, float partialTicks, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        if (ShiftedLensConfig.SQUID_REMODEL.get()) {
            this.remodel.render(squid, yaw, partialTicks, poseStack, bufferSource, packedLight);
        } else {
            this.vanilla.render(squid, yaw, partialTicks, poseStack, bufferSource, packedLight);
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Squid squid) {
        return ShiftedLensConfig.SQUID_REMODEL.get() ? this.remodel.getTextureLocation(squid) : this.vanilla.getTextureLocation(squid);
    }
}
