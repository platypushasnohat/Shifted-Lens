package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.AnchovyModel;
import com.platypushasnohat.shifted_lens.entities.Anchovy;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class AnchovyRenderer extends MobRenderer<Anchovy, AnchovyModel<Anchovy>> {

    private static final ResourceLocation TEXTURE_RIVER = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/anchovy/anchovy.png");
    private static final ResourceLocation TEXTURE_OCEAN = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/anchovy/anchovy.png");

    public AnchovyRenderer(EntityRendererProvider.Context context) {
        super(context, new AnchovyModel<>(context.bakeLayer(SLModelLayers.ANCHOVY)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(Anchovy entity) {
        if (entity.getVariant() == 1) return TEXTURE_OCEAN;
        else return TEXTURE_RIVER;
    }

    @Override
    protected @Nullable RenderType getRenderType(Anchovy entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutout(getTextureLocation(entity));
    }

    @Override
    public void setupRotations(Anchovy entity, PoseStack matrixStack, float f, float g, float h) {
        super.setupRotations(entity, matrixStack, f, g, h);
        matrixStack.scale(0.75F, 0.75F, 0.75F);

        if (!entity.isInWater()) {
            matrixStack.translate(0, 0.1, 0);
            matrixStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
        }
    }
}
