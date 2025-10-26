package com.platypushasnohat.shifted_lens.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class GuardianBeamRenderer {

    public static final RenderType BEAM_RENDER = RenderType.entityCutoutNoCull(new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/beam.png"));

    private static void vertex(VertexConsumer consumer, Matrix4f matrix4f, Matrix3f matrix3f, float f, float g, float h, int red, int green, int blue, float k, float l) {
        consumer.vertex(matrix4f, f, g, h).color(red, green, blue, 255).uv(k, l).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void render(Guardian guardian, Entity target, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource) {
        float f = guardian.getAttackAnimationScale(partialTicks);
        float f1 = guardian.getClientSideAttackTime() + partialTicks;
        float f2 = f1 * 0.5F % 1.0F;
        float f3 = guardian.getEyeHeight();
        poseStack.pushPose();
        poseStack.translate(0.0F, f3, 0.0F);
        Vec3 vec3 = getPosition(target, (double) target.getBbHeight() * 0.5D, partialTicks);
        Vec3 vec31 = getPosition(guardian, f3, partialTicks);
        Vec3 vec32 = vec3.subtract(vec31);
        float f4 = (float)(vec32.length() + 1.0D);
        vec32 = vec32.normalize();
        float f5 = (float) Math.acos(vec32.y);
        float f6 = (float) Math.atan2(vec32.z, vec32.x);
        poseStack.mulPose(Axis.YP.rotationDegrees((((float) Math.PI / 2F) - f6) * (180F / (float) Math.PI)));
        poseStack.mulPose(Axis.XP.rotationDegrees(f5 * (180F / (float) Math.PI)));
        float f7 = f1 * 0.05F * -1.5F;
        float f8 = f * f;
        int j = 64 + (int)(f8 * 191.0F);
        int k = 32 + (int)(f8 * 191.0F);
        int l = 128 - (int)(f8 * 64.0F);
        float f11 = Mth.cos(f7 + 2.3561945F) * 0.282F;
        float f12 = Mth.sin(f7 + 2.3561945F) * 0.282F;
        float f13 = Mth.cos(f7 + ((float) Math.PI / 4F)) * 0.282F;
        float f14 = Mth.sin(f7 + ((float) Math.PI / 4F)) * 0.282F;
        float f15 = Mth.cos(f7 + 3.926991F) * 0.282F;
        float f16 = Mth.sin(f7 + 3.926991F) * 0.282F;
        float f17 = Mth.cos(f7 + 5.4977875F) * 0.282F;
        float f18 = Mth.sin(f7 + 5.4977875F) * 0.282F;
        float f19 = Mth.cos(f7 + (float) Math.PI) * 0.2F;
        float f20 = Mth.sin(f7 + (float) Math.PI) * 0.2F;
        float f21 = Mth.cos(f7 + 0.0F) * 0.2F;
        float f22 = Mth.sin(f7 + 0.0F) * 0.2F;
        float f23 = Mth.cos(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f24 = Mth.sin(f7 + ((float) Math.PI / 2F)) * 0.2F;
        float f25 = Mth.cos(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f26 = Mth.sin(f7 + ((float) Math.PI * 1.5F)) * 0.2F;
        float f29 = -1.0F + f2;
        float f30 = f4 * 2.5F + f29;
        VertexConsumer vertexconsumer = bufferSource.getBuffer(BEAM_RENDER);
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        vertex(vertexconsumer, matrix4f, matrix3f, f19, f4, f20, j, k, l, 0.4999F, f30);
        vertex(vertexconsumer, matrix4f, matrix3f, f19, 0.0F, f20, j, k, l, 0.4999F, f29);
        vertex(vertexconsumer, matrix4f, matrix3f, f21, 0.0F, f22, j, k, l, 0.0F, f29);
        vertex(vertexconsumer, matrix4f, matrix3f, f21, f4, f22, j, k, l, 0.0F, f30);
        vertex(vertexconsumer, matrix4f, matrix3f, f23, f4, f24, j, k, l, 0.4999F, f30);
        vertex(vertexconsumer, matrix4f, matrix3f, f23, 0.0F, f24, j, k, l, 0.4999F, f29);
        vertex(vertexconsumer, matrix4f, matrix3f, f25, 0.0F, f26, j, k, l, 0.0F, f29);
        vertex(vertexconsumer, matrix4f, matrix3f, f25, f4, f26, j, k, l, 0.0F, f30);
        float f31 = 0.0F;
        if (guardian.tickCount % 2 == 0) {
            f31 = 0.5F;
        }
        vertex(vertexconsumer, matrix4f, matrix3f, f11, f4, f12, j, k, l, 0.5F, f31 + 0.5F);
        vertex(vertexconsumer, matrix4f, matrix3f, f13, f4, f14, j, k, l, 1.0F, f31 + 0.5F);
        vertex(vertexconsumer, matrix4f, matrix3f, f17, f4, f18, j, k, l, 1.0F, f31);
        vertex(vertexconsumer, matrix4f, matrix3f, f15, f4, f16, j, k, l, 0.5F, f31);
        poseStack.popPose();
    }

    private static Vec3 getPosition(Entity LivingEntityIn, double y, float vec) {
        double d0 = LivingEntityIn.xOld + (LivingEntityIn.getX() - LivingEntityIn.xOld) * (double) vec;
        double d1 = y + LivingEntityIn.yOld + (LivingEntityIn.getY() - LivingEntityIn.yOld) * (double) vec;
        double d2 = LivingEntityIn.zOld + (LivingEntityIn.getZ() - LivingEntityIn.zOld) * (double) vec;
        return new Vec3(d0, d1, d2);
    }
}
