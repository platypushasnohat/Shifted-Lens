package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.entities.SLSalmon;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLSalmonModel<T extends SLSalmon> extends HierarchicalModel<T> {

	private final ModelPart bone2;
	private final ModelPart bone6;
	private final ModelPart bone;
	private final ModelPart bone3;
	private final ModelPart bone8;
	private final ModelPart bone4;
	private final ModelPart bone5;
	private final ModelPart bone7;

	public SLSalmonModel(ModelPart root) {
		this.bone2 = root.getChild("bone2");
		this.bone6 = this.bone2.getChild("bone6");
		this.bone = this.bone2.getChild("bone");
		this.bone3 = this.bone2.getChild("bone3");
		this.bone8 = this.bone3.getChild("bone8");
		this.bone4 = this.bone3.getChild("bone4");
		this.bone5 = this.bone3.getChild("bone5");
		this.bone7 = this.bone2.getChild("bone7");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition bone2 = partdefinition.addOrReplaceChild("bone2", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(18, 18).addBox(0.5F, -4.0F, -3.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(-0.5F, 19.0F, -2.0F));

		PartDefinition bone6 = bone2.addOrReplaceChild("bone6", CubeListBuilder.create().texOffs(24, 25).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, 2.0F, -3.0F));

		PartDefinition bone = bone2.addOrReplaceChild("bone", CubeListBuilder.create().texOffs(18, 10).addBox(-1.0F, -2.5F, -2.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
		.texOffs(0, 22).addBox(-1.0F, -2.5F, -4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(8, 22).addBox(-1.0F, -2.5F, -5.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(14, 25).addBox(-1.0F, 1.5F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.5F, -3.0F));

		PartDefinition bone3 = bone2.addOrReplaceChild("bone3", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(14, 22).addBox(0.5F, -4.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(0.5F, 2.0F, 3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 3.0F));

		PartDefinition bone8 = bone3.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, -2.0F, -1.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -0.5F, 6.0F));

		PartDefinition bone4 = bone3.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(20, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 1.0F));

		PartDefinition bone5 = bone3.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(20, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 1.0F));

		PartDefinition bone7 = bone2.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(24, 25).mirror().addBox(-3.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 2.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	public void setupAnim(SLSalmon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		this.bone2.xRot = headPitch * (Mth.DEG_TO_RAD);
		this.bone2.zRot = netHeadYaw * (Mth.DEG_TO_RAD) / 2;
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.bone2.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.bone2;
	}
}