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
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLElderGuardianModel extends HierarchicalModel<ElderGuardian> {

	private final ModelPart root;
	private final ModelPart body;
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

	public SLElderGuardianModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
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
		this.tail1 = this.root.getChild("tail1");
		this.tail2 = this.tail1.getChild("tail2");
		this.tail3 = this.tail2.getChild("tail3");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(32, 80).addBox(-12.0F, -15.0F, -8.0F, 24.0F, 4.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(0, 56).addBox(-16.0F, -11.0F, -8.0F, 4.0F, 24.0F, 24.0F, new CubeDeformation(0.0F))
				.texOffs(0, 56).mirror().addBox(12.0F, -11.0F, -8.0F, 4.0F, 24.0F, 24.0F, new CubeDeformation(0.0F)).mirror(false)
				.texOffs(0, 0).addBox(-12.0F, -11.0F, -12.0F, 24.0F, 24.0F, 32.0F, new CubeDeformation(0.0F))
				.texOffs(68, 121).addBox(-6.0F, -1.0F, -12.0F, 12.0F, 6.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(101, 117).addBox(-6.0F, -1.0F, -11.0F, 12.0F, 6.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -13.0F, 0.0F));

		PartDefinition cube_r1 = body.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(32, 80).addBox(-12.0F, -15.0F, -8.0F, 24.0F, 4.0F, 24.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 2.0F, 0.0F, 0.0F, 0.0F, -3.1416F));

		PartDefinition eye = body.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(16, 0).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -10.25F));

		PartDefinition spine1 = body.addOrReplaceChild("spine1", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 8.0F));

		PartDefinition spine1_rotation = spine1.addOrReplaceChild("spine1_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r2 = spine1_rotation.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, 0.0F));

		PartDefinition spine2 = body.addOrReplaceChild("spine2", CubeListBuilder.create(), PartPose.offset(0.0F, -3.0F, 0.0F));

		PartDefinition spine2_rotation = spine2.addOrReplaceChild("spine2_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r3 = spine2_rotation.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, 0.0F));

		PartDefinition spine3 = body.addOrReplaceChild("spine3", CubeListBuilder.create(), PartPose.offset(4.0F, -3.0F, 4.0F));

		PartDefinition spine3_rotation = spine3.addOrReplaceChild("spine3_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r4 = spine3_rotation.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition spine4 = body.addOrReplaceChild("spine4", CubeListBuilder.create(), PartPose.offset(-4.0F, -3.0F, 4.0F));

		PartDefinition spine4_rotation = spine4.addOrReplaceChild("spine4_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r5 = spine4_rotation.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition spine5 = body.addOrReplaceChild("spine5", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition spine5_rotation = spine5.addOrReplaceChild("spine5_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r6 = spine5_rotation.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(0, 0).addBox(-1.7574F, -23.6568F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.7854F, 0.0F, -1.5708F));

		PartDefinition spine6 = body.addOrReplaceChild("spine6", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition spine6_rotation = spine6.addOrReplaceChild("spine6_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r7 = spine6_rotation.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(0, 0).addBox(-1.7574F, -23.6568F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 2.3562F, 0.0F, -1.5708F));

		PartDefinition spine7 = body.addOrReplaceChild("spine7", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition spine7_rotation = spine7.addOrReplaceChild("spine7_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r8 = spine7_rotation.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 0).addBox(-1.7574F, -23.6568F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -2.3562F, 0.0F, -1.5708F));

		PartDefinition spine8 = body.addOrReplaceChild("spine8", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition spine8_rotation = spine8.addOrReplaceChild("spine8_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r9 = spine8_rotation.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(0, 0).addBox(-1.7574F, -23.6568F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7854F, 0.0F, -1.5708F));

		PartDefinition spine9 = body.addOrReplaceChild("spine9", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition spine9_rotation = spine9.addOrReplaceChild("spine9_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r10 = spine9_rotation.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(0, 0).addBox(-1.7574F, -23.6568F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -1.5708F, 0.7854F, 1.5708F));

		PartDefinition spine10 = body.addOrReplaceChild("spine10", CubeListBuilder.create(), PartPose.offset(0.0F, 1.0F, 4.0F));

		PartDefinition spine10_rotation = spine10.addOrReplaceChild("spine10_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r11 = spine10_rotation.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(0, 0).addBox(-1.7574F, -23.6568F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 1.5708F, 0.7854F, -1.5708F));

		PartDefinition spine11 = body.addOrReplaceChild("spine11", CubeListBuilder.create(), PartPose.offset(4.0F, 5.0F, 4.0F));

		PartDefinition spine11_rotation = spine11.addOrReplaceChild("spine11_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r12 = spine11_rotation.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 2.3562F));

		PartDefinition spine12 = body.addOrReplaceChild("spine12", CubeListBuilder.create(), PartPose.offset(-4.0F, 5.0F, 4.0F));

		PartDefinition spine12_rotation = spine12.addOrReplaceChild("spine12_rotation", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition cube_r13 = spine12_rotation.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -18.0F, -2.0F, 4.0F, 18.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -2.3562F));

		PartDefinition tail1 = root.addOrReplaceChild("tail1", CubeListBuilder.create().texOffs(80, 0).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 20.0F));

		PartDefinition tail2 = tail1.addOrReplaceChild("tail2", CubeListBuilder.create().texOffs(0, 108).addBox(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 15.0F));

		PartDefinition tail3 = tail2.addOrReplaceChild("tail3", CubeListBuilder.create().texOffs(82, 64).addBox(-2.0F, -2.0F, -1.0F, 4.0F, 4.0F, 12.0F, new CubeDeformation(0.0F))
				.texOffs(50, 39).addBox(0.0F, -8.0F, 6.0F, 0.0F, 17.0F, 18.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 13.0F));

		return LayerDefinition.create(meshdefinition, 128, 128);
	}

	@Override
	public void setupAnim(ElderGuardian entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimationState eyeAnimationState = ((AnimationStateAccess) entity).getEyeAnimationState();
		AnimationState beamAnimationState = ((AnimationStateAccess) entity).getBeamAnimationState();
		AnimationState swimmingAnimationState = ((AnimationStateAccess) entity).getSwimmingAnimationState();

		this.animate(swimmingAnimationState, SLGuardianAnimations.SWIM, ageInTicks, 0.5F + limbSwingAmount * 1.2F);
		this.animate(eyeAnimationState, SLGuardianAnimations.EYE, ageInTicks);
		this.animate(beamAnimationState, SLGuardianAnimations.BEAM, ageInTicks);

		if (entity.isInWaterOrBubble()) {
			this.root.xRot = headPitch * (Mth.DEG_TO_RAD);
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		poseStack.translate(0.0F, -0.25F, -0.25F);
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}