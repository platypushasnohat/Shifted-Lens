package com.platypushasnohat.shifted_lens.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.blocks.blockentity.WhirligigBlockEntity;
import com.platypushasnohat.shifted_lens.client.models.blockentity.WhirligigBlockModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class WhirligigRenderer<T extends WhirligigBlockEntity> implements BlockEntityRenderer<T> {

    private static final WhirligigBlockModel MODEL = new WhirligigBlockModel(WhirligigBlockModel.createMesh().bakeRoot());
    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/block/whirligig.png");

    protected final RandomSource random = RandomSource.create();

    public WhirligigRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(T valve, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        poseStack.pushPose();
        BlockState state = valve.getBlockState();
        poseStack.translate(0.5, 3.5, 0.5);
        poseStack.scale(1,-1,-1);

        MODEL.setupAnim(null, 0, 0, 1, 0, 0);
        MODEL.renderToBuffer(poseStack, bufferIn.getBuffer(RenderType.entityCutoutNoCull(TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1);
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
