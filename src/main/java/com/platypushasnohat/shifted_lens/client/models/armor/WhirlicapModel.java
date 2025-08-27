package com.platypushasnohat.shifted_lens.client.models.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("FieldCanBeLocal, unused")
public class WhirlicapModel extends HumanoidModel<LivingEntity> {

	private final ModelPart whirlicap;
	private final ModelPart thingthatpokesout;
	private final ModelPart whirligig;

	public WhirlicapModel(ModelPart root) {
        super(root);
        this.whirlicap = root.getChild("head").getChild("whirlicap");
		this.thingthatpokesout = whirlicap.getChild("thingthatpokesout");
		this.whirligig = whirlicap.getChild("whirligig");
	}

	public static LayerDefinition createArmorLayer() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(new CubeDeformation(0.0F), 0.0F);
		PartDefinition root = meshdefinition.getRoot();
		PartDefinition head = root.getChild("head");

		PartDefinition whirlicap = head.addOrReplaceChild("whirlicap", CubeListBuilder.create()
				.texOffs(25, 45).addBox(-4.0F, -9.0F, -4.0F, 8.0F, 4.0F, 8.0F, new CubeDeformation(0.6F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition thingthatpokesout = whirlicap.addOrReplaceChild("thingthatpokesout", CubeListBuilder.create()
				.texOffs(25, 57).addBox(-4.0F, -1.655F, -3.6F, 8.0F, 0.0F, 3.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, -2.75F, -4.0F));

		PartDefinition whirligig = whirlicap.addOrReplaceChild("whirligig", CubeListBuilder.create().texOffs(25, 32).addBox(-6.5F, -4.0F, -6.5F, 13.0F, 0.0F, 13.0F, new CubeDeformation(0.005F))
				.texOffs(51, 57).addBox(-1.0F, -4.0F, 0.0F, 2.0F, 4.0F, 0.0F, new CubeDeformation(0.005F))
				.texOffs(47, 57).addBox(0.0F, -4.0F, -1.0F, 0.0F, 4.0F, 2.0F, new CubeDeformation(0.005F)), PartPose.offset(0.0F, -9.625F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public WhirlicapModel withAnimations(LivingEntity entity) {
		float partialTicks = Minecraft.getInstance().getFrameTime();
		float rotation = entity.isInWaterOrBubble() ? 0.05F : (entity.level().isThundering() ? 0.5F : entity.level().isRaining() ? 0.2F : 0.1F);
		float speed = entity.getDeltaMovement().y > 0.05D ? 3 * rotation : 1 * rotation;
		this.whirligig.yRot = (entity.tickCount + partialTicks) * speed % ((float) Math.PI * 2F);
		return this;
	}
}