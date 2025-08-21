package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.AnchovyAnimations;
import com.platypushasnohat.shifted_lens.entities.Anchovy;
import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
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

	private final ModelPart root;
	private final ModelPart bottomfin;
	private final ModelPart topfin;
	private final ModelPart tailfin;

	public AnchovyModel(ModelPart root) {
		this.root = root.getChild("root");
		this.bottomfin = this.root.getChild("bottomfin");
		this.topfin = this.root.getChild("topfin");
		this.tailfin = this.root.getChild("tailfin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -1.0F, -5.0F, 1.0F, 2.0F, 10.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 23.0F, -1.0F));

		PartDefinition bottomfin = root.addOrReplaceChild("bottomfin", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, 0.0F, -1.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

		PartDefinition topfin = root.addOrReplaceChild("topfin", CubeListBuilder.create().texOffs(14, 12).addBox(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition tailfin = root.addOrReplaceChild("tailfin", CubeListBuilder.create().texOffs(8, 12).addBox(0.0F, -1.0F, 0.0F, 0.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Anchovy entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		if (entity.isInWater()) {
			this.animateWalk(AnchovyAnimations.SWIM, limbSwing, limbSwingAmount, 2, 4);
		} else {
			this.animate(entity.flopAnimationState, AnchovyAnimations.FLOP, ageInTicks);
		}

		float prevOnLandProgress = ((AbstractFishAccess) entity).getPrevOnLandProgress();
		float onLandProgress = ((AbstractFishAccess) entity).getOnLandProgress();
		float partialTicks = ageInTicks - entity.tickCount;
		float landProgress = prevOnLandProgress + (onLandProgress - prevOnLandProgress) * partialTicks;

		this.root.xRot = headPitch * (Mth.DEG_TO_RAD);
		this.root.zRot += landProgress * ((float) Math.toRadians(-90) / 5F);
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