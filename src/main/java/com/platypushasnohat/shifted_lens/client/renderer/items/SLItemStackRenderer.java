package com.platypushasnohat.shifted_lens.client.renderer.items;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.blockentity.WhirligigBlockModel;
import com.platypushasnohat.shifted_lens.registry.SLBlocks;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class SLItemStackRenderer extends BlockEntityWithoutLevelRenderer {

    private static final ResourceLocation WHIRLIGIG_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/block/whirligig.png");
    private static final WhirligigBlockModel WHIRLIGIG_MODEL = new WhirligigBlockModel(WhirligigBlockModel.createMesh().bakeRoot());

    public SLItemStackRenderer() {
        super(null, null);
    }

    @Override
    public void renderByItem(ItemStack itemStackIn, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackIn.is(SLBlocks.WHIRLIGIG.get().asItem())) {
            poseStack.pushPose();
            poseStack.translate(0.5F, 1.5F, 0.5F);
            poseStack.mulPose(Axis.XP.rotationDegrees(-180));
            WHIRLIGIG_MODEL.root().getAllParts().forEach(ModelPart::resetPose);
            WHIRLIGIG_MODEL.renderToBuffer(poseStack, getVertexConsumer(bufferIn, RenderType.entityCutoutNoCull(WHIRLIGIG_TEXTURE), WHIRLIGIG_TEXTURE), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            poseStack.popPose();
        }
    }

    private static VertexConsumer getVertexConsumer(MultiBufferSource bufferIn, RenderType renderType, ResourceLocation resourceLocation){
        return bufferIn.getBuffer(renderType);
    }
}
