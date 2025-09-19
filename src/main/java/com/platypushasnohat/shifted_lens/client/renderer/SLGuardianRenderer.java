package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.models.SLGuardianModel;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SLGuardianRenderer extends MobRenderer<Guardian, SLGuardianModel> {

    private static final ResourceLocation GUARDIAN = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/guardian.png");
    private static final ResourceLocation STAINLESS_GUARDIAN = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/stainless_guardian.png");

    public SLGuardianRenderer(EntityRendererProvider.Context context) {
        super(context, new SLGuardianModel(context.bakeLayer(SLModelLayers.GUARDIAN)), 0.5F);
    }

    @Override
    public void render(Guardian entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        LivingEntity target = entity.getActiveAttackTarget();
        if (target != null) {
            GuardianBeamRenderer.render(entity, target, poseStack, bufferSource, partialTicks);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(Guardian entity) {
        return ShiftedLensConfig.STAINLESS_GUARDIANS.get() ? STAINLESS_GUARDIAN : GUARDIAN;
    }
}
