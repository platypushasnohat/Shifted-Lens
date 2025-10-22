package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.PufferfishSmallModel;
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
@Mixin(PufferfishSmallModel.class)
public abstract class PufferfishSmallModelMixin<T extends Entity> extends HierarchicalModel<T> {

    @Unique
    private ModelPart swim_control;
    @Unique
    private ModelPart body;
    @Unique
    private ModelPart back_fin;
    @Unique
    private ModelPart right_fin;
    @Unique
    private ModelPart left_fin;
    @Unique
    private ModelPart right_eye;
    @Unique
    private ModelPart left_eye;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ModelPart root, CallbackInfo ci) {
        this.swim_control = root.getChild("swim_control");
        this.body = this.swim_control.getChild("body");
        this.back_fin = this.swim_control.getChild("back_fin");
        this.right_fin = this.swim_control.getChild("right_fin");
        this.left_fin = this.swim_control.getChild("left_fin");
        this.right_eye = this.swim_control.getChild("right_eye");
        this.left_eye = this.swim_control.getChild("left_eye");
    }

    @Inject(method = "createBodyLayer", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> cir) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition swim_control = partdefinition.addOrReplaceChild("swim_control", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_fin", CubeListBuilder.create(), PartPose.offset(-1.5F, 0.0F, -1.5F));
        partdefinition.addOrReplaceChild("left_fin", CubeListBuilder.create(), PartPose.offset(1.5F, 0.0F, -1.5F));

        swim_control.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 27).addBox(-1.5F, -2.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, 0.0F));

        swim_control.addOrReplaceChild("back_fin", CubeListBuilder.create().texOffs(-3, 0).addBox(-1.5F, 0.0F, 0.0F, 3.0F, 0.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.5F));

        swim_control.addOrReplaceChild("right_fin", CubeListBuilder.create().texOffs(25, 0).addBox(-1.0F, 0.0F, 0.01F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.5F, 0.0F, -1.5F));

        swim_control.addOrReplaceChild("left_fin", CubeListBuilder.create().texOffs(25, 0).addBox(0.0F, 0.0F, 0.01F, 1.0F, 0.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(1.5F, 0.0F, -1.5F));

        swim_control.addOrReplaceChild("right_eye", CubeListBuilder.create().texOffs(28, 6).addBox(0.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));

        swim_control.addOrReplaceChild("left_eye", CubeListBuilder.create().texOffs(24, 6).addBox(-1.5F, 0.0F, -1.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.0F, 0.0F));
        cir.setReturnValue(LayerDefinition.create(meshdefinition, 32, 32));
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        ci.cancel();
        this.root().getAllParts().forEach(ModelPart::resetPose);

        this.right_fin.zRot = -0.2F + 0.4F * Mth.sin(ageInTicks * 0.2F);
        this.left_fin.zRot = 0.2F - 0.4F * Mth.sin(ageInTicks * 0.2F);

        float prevOnLandProgress = ((AbstractFishAccess) entity).shiftedLens$getPrevOnLandProgress();
        float onLandProgress = ((AbstractFishAccess) entity).shiftedLens$getOnLandProgress();
        float partialTicks = ageInTicks - entity.tickCount;
        float landProgress = prevOnLandProgress + (onLandProgress - prevOnLandProgress) * partialTicks;

        this.swim_control.xRot = headPitch * (Mth.DEG_TO_RAD);
        this.swim_control.zRot += landProgress * ((float) Math.toRadians(-90) / 5F);
    }
}
