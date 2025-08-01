package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.entities.Squill;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;

public class SquillModel<T extends Squill> extends HierarchicalModel<T> {

	private final ModelPart squill;
	private final ModelPart body;
	private final ModelPart openjaw;
	private final ModelPart closedjaw;
	private final ModelPart tentacle1;
	private final ModelPart tentacle2;
	private final ModelPart tentacle3;
	private final ModelPart tentacle4;
	private final ModelPart tentacle5;
	private final ModelPart tentacle6;
	private final ModelPart tentacle7;
	private final ModelPart tentacle8;
	private final ModelPart propeller;

	public SquillModel(ModelPart root) {
		this.squill = root.getChild("squill");
		this.body = this.squill.getChild("body");
		this.openjaw = this.body.getChild("openjaw");
		this.closedjaw = this.body.getChild("closedjaw");
		this.tentacle1 = this.body.getChild("tentacle1");
		this.tentacle2 = this.body.getChild("tentacle2");
		this.tentacle3 = this.body.getChild("tentacle3");
		this.tentacle4 = this.body.getChild("tentacle4");
		this.tentacle5 = this.body.getChild("tentacle5");
		this.tentacle6 = this.body.getChild("tentacle6");
		this.tentacle7 = this.body.getChild("tentacle7");
		this.tentacle8 = this.body.getChild("tentacle8");
		this.propeller = this.body.getChild("propeller");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition squill = partdefinition.addOrReplaceChild("squill", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition body = squill.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -6.0F, 0.0F));

		PartDefinition openjaw = body.addOrReplaceChild("openjaw", CubeListBuilder.create().texOffs(-10, 46).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 0.0F));

		PartDefinition closedjaw = body.addOrReplaceChild("closedjaw", CubeListBuilder.create().texOffs(11, 46).addBox(-5.0F, 0.0F, -5.0F, 10.0F, 0.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 6.0F, 0.0F));

		PartDefinition tentacle1 = body.addOrReplaceChild("tentacle1", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 7.0F, 0.0F));

		PartDefinition tentacle2 = body.addOrReplaceChild("tentacle2", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 7.0F, 3.5F));

		PartDefinition tentacle3 = body.addOrReplaceChild("tentacle3", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 5.0F));

		PartDefinition tentacle4 = body.addOrReplaceChild("tentacle4", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 7.0F, 3.5F));

		PartDefinition tentacle5 = body.addOrReplaceChild("tentacle5", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 7.0F, 0.0F));

		PartDefinition tentacle6 = body.addOrReplaceChild("tentacle6", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 7.0F, -3.5F));

		PartDefinition tentacle7 = body.addOrReplaceChild("tentacle7", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -5.0F));

		PartDefinition tentacle8 = body.addOrReplaceChild("tentacle8", CubeListBuilder.create().texOffs(48, 9).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 9.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 7.0F, -3.5F));

		PartDefinition propeller = body.addOrReplaceChild("propeller", CubeListBuilder.create().texOffs(-13, 28).addBox(-6.5F, -10.0F, -6.5F, 13.0F, 0.0F, 13.0F, new CubeDeformation(0.0F))
				.texOffs(48, 20).addBox(-1.0F, -10.0F, 0.0F, 2.0F, 10.0F, 0.0F, new CubeDeformation(0.0F))
				.texOffs(48, 18).addBox(0.0F, -10.0F, -1.0F, 0.0F, 10.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -8.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Squill entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

//		this.squill.xRot = headPitch * (Mth.DEG_TO_RAD);
//		this.squill.zRot = netHeadYaw * ((Mth.DEG_TO_RAD) / 2);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.squill.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.squill;
	}
}