package com.platypushasnohat.shifted_lens.client.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.ShiftedGhastAnimations;
import com.platypushasnohat.shifted_lens.entities.ShiftedGhast;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class ShiftedGhastModel<T extends ShiftedGhast> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart Ghast;
	private final ModelPart Head;
	private final ModelPart Closed_Mouth;
	private final ModelPart Brow;
	private final ModelPart SadBrow;
	private final ModelPart Eyes;
	private final ModelPart Tentacle1;
	private final ModelPart Tentacle2;
	private final ModelPart Tentacle3;
	private final ModelPart Tentacle4;
	private final ModelPart Tentacle6;
	private final ModelPart Tentacle7;
	private final ModelPart Tentacle8;
	private final ModelPart Tentacle9;
	private final List<ModelPart> glowingLayerModelParts;

	public ShiftedGhastModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Ghast = this.root.getChild("Ghast");
		this.Head = this.Ghast.getChild("Head");
		this.Closed_Mouth = this.Head.getChild("Closed_Mouth");
		this.Brow = this.Head.getChild("Brow");
		this.SadBrow = this.Brow.getChild("SadBrow");
		this.Eyes = this.Head.getChild("Eyes");
		this.Tentacle1 = this.Ghast.getChild("Tentacle1");
		this.Tentacle2 = this.Ghast.getChild("Tentacle2");
		this.Tentacle3 = this.Ghast.getChild("Tentacle3");
		this.Tentacle4 = this.Ghast.getChild("Tentacle4");
		this.Tentacle6 = this.Ghast.getChild("Tentacle6");
		this.Tentacle7 = this.Ghast.getChild("Tentacle7");
		this.Tentacle8 = this.Ghast.getChild("Tentacle8");
		this.Tentacle9 = this.Ghast.getChild("Tentacle9");
		this.glowingLayerModelParts = ImmutableList.of(this.Head, this.Eyes);

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
		PartDefinition Ghast = root.addOrReplaceChild("Ghast", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Head = Ghast.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0F, -32.0F, -31.0F, 64.0F, 64.0F, 64.0F, new CubeDeformation(0.1F)).texOffs(128, 153).addBox(-13.0F, 6.0F, -30.5F, 26.0F, 26.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -32.0F, 0.0F));
		PartDefinition Closed_Mouth = Head.addOrReplaceChild("Closed_Mouth", CubeListBuilder.create().texOffs(0, 158).addBox(-13.0F, -13.0F, -30.75F, 26.0F, 26.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 18.0F, 0.0F));
		PartDefinition Brow = Head.addOrReplaceChild("Brow", CubeListBuilder.create().texOffs(11, 236).addBox(-50.0F, -51.0F, -0.5F, 38.0F, 16.0F, 0.0F, new CubeDeformation(0.1F)).texOffs(159, 210).addBox(-50.0F, -44.0F, -0.75F, 38.0F, 9.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(31.0F, 32.0F, -30.0F));
		PartDefinition SadBrow = Brow.addOrReplaceChild("SadBrow", CubeListBuilder.create().texOffs(0, 151).addBox(-50.0F, -51.0F, -1.0F, 38.0F, 7.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.25F));
		PartDefinition Eyes = Head.addOrReplaceChild("Eyes", CubeListBuilder.create().texOffs(0, 128).addBox(-20.0F, -9.0F, 0.0F, 40.0F, 23.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -12.5F, -30.0F));
		PartDefinition Tentacle1 = Ghast.addOrReplaceChild("Tentacle1", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-23.0F, -6.0F, -22.0F));
		PartDefinition Tentacle2 = Ghast.addOrReplaceChild("Tentacle2", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -6.0F, -22.0F));
		PartDefinition Tentacle3 = Ghast.addOrReplaceChild("Tentacle3", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(23.0F, -6.0F, -22.0F));
		PartDefinition Tentacle4 = Ghast.addOrReplaceChild("Tentacle4", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-23.0F, -5.0F, 1.0F));
		PartDefinition Tentacle6 = Ghast.addOrReplaceChild("Tentacle6", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(23.0F, -5.0F, 1.0F));
		PartDefinition Tentacle7 = Ghast.addOrReplaceChild("Tentacle7", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-23.0F, -5.0F, 24.0F));
		PartDefinition Tentacle8 = Ghast.addOrReplaceChild("Tentacle8", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(1.0F, -5.0F, 24.0F));
		PartDefinition Tentacle9 = Ghast.addOrReplaceChild("Tentacle9", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(23.0F, -5.0F, 24.0F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(ShiftedGhast entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.animate(entity.idleAnimationState, ShiftedGhastAnimations.IDLE, ageInTicks, 1.75F);
		this.animate(entity.shootAnimationState, ShiftedGhastAnimations.ATTACK, ageInTicks);

		this.Tentacle1.xRot = (float) (limbSwingAmount * Math.toRadians(12) + Mth.cos(limbSwing * 0.19F) * 0.2F * limbSwingAmount);
		this.Tentacle2.xRot = (float) (limbSwingAmount * Math.toRadians(10) + Mth.cos(limbSwing * 0.17F) * 0.2F * limbSwingAmount);
		this.Tentacle3.xRot = (float) (limbSwingAmount * Math.toRadians(11) + Mth.cos(limbSwing * 0.2F) * 0.2F * limbSwingAmount);
		this.Tentacle4.xRot = (float) (limbSwingAmount * Math.toRadians(12) + Mth.cos(limbSwing * 0.18F) * 0.2F * limbSwingAmount);
		this.Tentacle6.xRot = (float) (limbSwingAmount * Math.toRadians(11) + Mth.cos(limbSwing * 0.2F) * 0.2F * limbSwingAmount);
		this.Tentacle7.xRot = (float) (limbSwingAmount * Math.toRadians(13) + Mth.cos(limbSwing * 0.19F) * 0.2F * limbSwingAmount);
		this.Tentacle8.xRot = (float) (limbSwingAmount * Math.toRadians(13) + Mth.cos(limbSwing * 0.2F) * 0.2F * limbSwingAmount);
		this.Tentacle9.xRot = (float) (limbSwingAmount * Math.toRadians(12) + Mth.cos(limbSwing * 0.18F) * 0.2F * limbSwingAmount);

		this.Ghast.xRot = headPitch / (180F / (float) Math.PI);
		this.Ghast.yRot = netHeadYaw / (180F / (float) Math.PI);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart root() {
		return this.root;
	}

	public List<ModelPart> getGlowingLayerModelParts() {
		return this.glowingLayerModelParts;
	}
}