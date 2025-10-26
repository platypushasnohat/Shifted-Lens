package com.platypushasnohat.shifted_lens.client.renderer.remodels;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.models.SLElderGuardianModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.GuardianBeamRenderer;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ElderGuardianRemodelRenderer extends MobRenderer<ElderGuardian, SLElderGuardianModel> {

    public static final ResourceLocation ELDER_GUARDIAN = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/elder_guardian.png");
    public static final ResourceLocation STAINLESS_ELDER_GUARDIAN = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/stainless_elder_guardian.png");

    public ElderGuardianRemodelRenderer(EntityRendererProvider.Context context) {
        super(context, new SLElderGuardianModel(context.bakeLayer(SLModelLayers.ELDER_GUARDIAN)), 1.2F);
    }

    @Override
    public void render(ElderGuardian entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, packedLight);
        LivingEntity target = entity.getActiveAttackTarget();
        if (target != null) {
            GuardianBeamRenderer.render(entity, target, partialTicks, poseStack, bufferSource);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ElderGuardian entity) {
        return ShiftedLensConfig.STAINLESS_GUARDIANS.get() ? STAINLESS_ELDER_GUARDIAN : ELDER_GUARDIAN;
    }
}
