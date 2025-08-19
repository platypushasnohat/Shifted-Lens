package com.platypushasnohat.shifted_lens.client.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.SLSalmonAnimations;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLSalmonModel<T extends Salmon> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart left_fin;
	private final ModelPart head;
	private final ModelPart body_back;
	private final ModelPart tail;
	private final ModelPart fin1;
	private final ModelPart fin2;
	private final ModelPart right_fin;

	private float smoothedXRot;

	public SLSalmonModel(ModelPart root) {
		this.root = root.getChild("root");
		this.left_fin = this.root.getChild("left_fin");
		this.head = this.root.getChild("head");
		this.body_back = this.root.getChild("body_back");
		this.tail = this.body_back.getChild("tail");
		this.fin1 = this.body_back.getChild("fin1");
		this.fin2 = this.body_back.getChild("fin2");
		this.right_fin = this.root.getChild("right_fin");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -2.0F, -3.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(18, 18).addBox(0.5F, -4.0F, -3.0F, 0.0F, 2.0F, 5.0F, new CubeDeformation(0.025F)), PartPose.offset(-0.5F, 21.0F, -1.0F));

		PartDefinition left_fin = root.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(24, 25).addBox(0.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.025F)), PartPose.offset(2.0F, 2.0F, -3.0F));

		PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(18, 10).addBox(-1.0F, -2.5F, -2.0F, 2.0F, 5.0F, 3.0F, new CubeDeformation(0.0F))
				.texOffs(0, 22).addBox(-1.0F, -2.5F, -4.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(8, 22).addBox(-1.0F, -2.5F, -5.0F, 2.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(14, 25).addBox(-1.0F, 1.5F, -3.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 0.5F, -3.0F));

		PartDefinition body_back = root.addOrReplaceChild("body_back", CubeListBuilder.create().texOffs(0, 11).addBox(-1.0F, -3.0F, 0.0F, 3.0F, 5.0F, 6.0F, new CubeDeformation(0.0F))
				.texOffs(14, 22).addBox(0.5F, -4.0F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.025F))
				.texOffs(0, 27).addBox(0.5F, 2.0F, 3.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offset(0.0F, 1.0F, 3.0F));

		PartDefinition tail = body_back.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(18, 0).addBox(0.0F, -2.0F, -1.0F, 0.0F, 4.0F, 6.0F, new CubeDeformation(0.025F)), PartPose.offset(0.5F, -0.5F, 6.0F));

		PartDefinition fin1 = body_back.addOrReplaceChild("fin1", CubeListBuilder.create().texOffs(20, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.025F)), PartPose.offset(0.5F, 2.0F, 1.0F));

		PartDefinition fin2 = body_back.addOrReplaceChild("fin2", CubeListBuilder.create().texOffs(20, 25).addBox(0.0F, 0.0F, -1.0F, 0.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.5F, 2.0F, 1.0F));

		PartDefinition right_fin = root.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(24, 25).mirror().addBox(-3.0F, -1.0F, 0.0F, 3.0F, 2.0F, 0.0F, new CubeDeformation(0.025F)).mirror(false), PartPose.offset(-1.0F, 2.0F, -3.0F));

		return LayerDefinition.create(meshdefinition, 32, 64);
	}

	@Override
	public void setupAnim(Salmon entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
		AnimationState flopAnimationState = ((AnimationStateAccess) entity).getFlopAnimationState();

		if (entity.isInWater()) {
			this.animateWalk(SLSalmonAnimations.SWIM, limbSwing, limbSwingAmount, 4, 8);
		} else {
			this.animate(flopAnimationState, SLSalmonAnimations.FLOP, ageInTicks);
		}

		final double dx = entity.getDeltaMovement().x;
		final double dy = entity.getDeltaMovement().y;
		final double dz = entity.getDeltaMovement().z;
		final float horizontalSpeed = Mth.sqrt((float) (dx * dx + dz * dz));
		final float maxHorizontal = Math.max(horizontalSpeed, 0.05F); // avoids weird spikes
		float targetXRot = (float) -Mth.atan2((float) dy, maxHorizontal) * 2.0F;
		targetXRot = Mth.clamp(targetXRot, -0.8F, 0.8F); // should cap around ~45*

		float deltaPitch = Math.abs(targetXRot - this.smoothedXRot);
		float smoothing = Mth.clamp(0.02F + deltaPitch * 0.12F, 0.01F, 0.07F); // helps prevent weird jittering from model rotation (idk why this happens)
		this.smoothedXRot = Mth.lerp(smoothing, this.smoothedXRot, targetXRot);
		float clampedHeadPitch = Mth.clamp(headPitch, -30.0F, 30.0F) * (Mth.DEG_TO_RAD); // limits head pitch to 30*
		this.root.xRot = this.smoothedXRot + clampedHeadPitch;

		this.root.zRot = netHeadYaw * (Mth.DEG_TO_RAD) / 2;
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