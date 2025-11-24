package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.TropicalFishModelA;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
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

@SuppressWarnings("unused, FieldCanBeLocal")
@OnlyIn(Dist.CLIENT)
@Mixin(TropicalFishModelA.class)
public abstract class TropicalFishModelAMixin<T extends Entity> extends ColorableHierarchicalModel<T> {

    @Unique
    private ModelPart tailFin;
    @Unique
    private ModelPart leftFin;
    @Unique
    private ModelPart rightFin;
    @Unique
    private ModelPart swim_control;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ModelPart modelPart, CallbackInfo ci) {
        this.swim_control = modelPart.getChild("swim_control");
        this.leftFin = this.swim_control.getChild("left_fin");
        this.rightFin = this.swim_control.getChild("right_fin");
        this.tailFin = this.swim_control.getChild("tail_fin");
    }

    @Inject(method = "createBodyLayer", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> callbackInfo) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition swim_control = partdefinition.addOrReplaceChild("swim_control", CubeListBuilder.create(), PartPose.offset(0.0F, 22.5F, 0.0F));

        swim_control.addOrReplaceChild("left_fin", CubeListBuilder.create()
                .texOffs(2, 12).addBox(0.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(1.0F, 1.5F, 0.0F, 0.0F, -0.7854F, 0.0F));

        swim_control.addOrReplaceChild("right_fin", CubeListBuilder.create()
                .texOffs(2, 16).addBox(-2.0F, -2.0F, 0.0F, 2.0F, 2.0F, 0.0F), PartPose.offsetAndRotation(-1.0F, 1.5F, 0.0F, 0.0F, 0.7854F, 0.0F));

        swim_control.addOrReplaceChild("tail_fin", CubeListBuilder.create()
                .texOffs(24, -4).addBox(0.0F, -1.5F, 0.0F, 0.0F, 3.0F, 4.0F), PartPose.offset(0.0F, 0.0F, 3.0F));

        swim_control.addOrReplaceChild("top_fin", CubeListBuilder.create()
                .texOffs(10, -6).addBox(0.0F, -4.0F, 0.0F, 0.0F, 4.0F, 6.0F), PartPose.offset(0.0F, -1.5F, -3.0F));

        swim_control.addOrReplaceChild("body", CubeListBuilder.create()
                .texOffs(0, 0).addBox(-1.0F, -1.5F, -3.0F, 2.0F, 3.0F, 6.0F), PartPose.offset(0.0F, 0.0F, 0.0F));

        partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        callbackInfo.setReturnValue(LayerDefinition.create(meshdefinition, 32, 32));
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    private void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (ShiftedLensConfig.TROPICAL_FISH_REVAMP.get()) {
            this.root().getAllParts().forEach(ModelPart::resetPose);

            ci.cancel();
            float f = 1.0F;
            if (!entity.isInWater()) {
                f = 1.5F;
            }
            this.tailFin.yRot = -f * 0.45F * Mth.sin(0.6F * ageInTicks);

            float prevOnLandProgress = ((AbstractFishAccess) entity).shiftedLens$getPrevOnLandProgress();
            float onLandProgress = ((AbstractFishAccess) entity).shiftedLens$getOnLandProgress();
            float partialTicks = ageInTicks - entity.tickCount;
            float landProgress = prevOnLandProgress + (onLandProgress - prevOnLandProgress) * partialTicks;

            this.swim_control.xRot = headPitch * (Mth.DEG_TO_RAD);
            this.swim_control.zRot += landProgress * ((float) Math.toRadians(-90) / 5F);
        }
    }
}
