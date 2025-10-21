package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.SLGuardianAnimations;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLGuardianModel extends HierarchicalModel<Guardian> {

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart body_flipped;
	private final ModelPart body_sub_1;
	private final ModelPart eye;
	private final ModelPart spine1;
	private final ModelPart spine1_rotation;
	private final ModelPart spine2;
	private final ModelPart spine2_rotation;
	private final ModelPart spine3;
	private final ModelPart spine3_rotation;
	private final ModelPart spine4;
	private final ModelPart spine4_rotation;
	private final ModelPart spine5;
	private final ModelPart spine5_rotation;
	private final ModelPart spine6;
	private final ModelPart spine6_rotation;
	private final ModelPart spine7;
	private final ModelPart spine7_rotation;
	private final ModelPart spine8;
	private final ModelPart spine8_rotation;
	private final ModelPart spine9;
	private final ModelPart spine9_rotation;
	private final ModelPart spine10;
	private final ModelPart spine10_rotation;
	private final ModelPart spine11;
	private final ModelPart spine11_rotation;
	private final ModelPart spine12;
	private final ModelPart spine12_rotation;
	private final ModelPart tail1;
	private final ModelPart tail2;
	private final ModelPart tail3;

	public SLGuardianModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.body_flipped = this.body.getChild("body_flipped");
		this.body_sub_1 = this.body_flipped.getChild("body_sub_1");
		this.eye = this.body.getChild("eye");
		this.spine1 = this.body.getChild("spine1");
		this.spine1_rotation = this.spine1.getChild("spine1_rotation");
		this.spine2 = this.body.getChild("spine2");
		this.spine2_rotation = this.spine2.getChild("spine2_rotation");
		this.spine3 = this.body.getChild("spine3");
		this.spine3_rotation = this.spine3.getChild("spine3_rotation");
		this.spine4 = this.body.getChild("spine4");
		this.spine4_rotation = this.spine4.getChild("spine4_rotation");
		this.spine5 = this.body.getChild("spine5");
		this.spine5_rotation = this.spine5.getChild("spine5_rotation");
		this.spine6 = this.body.getChild("spine6");
		this.spine6_rotation = this.spine6.getChild("spine6_rotation");
		this.spine7 = this.body.getChild("spine7");
		this.spine7_rotation = this.spine7.getChild("spine7_rotation");
		this.spine8 = this.body.getChild("spine8");
		this.spine8_rotation = this.spine8.getChild("spine8_rotation");
		this.spine9 = this.body.getChild("spine9");
		this.spine9_rotation = this.spine9.getChild("spine9_rotation");
		this.spine10 = this.body.getChild("spine10");
		this.spine10_rotation = this.spine10.getChild("spine10_rotation");
		this.spine11 = this.body.getChild("spine11");
		this.spine11_rotation = this.spine11.getChild("spine11_rotation");
		this.spine12 = this.body.getChild("spine12");
		this.spine12_rotation = this.spine12.getChild("spine12_rotation");
		this.tail1 = this.body.getChild("tail1");
		this.tail2 = this.tail1.getChild("tail2");
		this.tail3 = this.tail2.getChild("tail3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.5F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -6.5F, -8.0F, 12.0F, 12.0F, 16.0F, new CubeDeformation(0.0F))
				.texOffs(50, 60).addBox(-3.0F, -1.5F, -8.0F, 6.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(31, 59).addBox(-3.0F, -1.5F, -7.0F, 6.0F, 3.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(16, 40).addBox(-6.0F, -8.5F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(16, 40).addBox(-6.0F, 5.5F, -6.0F, 12.0F, 2.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(0, 28).addBox(-8.0F, -6.5F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition body_flipped = body.addOrReplaceChild("body_flipped", CubeListBuilder.create(), PartPose.offset(0.0F, 7.5F, 0.0F));

		PartDefinition body_sub_1 = body_flipped.addOrReplaceChild("body_sub_1", CubeListBuilder.create().texOffs(0, 28).mirror().addBox(6.0F, -14.0F, -6.0F, 2.0F, 12.0F, 12.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition eye = body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(8, 0).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -7.25F));

		PartDefinition spine1 = body.addOrReplaceChild("spine1", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, 7.0F));

		PartDefinition spine1_rotation = spine1.addOrReplaceChild("spine1_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition spine2 = body.addOrReplaceChild("spine2", CubeListBuilder.create(), PartPose.offset(0.0F, -5.0F, -7.0F));

		PartDefinition spine2_rotation = spine2.addOrReplaceChild("spine2_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition spine3 = body.addOrReplaceChild("spine3", CubeListBuilder.create(), PartPose.offset(7.0F, -5.0F, 0.0F));

		PartDefinition spine3_rotation = spine3.addOrReplaceChild("spine3_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition spine4 = body.addOrReplaceChild("spine4", CubeListBuilder.create(), PartPose.offset(-7.0F, -5.0F, 0.0F));

		PartDefinition spine4_rotation = spine4.addOrReplaceChild("spine4_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition spine5 = body.addOrReplaceChild("spine5", CubeListBuilder.create(), PartPose.offset(-7.0F, 2.0F, -7.0F));

		PartDefinition spine5_rotation = spine5.addOrReplaceChild("spine5_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 1.5708F, 0.7854F, 0.0F));

		PartDefinition spine6 = body.addOrReplaceChild("spine6", CubeListBuilder.create(), PartPose.offset(7.0F, 2.0F, -7.0F));

		PartDefinition spine6_rotation = spine6.addOrReplaceChild("spine6_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 1.5708F, -0.7854F, 0.0F));

		PartDefinition spine7 = body.addOrReplaceChild("spine7", CubeListBuilder.create(), PartPose.offset(7.0F, 2.0F, 7.0F));

		PartDefinition spine7_rotation = spine7.addOrReplaceChild("spine7_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, -1.5708F, 0.7854F, 0.0F));

		PartDefinition spine8 = body.addOrReplaceChild("spine8", CubeListBuilder.create(), PartPose.offset(-7.0F, 2.0F, 7.0F));

		PartDefinition spine8_rotation = spine8.addOrReplaceChild("spine8_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, -1.5708F, -0.7854F, 0.0F));

		PartDefinition spine9 = body.addOrReplaceChild("spine9", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, 7.0F));

		PartDefinition spine9_rotation = spine9.addOrReplaceChild("spine9_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, -2.3562F, 0.0F, 0.0F));

		PartDefinition spine10 = body.addOrReplaceChild("spine10", CubeListBuilder.create(), PartPose.offset(0.0F, 9.0F, -7.0F));

		PartDefinition spine10_rotation = spine10.addOrReplaceChild("spine10_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 2.3562F, 0.0F, 0.0F));

		PartDefinition spine11 = body.addOrReplaceChild("spine11", CubeListBuilder.create(), PartPose.offset(7.0F, 9.0F, 0.0F));

		PartDefinition spine11_rotation = spine11.addOrReplaceChild("spine11_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.0F, 0.0F, 2.3562F));

		PartDefinition spine12 = body.addOrReplaceChild("spine12", CubeListBuilder.create(), PartPose.offset(-7.0F, 9.0F, 0.0F));

		PartDefinition spine12_rotation = spine12.addOrReplaceChild("spine12_rotation", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -2.5F, 0.0F, 0.0F, 0.0F, -2.3562F));

		PartDefinition tail1 = body.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(40, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 9.0F));

		PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 54).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 6.0F));

		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(41, 32).addBox(-1.0F, -1.0F, -2.0F, 2.0F, 2.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(25, 19).addBox(0.0F, -4.5F, 1.0F, 0.0F, 9.0F, 9.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 0.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Guardian entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimationState eyeAnimationState = ((AnimationStateAccess) entity).shiftedLens$getEyeAnimationState();
		AnimationState beamAnimationState = ((AnimationStateAccess) entity).shiftedLens$getBeamAnimationState();
		AnimationState swimmingAnimationState = ((AnimationStateAccess) entity).shiftedLens$getSwimmingAnimationState();

		this.animate(swimmingAnimationState, SLGuardianAnimations.SWIM, ageInTicks, 0.5F + limbSwingAmount * 1.2F);
		this.animate(eyeAnimationState, SLGuardianAnimations.EYE, ageInTicks);
		this.animate(beamAnimationState, SLGuardianAnimations.BEAM, ageInTicks);

		if (entity.isInWaterOrBubble()) {
			this.root.xRot = headPitch * (Mth.DEG_TO_RAD);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0.0F, 0.05F, -0.025F);
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}