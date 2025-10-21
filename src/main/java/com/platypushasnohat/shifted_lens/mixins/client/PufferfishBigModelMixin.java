package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PufferfishBigModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(PufferfishBigModel.class)
public abstract class PufferfishBigModelMixin<T extends Entity> extends HierarchicalModel<T> {

    @Unique
    private ModelPart swim_control;
    @Unique
    private ModelPart body;
    @Unique
    private ModelPart left_blue_fin;
    @Unique
    private ModelPart right_blue_fin;
    @Unique
    private ModelPart spikes_front_top;
    @Unique
    private ModelPart spikes_middle_top;
    @Unique
    private ModelPart spikes_back_top;
    @Unique
    private ModelPart spikes_front_left;
    @Unique
    private ModelPart spikes_front_right;
    @Unique
    private ModelPart spikes_back_left;
    @Unique
    private ModelPart spikes_back_right;
    @Unique
    private ModelPart spikes_front_bottom;
    @Unique
    private ModelPart spikes_middle_bottom;
    @Unique
    private ModelPart spikes_back_bottom;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ModelPart root, CallbackInfo ci) {
        this.swim_control = root.getChild("swim_control");
        this.body = this.swim_control.getChild("body");
        this.left_blue_fin = this.swim_control.getChild("left_blue_fin");
        this.right_blue_fin = this.swim_control.getChild("right_blue_fin");
        this.spikes_front_top = this.swim_control.getChild("spikes_front_top");
        this.spikes_middle_top = this.swim_control.getChild("spikes_middle_top");
        this.spikes_back_top = this.swim_control.getChild("spikes_back_top");
        this.spikes_front_left = this.swim_control.getChild("spikes_front_left");
        this.spikes_front_right = this.swim_control.getChild("spikes_front_right");
        this.spikes_back_left = this.swim_control.getChild("spikes_back_left");
        this.spikes_back_right = this.swim_control.getChild("spikes_back_right");
        this.spikes_front_bottom = this.swim_control.getChild("spikes_front_bottom");
        this.spikes_middle_bottom = this.swim_control.getChild("spikes_middle_bottom");
        this.spikes_back_bottom = this.swim_control.getChild("spikes_back_bottom");
    }

    @Inject(method = "createBodyLayer", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> cir) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition swim_control = partdefinition.addOrReplaceChild("swim_control", CubeListBuilder.create(), PartPose.offset(0.0F, 19.5F, 0.0F));
        PartDefinition left_blue_fin = partdefinition.addOrReplaceChild("left_blue_fin", CubeListBuilder.create(), PartPose.offset(2.5F, -1.5F, -1.5F));
        PartDefinition right_blue_fin = partdefinition.addOrReplaceChild("right_blue_fin", CubeListBuilder.create(), PartPose.offset(-2.5F, -1.5F, -1.5F));

        swim_control.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, 0.0F));

        swim_control.addOrReplaceChild("left_blue_fin", CubeListBuilder.create().texOffs(24, 3).addBox(0.0F, 0.0F, -0.99F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.0F, -3.5F, -2.0F));

        swim_control.addOrReplaceChild("right_blue_fin", CubeListBuilder.create().texOffs(24, 0).addBox(-2.0F, 0.0F, -0.99F, 2.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.0F, -3.5F, -2.0F));

        swim_control.addOrReplaceChild("spikes_front_top", CubeListBuilder.create().texOffs(15, 17).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, -4.0F, 0.7854F, 0.0F, 0.0F));

        swim_control.addOrReplaceChild("spikes_middle_top", CubeListBuilder.create().texOffs(14, 16).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -4.5F, 0.0F));

        swim_control.addOrReplaceChild("spikes_back_top", CubeListBuilder.create().texOffs(23, 18).addBox(-4.0F, -1.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -4.5F, 4.0F, -0.7854F, 0.0F, 0.0F));

        swim_control.addOrReplaceChild("spikes_front_left", CubeListBuilder.create().texOffs(1, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.5F, -4.0F, 0.0F, 0.7854F, 0.0F));

        swim_control.addOrReplaceChild("spikes_front_right", CubeListBuilder.create().texOffs(5, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.5F, -4.0F, 0.0F, -0.7854F, 0.0F));

        swim_control.addOrReplaceChild("spikes_back_left", CubeListBuilder.create().texOffs(9, 17).addBox(0.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.5F, 4.0F, 0.0F, -0.7854F, 0.0F));

        swim_control.addOrReplaceChild("spikes_back_right", CubeListBuilder.create().texOffs(9, 17).addBox(-1.0F, -8.0F, 0.0F, 1.0F, 8.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.5F, 4.0F, 0.0F, 0.7854F, 0.0F));

        swim_control.addOrReplaceChild("spikes_front_bottom", CubeListBuilder.create().texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, -4.0F, -0.7854F, 0.0F, 0.0F));

        swim_control.addOrReplaceChild("spikes_middle_bottom", CubeListBuilder.create().texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 3.5F, 0.0F));

        swim_control.addOrReplaceChild("spikes_back_bottom", CubeListBuilder.create().texOffs(15, 20).addBox(-4.0F, 0.0F, 0.0F, 8.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 3.5F, 4.0F, 0.7854F, 0.0F, 0.0F));

        cir.setReturnValue(LayerDefinition.create(meshdefinition, 32, 32));
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ci.cancel();
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.right_blue_fin.zRot = -0.2F + 0.4F * Mth.sin(ageInTicks * 0.2F);
        this.left_blue_fin.zRot = 0.2F - 0.4F * Mth.sin(ageInTicks * 0.2F);

        float prevOnLandProgress = ((AbstractFishAccess) entity).getPrevOnLandProgress();
        float onLandProgress = ((AbstractFishAccess) entity).getOnLandProgress();
        float partialTicks = ageInTicks - entity.tickCount;
        float landProgress = prevOnLandProgress + (onLandProgress - prevOnLandProgress) * partialTicks;

        this.swim_control.xRot = headPitch * (Mth.DEG_TO_RAD);
        this.swim_control.zRot += landProgress * ((float) Math.toRadians(-90) / 5F);
    }
}
