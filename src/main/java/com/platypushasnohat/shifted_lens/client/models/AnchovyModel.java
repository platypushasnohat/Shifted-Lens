package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.AnchovyAnimations;
import com.platypushasnohat.shifted_lens.entities.Anchovy;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class AnchovyModel<T extends Anchovy> extends HierarchicalModel<T> {

	private final ModelPart Baitfish;
	private final ModelPart bottomfin;
	private final ModelPart topfin;
	private final ModelPart tailfin;

	public AnchovyModel(ModelPart root) {
		this.Baitfish = root.getChild("Baitfish");
		this.bottomfin = this.Baitfish.getChild("bottomfin");
		this.topfin = this.Baitfish.getChild("topfin");
		this.tailfin = this.Baitfish.getChild("tailfin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition Baitfish = partdefinition.addOrReplaceChild("Baitfish", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

		PartDefinition bottomfin = Baitfish.addOrReplaceChild("bottomfin", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition topfin = Baitfish.addOrReplaceChild("topfin", CubeListBuilder.create().texOffs(14, 12).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition tailfin = Baitfish.addOrReplaceChild("tailfin", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Anchovy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity.isInWater()) {
			this.animateWalk(AnchovyAnimations.SWIM, limbSwing, limbSwingAmount, 2, 4);
			this.Baitfish.xRot = headPitch * (Mth.DEG_TO_RAD);
			this.Baitfish.zRot = netHeadYaw * (Mth.DEG_TO_RAD) / 2;
		} else {
			this.animate(entity.flopAnimationState, AnchovyAnimations.FLOP, ageInTicks);
			this.Baitfish.resetPose();
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.Baitfish.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart root() {
		return this.Baitfish;
	}
}