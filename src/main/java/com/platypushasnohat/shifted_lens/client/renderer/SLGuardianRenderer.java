package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGuardianModel;
import com.platypushasnohat.shifted_lens.entities.SLGuardian;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class SLGuardianRenderer extends MobRenderer<SLGuardian, SLGuardianModel<SLGuardian>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/guardian.png");

    public SLGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, new SLGuardianModel<>(context.bakeLayer(SLModelLayers.GUARDIAN)), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(SLGuardian entity) {
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(SLGuardian entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutoutNoCull(TEXTURE);
    }

    @Override
    public void render(SLGuardian entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);

        LivingEntity target = entity.getActiveAttackTarget();
        if (target != null) {
            GuardianBeamRenderer.render(entity, target, poseStack, buffer, partialTicks);
        }
    }

    @Override
    public boolean shouldRender(SLGuardian entity, Frustum camera, double x, double y, double z) {
        if (super.shouldRender(entity, camera, x, y, z)) {
            return true;
        } else {
            if (entity.hasActiveAttackTarget()) {
                LivingEntity livingentity = entity.getActiveAttackTarget();
                if (livingentity != null) {
                    Vec3 vec3 = this.getPosition(livingentity, (double) livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vec3 vec31 = this.getPosition(entity, entity.getEyeHeight(), 1.0F);
                    return camera.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
                }
            }
            return false;
        }
    }

    private Vec3 getPosition(LivingEntity entity, double y, float pos) {
        double d0 = Mth.lerp(pos, entity.xOld, entity.getX());
        double d1 = Mth.lerp(pos, entity.yOld, entity.getY()) + y;
        double d2 = Mth.lerp(pos, entity.zOld, entity.getZ());
        return new Vec3(d0, d1, d2);
    }
}
