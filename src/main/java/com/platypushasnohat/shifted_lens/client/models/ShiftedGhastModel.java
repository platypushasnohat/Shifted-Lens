package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.entities.ShiftedGhast;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class ShiftedGhastModel<T extends ShiftedGhast> extends HierarchicalModel<T> {

	private final ModelPart Flight_Control;
	private final ModelPart Overlay_Ghast;
	private final ModelPart Ghast;
	private final ModelPart Overlay_Head;
	private final ModelPart Head;
	private final ModelPart Overlay_Closed_Mouth;
	private final ModelPart Closed_Mouth;
	private final ModelPart Brow;
	private final ModelPart Overlay_SadBrow;
	private final ModelPart SadBrow;
	private final ModelPart Overlay_Eyes;
	private final ModelPart Eyes;
	private final ModelPart Overlay_Tentacle1;
	private final ModelPart Tentacle1;
	private final ModelPart Overlay_Tentacle2;
	private final ModelPart Tentacle2;
	private final ModelPart Overlay_Tentacle3;
	private final ModelPart Tentacle3;
	private final ModelPart Overlay_Tentacle4;
	private final ModelPart Tentacle4;
	private final ModelPart Overlay_Tentacle6;
	private final ModelPart Tentacle6;
	private final ModelPart Overlay_Tentacle7;
	private final ModelPart Tentacle7;
	private final ModelPart Overlay_Tentacle8;
	private final ModelPart Tentacle8;
	private final ModelPart Overlay_Tentacle9;
	private final ModelPart Tentacle9;

	public ShiftedGhastModel(ModelPart root) {
		this.Flight_Control = root.getChild("Flight_Control");
		this.Overlay_Ghast = this.Flight_Control.getChild("Overlay_Ghast");
		this.Ghast = this.Overlay_Ghast.getChild("Ghast");
		this.Overlay_Head = this.Ghast.getChild("Overlay_Head");
		this.Head = this.Overlay_Head.getChild("Head");
		this.Overlay_Closed_Mouth = this.Head.getChild("Overlay_Closed_Mouth");
		this.Closed_Mouth = this.Overlay_Closed_Mouth.getChild("Closed_Mouth");
		this.Brow = this.Head.getChild("Brow");
		this.Overlay_SadBrow = this.Brow.getChild("Overlay_SadBrow");
		this.SadBrow = this.Overlay_SadBrow.getChild("SadBrow");
		this.Overlay_Eyes = this.Head.getChild("Overlay_Eyes");
		this.Eyes = this.Overlay_Eyes.getChild("Eyes");
		this.Overlay_Tentacle1 = this.Ghast.getChild("Overlay_Tentacle1");
		this.Tentacle1 = this.Overlay_Tentacle1.getChild("Tentacle1");
		this.Overlay_Tentacle2 = this.Ghast.getChild("Overlay_Tentacle2");
		this.Tentacle2 = this.Overlay_Tentacle2.getChild("Tentacle2");
		this.Overlay_Tentacle3 = this.Ghast.getChild("Overlay_Tentacle3");
		this.Tentacle3 = this.Overlay_Tentacle3.getChild("Tentacle3");
		this.Overlay_Tentacle4 = this.Ghast.getChild("Overlay_Tentacle4");
		this.Tentacle4 = this.Overlay_Tentacle4.getChild("Tentacle4");
		this.Overlay_Tentacle6 = this.Ghast.getChild("Overlay_Tentacle6");
		this.Tentacle6 = this.Overlay_Tentacle6.getChild("Tentacle6");
		this.Overlay_Tentacle7 = this.Ghast.getChild("Overlay_Tentacle7");
		this.Tentacle7 = this.Overlay_Tentacle7.getChild("Tentacle7");
		this.Overlay_Tentacle8 = this.Ghast.getChild("Overlay_Tentacle8");
		this.Tentacle8 = this.Overlay_Tentacle8.getChild("Tentacle8");
		this.Overlay_Tentacle9 = this.Ghast.getChild("Overlay_Tentacle9");
		this.Tentacle9 = this.Overlay_Tentacle9.getChild("Tentacle9");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		PartDefinition Flight_Control = partdefinition.addOrReplaceChild("Flight_Control", CubeListBuilder.create(), PartPose.offset(0.0F, -24.0F, 0.0F));
		PartDefinition Overlay_Ghast = Flight_Control.addOrReplaceChild("Overlay_Ghast", CubeListBuilder.create(), PartPose.offset(0.0F, 32.0F, 0.0F));
		PartDefinition Ghast = Overlay_Ghast.addOrReplaceChild("Ghast", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Head = Ghast.addOrReplaceChild("Overlay_Head", CubeListBuilder.create(), PartPose.offset(0.0F, -32.0F, 0.0F));
		PartDefinition Head = Overlay_Head.addOrReplaceChild("Head", CubeListBuilder.create().texOffs(0, 0).addBox(-32.0F, -32.0F, -31.0F, 64.0F, 64.0F, 64.0F, new CubeDeformation(0.1F)).texOffs(128, 153).addBox(-13.0F, 6.0F, -30.5F, 26.0F, 26.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Closed_Mouth = Head.addOrReplaceChild("Overlay_Closed_Mouth", CubeListBuilder.create(), PartPose.offset(31.0F, 30.0F, -29.75F));
		PartDefinition Closed_Mouth = Overlay_Closed_Mouth.addOrReplaceChild("Closed_Mouth", CubeListBuilder.create().texOffs(0, 158).addBox(-44.0F, -25.0F, -1.0F, 26.0F, 26.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Brow = Head.addOrReplaceChild("Brow", CubeListBuilder.create().texOffs(11, 236).addBox(-50.0F, -51.0F, -0.5F, 38.0F, 16.0F, 0.0F, new CubeDeformation(0.1F)).texOffs(159, 210).addBox(-50.0F, -44.0F, -0.75F, 38.0F, 9.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(31.0F, 32.0F, -30.0F));
		PartDefinition Overlay_SadBrow = Brow.addOrReplaceChild("Overlay_SadBrow", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.25F));
		PartDefinition SadBrow = Overlay_SadBrow.addOrReplaceChild("SadBrow", CubeListBuilder.create().texOffs(0, 151).addBox(-50.0F, -51.0F, -1.0F, 38.0F, 7.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Eyes = Head.addOrReplaceChild("Overlay_Eyes", CubeListBuilder.create(), PartPose.offset(0.0F, -12.5F, -30.0F));
		PartDefinition Eyes = Overlay_Eyes.addOrReplaceChild("Eyes", CubeListBuilder.create().texOffs(0, 128).addBox(-20.0F, -9.0F, 0.0F, 40.0F, 23.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle1 = Ghast.addOrReplaceChild("Overlay_Tentacle1", CubeListBuilder.create(), PartPose.offset(-23.0F, -6.0F, -22.0F));
		PartDefinition Tentacle1 = Overlay_Tentacle1.addOrReplaceChild("Tentacle1", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle2 = Ghast.addOrReplaceChild("Overlay_Tentacle2", CubeListBuilder.create(), PartPose.offset(0.0F, -6.0F, -22.0F));
		PartDefinition Tentacle2 = Overlay_Tentacle2.addOrReplaceChild("Tentacle2", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle3 = Ghast.addOrReplaceChild("Overlay_Tentacle3", CubeListBuilder.create(), PartPose.offset(26.0F, -6.0F, -25.0F));
		PartDefinition Tentacle3 = Overlay_Tentacle3.addOrReplaceChild("Tentacle3", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-3.0F, 0.0F, 3.0F));
		PartDefinition Overlay_Tentacle4 = Ghast.addOrReplaceChild("Overlay_Tentacle4", CubeListBuilder.create(), PartPose.offset(-23.0F, -5.0F, 1.0F));
		PartDefinition Tentacle4 = Overlay_Tentacle4.addOrReplaceChild("Tentacle4", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle6 = Ghast.addOrReplaceChild("Overlay_Tentacle6", CubeListBuilder.create(), PartPose.offset(26.0F, -5.0F, 1.0F));
		PartDefinition Tentacle6 = Overlay_Tentacle6.addOrReplaceChild("Tentacle6", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-3.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle7 = Ghast.addOrReplaceChild("Overlay_Tentacle7", CubeListBuilder.create(), PartPose.offset(-23.0F, -5.0F, 24.0F));
		PartDefinition Tentacle7 = Overlay_Tentacle7.addOrReplaceChild("Tentacle7", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle8 = Ghast.addOrReplaceChild("Overlay_Tentacle8", CubeListBuilder.create(), PartPose.offset(1.0F, -5.0F, 24.0F));
		PartDefinition Tentacle8 = Overlay_Tentacle8.addOrReplaceChild("Tentacle8", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		PartDefinition Overlay_Tentacle9 = Ghast.addOrReplaceChild("Overlay_Tentacle9", CubeListBuilder.create(), PartPose.offset(23.0F, -5.0F, 24.0F));
		PartDefinition Tentacle9 = Overlay_Tentacle9.addOrReplaceChild("Tentacle9", CubeListBuilder.create().texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.0F));
		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(ShiftedGhast entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Flight_Control.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	public ModelPart root() {
		return this.Flight_Control;
	}
}