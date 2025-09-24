package com.platypushasnohat.shifted_lens.client.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLRabbitModel extends EntityModel<Rabbit> {

    private final ModelPart leftRearFoot;
    private final ModelPart rightRearFoot;
    private final ModelPart leftHaunch;
    private final ModelPart rightHaunch;
    private final ModelPart body;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart head;
    private final ModelPart rightEar;
    private final ModelPart leftEar;
    private final ModelPart tail;
    private final ModelPart nose;
    private float jumpRotation;

    public SLRabbitModel(ModelPart modelPart) {
        this.leftRearFoot = modelPart.getChild("left_hind_foot");
        this.rightRearFoot = modelPart.getChild("right_hind_foot");
        this.leftHaunch = modelPart.getChild("left_haunch");
        this.rightHaunch = modelPart.getChild("right_haunch");
        this.body = modelPart.getChild("body");
        this.leftFrontLeg = modelPart.getChild("left_front_leg");
        this.rightFrontLeg = modelPart.getChild("right_front_leg");
        this.head = modelPart.getChild("head");
        this.rightEar = modelPart.getChild("right_ear");
        this.leftEar = modelPart.getChild("left_ear");
        this.tail = modelPart.getChild("tail");
        this.nose = modelPart.getChild("nose");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        partdefinition.addOrReplaceChild("left_hind_foot", CubeListBuilder.create().texOffs(26, 24).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F), PartPose.offset(3.0F, 17.5F, 3.7F));
        partdefinition.addOrReplaceChild("right_hind_foot", CubeListBuilder.create().texOffs(8, 24).addBox(-1.0F, 5.5F, -3.7F, 2.0F, 1.0F, 7.0F), PartPose.offset(-3.0F, 17.5F, 3.7F));
        partdefinition.addOrReplaceChild("left_haunch", CubeListBuilder.create().texOffs(30, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F), PartPose.offsetAndRotation(3.0F, 17.5F, 3.7F, -0.34906584F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_haunch", CubeListBuilder.create().texOffs(16, 15).addBox(-1.0F, 0.0F, 0.0F, 2.0F, 4.0F, 5.0F), PartPose.offsetAndRotation(-3.0F, 17.5F, 3.7F, -0.34906584F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -2.0F, -10.0F, 6.0F, 5.0F, 10.0F), PartPose.offsetAndRotation(0.0F, 19.0F, 8.0F, -0.34906584F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create().texOffs(8, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.offsetAndRotation(3.0F, 17.0F, -1.0F, -0.17453292F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create().texOffs(0, 15).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F), PartPose.offsetAndRotation(-3.0F, 17.0F, -1.0F, -0.17453292F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-2.5F, -4.0F, -5.0F, 5.0F, 4.0F, 5.0F), PartPose.offset(0.0F, 16.0F, -1.0F));
        partdefinition.addOrReplaceChild("right_ear", CubeListBuilder.create().texOffs(52, 0).addBox(-2.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 16.0F, -1.0F, 0.0F, -0.2617994F, 0.0F));
        partdefinition.addOrReplaceChild("left_ear", CubeListBuilder.create().texOffs(58, 0).addBox(0.5F, -9.0F, -1.0F, 2.0F, 5.0F, 1.0F), PartPose.offsetAndRotation(0.0F, 16.0F, -1.0F, 0.0F, 0.2617994F, 0.0F));
        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(52, 6).addBox(-1.5F, -1.5F, 0.0F, 3.0F, 3.0F, 2.0F), PartPose.offsetAndRotation(0.0F, 20.0F, 7.0F, -0.3490659F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("nose", CubeListBuilder.create().texOffs(32, 9).addBox(-0.5F, -2.5F, -5.5F, 1.0F, 1.0F, 1.0F), PartPose.offset(0.0F, 16.0F, -1.0F));
        return LayerDefinition.create(meshdefinition, 64, 32);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer consumer, int i, int i1, float v, float v1, float v2, float v3) {
        if (this.young) {
            poseStack.pushPose();
            poseStack.scale(0.7F, 0.7F, 0.7F);
            poseStack.translate(0.0F, 0.85F, 0.125F);
            ImmutableList.of(this.head, this.leftEar, this.rightEar, this.nose).forEach((modelPart) -> modelPart.render(poseStack, consumer, i, i1, v, v1, v2, v3));
            poseStack.popPose();
            poseStack.pushPose();
            poseStack.scale(0.5F, 0.5F, 0.5F);
            poseStack.translate(0.0F, 1.5F, 0.0F);
            ImmutableList.of(this.leftRearFoot, this.rightRearFoot, this.leftHaunch, this.rightHaunch, this.body, this.leftFrontLeg, this.rightFrontLeg, this.tail).forEach((modelPart) -> modelPart.render(poseStack, consumer, i, i1, v, v1, v2, v3));
            poseStack.popPose();
        } else {
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.0F, -0.125F);
            ImmutableList.of(this.leftRearFoot, this.rightRearFoot, this.leftHaunch, this.rightHaunch, this.body, this.leftFrontLeg, this.rightFrontLeg, this.head, this.rightEar, this.leftEar, this.tail, this.nose).forEach((modelPart) -> modelPart.render(poseStack, consumer, i, i1, v, v1, v2, v3));
            poseStack.popPose();
        }
    }

    @Override
    public void setupAnim(Rabbit entity, float v, float v1, float v2, float v3, float v4) {
        float f = v2 - (float) entity.tickCount;
        this.nose.xRot = v4 * ((float) Math.PI / 180F);
        this.head.xRot = v4 * ((float) Math.PI / 180F);
        this.rightEar.xRot = v4 * ((float) Math.PI / 180F);
        this.leftEar.xRot = v4 * ((float) Math.PI / 180F);
        this.nose.yRot = v3 * ((float) Math.PI / 180F);
        this.head.yRot = v3 * ((float) Math.PI / 180F);
        this.rightEar.yRot = this.nose.yRot - 0.2617994F;
        this.leftEar.yRot = this.nose.yRot + 0.2617994F;
        this.jumpRotation = Mth.sin(entity.getJumpCompletion(f) * (float)Math.PI);
        this.leftHaunch.xRot = (this.jumpRotation * 50.0F - 21.0F) * ((float)Math.PI / 180F);
        this.rightHaunch.xRot = (this.jumpRotation * 50.0F - 21.0F) * ((float)Math.PI / 180F);
        this.leftRearFoot.xRot = this.jumpRotation * 50.0F * ((float)Math.PI / 180F);
        this.rightRearFoot.xRot = this.jumpRotation * 50.0F * ((float)Math.PI / 180F);
        this.leftFrontLeg.xRot = (this.jumpRotation * -40.0F - 11.0F) * ((float)Math.PI / 180F);
        this.rightFrontLeg.xRot = (this.jumpRotation * -40.0F - 11.0F) * ((float)Math.PI / 180F);
    }

    @Override
    public void prepareMobModel(Rabbit entity, float v, float v1, float v2) {
        super.prepareMobModel(entity, v, v1, v2);
        this.jumpRotation = Mth.sin(entity.getJumpCompletion(v2) * (float) Math.PI);
    }
}
