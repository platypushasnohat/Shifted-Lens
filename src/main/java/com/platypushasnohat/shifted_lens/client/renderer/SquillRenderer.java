package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SquillModel;
import com.platypushasnohat.shifted_lens.entities.Squill;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

@OnlyIn(Dist.CLIENT)
public class SquillRenderer extends MobRenderer<Squill, SquillModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squill/squill.png");
    private static final ResourceLocation TEXTURE_AGGRO = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squill/squill_aggro.png");

    public SquillRenderer(EntityRendererProvider.Context context) {
        super(context, new SquillModel(context.bakeLayer(SLModelLayers.SQUILL)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Squill entity) {
        if (entity.isAttacking()) return TEXTURE_AGGRO;
        else return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(Squill entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(entity));
    }

    @Override
    protected void scale(Squill entity, PoseStack poseStack, float partialTicks) {
        float alpha = 1.0F - (0.25F + 0.25F * (float) Math.sin(entity.tickCount * 0.02F)) * entity.getAlphaProgress(partialTicks);
        this.model.setAlpha(alpha);
    }

    @Override
    protected void setupRotations(Squill entity, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks) {
        Vec3 pull = entity.getPull(partialTicks);
        double pulledX = Mth.lerp(partialTicks, entity.xo, entity.getX()) - pull.x();
        double pulledY = Mth.lerp(partialTicks, entity.yo, entity.getY()) - pull.y();
        double pulledZ = Mth.lerp(partialTicks, entity.zo, entity.getZ()) - pull.z();

        Vector3f lookDir = new Vector3f((float) pulledX, (float) pulledY, (float) pulledZ);
        if (lookDir.lengthSquared() < 1e-6F) {
            lookDir.set(0.0F, 0.0F, 1.0F);
        } else {
            lookDir.normalize();
        }

        Vector3f defaultForward = new Vector3f(0.0F, 0.0F, -1.0F);
        Quaternionf rotation = new Quaternionf().rotationTo(defaultForward, lookDir);
        Quaternionf correction = new Quaternionf().rotateX(-Mth.HALF_PI);
        rotation.mul(correction);

        float rotationOffset = 0.2F;
        poseStack.translate(0.0F, rotationOffset, 0.0F);
        poseStack.mulPose(rotation);
        poseStack.translate(0.0F, -rotationOffset, 0.0F);

        if (entity.hasCustomName()) {
            String name = ChatFormatting.stripFormatting(entity.getName().getString());
            if (name.equals("Dinnerbone") || name.equals("Grumm")) {
                poseStack.translate(0.0D, entity.getBbHeight() + 0.1D, 0.0D);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
        }

        if (entity.isAttacking()) {
            poseStack.translate(0.0D, entity.getBbHeight() + 0.1D, 0.0D);
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
        }
    }
}
