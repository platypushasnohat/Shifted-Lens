package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.mixin_utils.AbstractFishAccess;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.TadpoleModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused, FieldCanBeLocal")
@OnlyIn(Dist.CLIENT)
@Mixin(TadpoleModel.class)
public abstract class TadpoleModelMixin<T extends Tadpole> extends AgeableListModel<T> {

    @Shadow
    @Final
    private ModelPart root;

    @Unique
    private ModelPart swim_control;
    @Unique
    private ModelPart body;
    @Unique
    private ModelPart tail;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void init(ModelPart root, CallbackInfo ci) {
        this.swim_control = root.getChild("swim_control");
        this.body = this.swim_control.getChild("body");
        this.tail = this.swim_control.getChild("tail");
    }

    @Inject(method = "createBodyLayer", at = @At("HEAD"), cancellable = true)
    private static void createBodyLayer(CallbackInfoReturnable<LayerDefinition> cir) {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition tail = partdefinition.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, 0.0F));
        PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 22.0F, -3.0F));

        PartDefinition swim_control = partdefinition.addOrReplaceChild("swim_control", CubeListBuilder.create(), PartPose.offset(0.0F, 23.0F, 0.0F));

        swim_control.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -1.0F, 0.0F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, -1.5F));
        swim_control.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(0, 0).addBox(0.0F, -1.0F, 0.0F, 0.0F, 2.0F, 7.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 1.5F));

        cir.setReturnValue(LayerDefinition.create(meshdefinition, 16, 16));
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/Entity;FFFFF)V", at = @At("HEAD"), cancellable = true)
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        if (ShiftedLensConfig.TADPOLE_TILTING.get()) {
            ci.cancel();
            this.swim_control.getAllParts().forEach(ModelPart::resetPose);

            float f = entity.isInWater() ? 1.0F : 1.5F;
            this.tail.yRot = -f * 0.25F * Mth.sin(0.3F * ageInTicks);

            float prevOnLandProgress = ((AbstractFishAccess) entity).shiftedLens$getPrevOnLandProgress();
            float onLandProgress = ((AbstractFishAccess) entity).shiftedLens$getOnLandProgress();
            float partialTicks = ageInTicks - entity.tickCount;
            float landProgress = prevOnLandProgress + (onLandProgress - prevOnLandProgress) * partialTicks;

            this.swim_control.xRot = headPitch * (Mth.DEG_TO_RAD);
            this.swim_control.zRot += landProgress * ((float) Math.toRadians(-90) / 5F);
        }
    }
}
