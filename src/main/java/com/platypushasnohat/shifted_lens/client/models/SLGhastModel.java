package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.SLGhastAnimations;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLGhastModel extends HierarchicalModel<Ghast> {

	private final ModelPart root;
	private final ModelPart Ghast;
	private final ModelPart Head;
	private final ModelPart Closed_Mouth;
	private final ModelPart Brow;
	private final ModelPart SadBrow;
	private final ModelPart Eyes;
	private final ModelPart[] tentacles = new ModelPart[8];

	public SLGhastModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Ghast = this.root.getChild("Ghast");
		this.Head = this.Ghast.getChild("Head");
		this.Closed_Mouth = this.Head.getChild("Closed_Mouth");
		this.Brow = this.Head.getChild("Brow");
		this.SadBrow = this.Brow.getChild("SadBrow");
		this.Eyes = this.Head.getChild("Eyes");
		for (int i = 0; i < this.tentacles.length; i++) {
			this.tentacles[i] = this.Ghast.getChild("Tentacle" + i);
		}
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Ghast = root.addOrReplaceChild("Ghast", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition Head = Ghast.addOrReplaceChild("Head", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-32.0F, -32.0F, -31.0F, 64.0F, 64.0F, 64.0F, new CubeDeformation(0.1F)).texOffs(128, 153).addBox(-13.0F, 6.0F, -30.5F, 26.0F, 26.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -32.0F, 0.0F));

		PartDefinition Closed_Mouth = Head.addOrReplaceChild("Closed_Mouth", CubeListBuilder.create()
				.texOffs(0, 158).addBox(-13.0F, -13.0F, -30.75F, 26.0F, 26.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 18.0F, 0.0F));

		PartDefinition Brow = Head.addOrReplaceChild("Brow", CubeListBuilder.create()
				.texOffs(11, 236).addBox(-50.0F, -51.0F, -0.5F, 38.0F, 16.0F, 0.0F, new CubeDeformation(0.1F)).texOffs(159, 210).addBox(-50.0F, -44.0F, -0.75F, 38.0F, 9.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(31.0F, 32.0F, -30.0F));

		PartDefinition SadBrow = Brow.addOrReplaceChild("SadBrow", CubeListBuilder.create()
				.texOffs(0, 151).addBox(-50.0F, -51.0F, -1.0F, 38.0F, 7.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, 0.0F, 0.25F));

		PartDefinition Eyes = Head.addOrReplaceChild("Eyes", CubeListBuilder.create()
				.texOffs(0, 128).addBox(-20.0F, -9.0F, 0.0F, 40.0F, 23.0F, 0.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -12.5F, -30.0F));

		PartDefinition Tentacle0 = Ghast.addOrReplaceChild("Tentacle0", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-23.0F, -6.0F, -22.0F));

		PartDefinition Tentacle1 = Ghast.addOrReplaceChild("Tentacle1", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(0.0F, -6.0F, -22.0F));

		PartDefinition Tentacle2 = Ghast.addOrReplaceChild("Tentacle2", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(23.0F, -6.0F, -22.0F));

		PartDefinition Tentacle3 = Ghast.addOrReplaceChild("Tentacle3", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-23.0F, -5.0F, 1.0F));

		PartDefinition Tentacle4 = Ghast.addOrReplaceChild("Tentacle4", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(23.0F, -5.0F, 1.0F));

		PartDefinition Tentacle5 = Ghast.addOrReplaceChild("Tentacle5", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(-23.0F, -5.0F, 24.0F));

		PartDefinition Tentacle6 = Ghast.addOrReplaceChild("Tentacle6", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(1.0F, -5.0F, 24.0F));

		PartDefinition Tentacle7 = Ghast.addOrReplaceChild("Tentacle7", CubeListBuilder.create()
				.texOffs(80, 128).addBox(-6.0F, 0.0F, -6.0F, 12.0F, 35.0F, 12.0F, new CubeDeformation(0.1F)), PartPose.offset(23.0F, -5.0F, 24.0F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	@Override
	public void setupAnim(Ghast entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimationState idleAnimationState = ((AnimationStateAccess) entity).shiftedLens$getIdleAnimationState();
		AnimationState shootAnimationState = ((AnimationStateAccess) entity).shiftedLens$getShootAnimationState();

		this.animate(idleAnimationState, SLGhastAnimations.IDLE, ageInTicks, 1.75F);
		this.animate(shootAnimationState, SLGhastAnimations.ATTACK, ageInTicks);

		for (int i = 0; i < 8; i++) {
			this.tentacles[i].xRot = 0.12F * Mth.sin(ageInTicks * 0.3F + i) + 0.4F;
		}

		this.Ghast.xRot = headPitch / (180F / (float) Math.PI);
		this.Ghast.yRot = netHeadYaw / (180F / (float) Math.PI);
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