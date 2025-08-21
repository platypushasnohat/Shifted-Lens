package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLGuardianModel;
import com.platypushasnohat.shifted_lens.client.renderer.GuardianBeamRenderer;
import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.GuardianRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@OnlyIn(Dist.CLIENT)
@Mixin(GuardianRenderer.class)
public abstract class GuardianRendererMixin extends MobRenderer<Guardian, SLGuardianModel<Guardian>> {

    @Shadow
    private static final ResourceLocation GUARDIAN_LOCATION = new ResourceLocation("textures/entity/guardian.png");

    @Shadow
    private static final ResourceLocation GUARDIAN_BEAM_LOCATION = new ResourceLocation("textures/entity/guardian_beam.png");

    @Unique
    private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/guardian.png");

    @Unique
    private SLGuardianModel<Guardian> shiftedLens$remodel;

    public GuardianRendererMixin(EntityRendererProvider.Context context, SLGuardianModel<Guardian> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "<init>(Lnet/minecraft/client/renderer/entity/EntityRendererProvider$Context;FLnet/minecraft/client/model/geom/ModelLayerLocation;)V", at = @At("TAIL"))
    private void GuardianRenderer(EntityRendererProvider.Context context, float p_174162_, ModelLayerLocation layerLocation, CallbackInfo ci) {
        this.shiftedLens$remodel = new SLGuardianModel<>(context.bakeLayer(SLModelLayers.GUARDIAN));
    }

    @Override
    public boolean shouldRender(Guardian guardian, Frustum frustum, double i, double j, double k) {
        if (super.shouldRender(guardian, frustum, i, j, k)) {
            return true;
        } else {
            if (guardian.hasActiveAttackTarget()) {
                LivingEntity livingentity = guardian.getActiveAttackTarget();
                if (livingentity != null) {
                    Vec3 vec3 = this.getPosition(livingentity, (double) livingentity.getBbHeight() * 0.5D, 1.0F);
                    Vec3 vec31 = this.getPosition(guardian, guardian.getEyeHeight(), 1.0F);
                    return frustum.isVisible(new AABB(vec31.x, vec31.y, vec31.z, vec3.x, vec3.y, vec3.z));
                }
            }
            return false;
        }
    }

    @Shadow
    private Vec3 getPosition(LivingEntity entity, double p_114804_, float p_114805_) {
        double d0 = Mth.lerp(p_114805_, entity.xOld, entity.getX());
        double d1 = Mth.lerp(p_114805_, entity.yOld, entity.getY()) + p_114804_;
        double d2 = Mth.lerp(p_114805_, entity.zOld, entity.getZ());
        return new Vec3(d0, d1, d2);
    }

    @Override
    public void render(Guardian guardian, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (SLConfig.REPLACE_GUARDIAN.get()) {
            this.model = this.shiftedLens$remodel;
        }

        super.render(guardian, entityYaw, partialTicks, poseStack, bufferSource, packedLight);

        LivingEntity target = guardian.getActiveAttackTarget();
        if (target != null) {
            GuardianBeamRenderer.render(guardian, target, poseStack, bufferSource, partialTicks);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Guardian guardian) {
        if (SLConfig.REPLACE_GUARDIAN.get()) {
            return GUARDIAN_TEXTURE;
        } else {
            return GUARDIAN_LOCATION;
        }
    }
}
