package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLTropicalFishModelB<T extends TropicalFish> extends ColorableHierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart fin;
	private final ModelPart tail;
	private final ModelPart bottom_fin;

	public SLTropicalFishModelB(ModelPart root) {
		this.root = root.getChild("root");
		this.fin = this.root.getChild("fin");
		this.tail = this.root.getChild("tail");
		this.bottom_fin = this.root.getChild("bottom_fin");
	}

	public static LayerDefinition createBodyLayer(CubeDeformation deformation) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-0.5F, -4.0F, -5.0F, 1.0F, 8.0F, 9.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 0.0F));

		PartDefinition fin = root.addOrReplaceChild("fin", CubeListBuilder.create().texOffs(0, 14).addBox(0.0F, -3.0F, -2.0F, 0.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.0F, 0.0F));

		PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(1, 18).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 4.0F));

		PartDefinition bottom_fin = root.addOrReplaceChild("bottom_fin", CubeListBuilder.create().texOffs(0, 12).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 4.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(TropicalFish entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);

		float f = 1.0F;

		if (!entity.isInWater()) {
			f = 1.5F;
		}
		this.tail.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);

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