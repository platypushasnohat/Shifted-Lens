package com.platypushasnohat.shifted_lens.client.models;

import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLSalmonModel {

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

		PartDefinition body_back = bone2.addOrReplaceChild("body_back", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(14, 22).addBox(0.5F, -4.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(0, 27).addBox(0.5F, 2.0F, 3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 3.0F));

		PartDefinition bone8 = body_back.addOrReplaceChild("bone8", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, -2.0F, -1.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, -0.5F, 6.0F));

		PartDefinition bone4 = body_back.addOrReplaceChild("bone4", CubeListBuilder.create().texOffs(20, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 1.0F));

		PartDefinition bone5 = body_back.addOrReplaceChild("bone5", CubeListBuilder.create().texOffs(20, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 1.0F));

		PartDefinition bone7 = bone2.addOrReplaceChild("bone7", CubeListBuilder.create().texOffs(24, 25).mirror().addBox(-3.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(-1.0F, 2.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}
}