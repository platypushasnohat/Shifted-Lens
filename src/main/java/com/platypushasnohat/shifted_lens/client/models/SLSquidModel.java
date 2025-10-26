package com.platypushasnohat.shifted_lens.client.models;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.animations.SLSquidAnimations;
import com.platypushasnohat.shifted_lens.mixin_utils.AnimationStateAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.animal.Squid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class SLSquidModel<T extends Squid> extends HierarchicalModel<T> {

	private final ModelPart root;
	private final ModelPart body;
	private final ModelPart tentacle1;
	private final ModelPart tentacle2;
	private final ModelPart tentacle3;
	private final ModelPart tentacle4;
	private final ModelPart tentacle5;
	private final ModelPart tentacle6;
	private final ModelPart tentacle7;
	private final ModelPart tentacle8;
    private final List<ModelPart> tentacles;

	public SLSquidModel(ModelPart root) {
		this.root = root.getChild("root");
		this.body = this.root.getChild("body");
		this.tentacle1 = this.body.getChild("tentacle1");
		this.tentacle2 = this.body.getChild("tentacle2");
		this.tentacle3 = this.body.getChild("tentacle3");
		this.tentacle4 = this.body.getChild("tentacle4");
		this.tentacle5 = this.body.getChild("tentacle5");
		this.tentacle6 = this.body.getChild("tentacle6");
		this.tentacle7 = this.body.getChild("tentacle7");
		this.tentacle8 = this.body.getChild("tentacle8");
        this.tentacles = ImmutableList.of(tentacle1, tentacle2, tentacle3, tentacle4, tentacle5, tentacle6, tentacle7, tentacle8);
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 16.0F, 0.0F));

		PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-6.0F, -8.0F, -6.0F, 12.0F, 16.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -1.0F, 0.0F));

		PartDefinition tentacle1 = body.addOrReplaceChild("tentacle1", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(5.0F, 7.0F, 0.0F));

		PartDefinition tentacle2 = body.addOrReplaceChild("tentacle2", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 7.0F, 3.5F));

		PartDefinition tentacle3 = body.addOrReplaceChild("tentacle3", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, 5.0F));

		PartDefinition tentacle4 = body.addOrReplaceChild("tentacle4", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 7.0F, 3.5F));

		PartDefinition tentacle5 = body.addOrReplaceChild("tentacle5", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-5.0F, 7.0F, 0.0F));

		PartDefinition tentacle6 = body.addOrReplaceChild("tentacle6", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-3.5F, 7.0F, -3.5F));

		PartDefinition tentacle7 = body.addOrReplaceChild("tentacle7", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 7.0F, -5.0F));

		PartDefinition tentacle8 = body.addOrReplaceChild("tentacle8", CubeListBuilder.create()
				.texOffs(48, 0).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 18.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(3.5F, 7.0F, -3.5F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.root().getAllParts().forEach(ModelPart::resetPose);
        AnimationState swimmingAnimationState = ((AnimationStateAccess) entity).shiftedLens$getSwimmingAnimationState();
        this.animate(swimmingAnimationState, SLSquidAnimations.PUSH, ageInTicks, 0.75F + (Mth.clamp(limbSwingAmount, 0.25F, 1.0F) * 1.5F));
        float partialTicks = ageInTicks - entity.tickCount;
        if (entity.isInWaterOrBubble()) {
            this.root.yRot = (entity.tickCount + partialTicks) * 0.02F % ((float) Math.PI * 2F);
        }
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