package com.platypushasnohat.shifted_lens.client.models.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;

public class WhirligigBlockModel extends HierarchicalModel<Entity> {

	public ModelPart root;
	public ModelPart spinner;

	public WhirligigBlockModel(ModelPart root) {
		this.root = root.getChild("root");
		this.spinner = this.root.getChild("spinner");
	}

	public static LayerDefinition createMesh() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create()
				.texOffs(0, 13).addBox(-0.5F, 17.0F, 0.0F, 1.0F, 16.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -9.0F, 0.0F));

        root.addOrReplaceChild("spinner", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-6.0F, 3.0F, 2.75F, 13.0F, 13.0F, 0.0F, new CubeDeformation(0.025F))
                .texOffs(4, 13).addBox(-1.0F, 8.0F, 3.0F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offset(-0.5F, 7.0F, -3.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		float rotation = 0.5F;
		this.spinner.zRot = ageInTicks * rotation % ((float) Math.PI * 2F);
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