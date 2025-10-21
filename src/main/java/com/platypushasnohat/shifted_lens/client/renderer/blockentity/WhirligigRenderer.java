package com.platypushasnohat.shifted_lens.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.blocks.WhirligigBlock;
import com.platypushasnohat.shifted_lens.blocks.blockentity.WhirligigBlockEntity;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Quaternionf;

@OnlyIn(Dist.CLIENT)
public class WhirligigRenderer implements BlockEntityRenderer<WhirligigBlockEntity> {

    public static final Material TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation(ShiftedLens.MOD_ID, "block/whirligig"));

    private final ModelPart root;
    private final ModelPart spinner;
    private final ModelPart spinner2;

    public WhirligigRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(SLModelLayers.WHIRLIGIG);
        this.root = modelPart.getChild("root");
        this.spinner = modelPart.getChild("spinner");
        this.spinner2 = modelPart.getChild("spinner2");
    }

    public static LayerDefinition createMesh() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        partdefinition.addOrReplaceChild("root", CubeListBuilder.create()
                .texOffs(0, 13).addBox(-0.5F, 0.5F, -0.625F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        partdefinition.addOrReplaceChild("spinner", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-6.5F, -6.5F, -0.875F, 13.0F, 13.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.ZERO);

        partdefinition.addOrReplaceChild("spinner2", CubeListBuilder.create()
                .texOffs(4, 13).addBox(-1.5F, -1.5F, -0.625F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.ZERO);

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void render(WhirligigBlockEntity whirligig, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light, int overlay) {
        VertexConsumer vertexConsumer = TEXTURE.buffer(bufferSource, RenderType::entityCutout);

        BlockState state = whirligig.getBlockState();
        WhirligigBlock block = (WhirligigBlock) state.getBlock();

        poseStack.translate(0.5F, 1.025F, 0.5F);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        poseStack.mulPose(Axis.YP.rotationDegrees(-block.getYRotationDegrees(state)));
        poseStack.mulPose(new Quaternionf().rotateZ((float) Math.PI));

        poseStack.pushPose();
        this.root.render(poseStack, vertexConsumer, light, overlay);
        this.spinner2.render(poseStack, vertexConsumer, light, overlay);
        poseStack.popPose();

        poseStack.pushPose();
        float h = whirligig.getActiveRotation(partialTicks) * 57.295776F;
        poseStack.mulPose(new Quaternionf().rotateZ(h * ((float) Math.PI / 180)));
        this.spinner.render(poseStack, vertexConsumer, light, overlay);
        poseStack.popPose();
    }

    @Override
    public int getViewDistance() {
        return 256;
    }
}
