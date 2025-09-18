package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.SLCodAnimations;
import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.animal.Cod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLCodModel extends HierarchicalModel<Cod> {

	private final ModelPart root;
	private final ModelPart right_fin;
	private final ModelPart left_fin;
	private final ModelPart head;
	private final ModelPart nose;
	private final ModelPart tail;
	private final ModelPart fin_back;

	public SLCodModel(ModelPart root) {
		this.root = root.getChild("root");
		this.right_fin = this.root.getChild("right_fin");
		this.left_fin = this.root.getChild("left_fin");
		this.head = this.root.getChild("head");
		this.nose = this.head.getChild("nose");
		this.tail = this.root.getChild("tail");
		this.fin_back = this.root.getChild("fin_back");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-1.0F, -3.0F, -2.0F, 2.0F, 5.0F, 7.0F, new CubeDeformation(0.0F))
				.texOffs(12, 12).addBox(0.0F, 2.0F, -2.5F, 0.0F, 1.0F, 7.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 21.0F, -1.0F));

		PartDefinition right_fin = root.addOrReplaceChild("right_fin", CubeListBuilder.create()
				.texOffs(0, 22).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(-1.0F, 1.0F, -1.0F, 0.0F, 0.0F, -0.7854F));

		PartDefinition left_fin = root.addOrReplaceChild("left_fin", CubeListBuilder.create()
				.texOffs(22, 20).addBox(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offsetAndRotation(1.0F, 1.0F, -1.0F, 0.0F, 0.0F, 0.7854F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(12, 20).addBox(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(18, 8).addBox(-1.0F, 2.0F, -1.0F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -2.0F));

		PartDefinition nose = head.addOrReplaceChild("nose", CubeListBuilder.create()
				.texOffs(22, 22).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 24).addBox(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.2F)), PartPose.offset(0.0F, 0.0F, -3.0F));

		PartDefinition tail = root.addOrReplaceChild("tail", CubeListBuilder.create()
				.texOffs(0, 12).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 0.0F, 5.0F));

		PartDefinition fin_back = root.addOrReplaceChild("fin_back", CubeListBuilder.create()
				.texOffs(18, 0).addBox(0.0F, -2.0F, -1.0F, 0.0F, 2.0F, 6.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, -2.0F, -2.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(Cod entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimationState flopAnimationState = ((AnimationStateAccess) entity).getFlopAnimationState();
		AnimationState swimmingAnimationState = ((AnimationStateAccess) entity).getSwimmingAnimationState();

		float prevOnLandProgress = ((AbstractFishAccess) entity).getPrevOnLandProgress();
		float onLandProgress = ((AbstractFishAccess) entity).getOnLandProgress();
		float partialTicks = ageInTicks - entity.tickCount;
		float landProgress = prevOnLandProgress + (onLandProgress - prevOnLandProgress) * partialTicks;

		this.animate(swimmingAnimationState, SLCodAnimations.SWIM, ageInTicks, 0.5F + limbSwingAmount * 1.5F);
		this.animate(flopAnimationState, SLCodAnimations.FLOP, ageInTicks);

		this.root.xRot = headPitch * (Mth.DEG_TO_RAD);
		this.root.zRot += landProgress * ((float) Math.toRadians(-90) / 5F);
	}

	@Override
	public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.root.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public @NotNull ModelPart root() {
		return this.root;
	}
}