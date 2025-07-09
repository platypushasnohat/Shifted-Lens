package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.entities.SteelChariot;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SteelChariotModel<T extends SteelChariot> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart exhaust2;
	private final ModelPart exhaust1;
	private final ModelPart wheel1;
	private final ModelPart wheel2;
	private final ModelPart wheel3;
	private final ModelPart wheel4;

	public SteelChariotModel(ModelPart root) {
		this.root = root.getChild("root");
		this.exhaust2 = this.root.getChild("exhaust2");
		this.exhaust1 = this.root.getChild("exhaust1");
		this.wheel1 = this.root.getChild("wheel1");
		this.wheel2 = this.root.getChild("wheel2");
		this.wheel3 = this.root.getChild("wheel3");
		this.wheel4 = this.root.getChild("wheel4");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 53).addBox(-9.0F, -19.9167F, -18.9167F, 18.0F, 16.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(92, 53).addBox(-9.0F, -3.9167F, -18.9167F, 18.0F, 7.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(92, 88).addBox(-9.0F, 3.0833F, -18.9167F, 18.0F, 0.0F, 28.0F, new CubeDeformation(0.0F))
				.texOffs(0, 0).addBox(-11.0F, -3.9167F, -30.9167F, 22.0F, 12.0F, 41.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 11.9167F, 7.9167F));

		PartDefinition exhaust2 = root.addOrReplaceChild("exhaust2", CubeListBuilder.create()
				.texOffs(26, 97).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 3.5833F, 10.0833F));

		PartDefinition exhaust1 = root.addOrReplaceChild("exhaust1", CubeListBuilder.create()
				.texOffs(26, 97).addBox(-2.5F, -2.5F, 0.0F, 5.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 3.5833F, 10.0833F));

		PartDefinition wheel1 = root.addOrReplaceChild("wheel1", CubeListBuilder.create()
				.texOffs(0, 97).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, 8.0833F, -14.9167F));

		PartDefinition wheel2 = root.addOrReplaceChild("wheel2", CubeListBuilder.create()
				.texOffs(0, 97).mirror().addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-11.0F, 8.0833F, -14.9167F));

		PartDefinition wheel3 = root.addOrReplaceChild("wheel3", CubeListBuilder.create()
				.texOffs(0, 97).addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(11.0F, 8.0833F, 3.0833F));

		PartDefinition wheel4 = root.addOrReplaceChild("wheel4", CubeListBuilder.create()
				.texOffs(0, 97).mirror().addBox(-2.5F, -4.0F, -4.0F, 5.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-11.0F, 8.0833F, 3.0833F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(SteelChariot entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}
}