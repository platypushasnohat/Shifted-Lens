package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.client.models.SLRabbitModel;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Rabbit;

public class SLRabbitRenderer extends MobRenderer<Rabbit, SLRabbitModel> {

    private static final ResourceLocation BROWN_RABBIT = new ResourceLocation("textures/entity/rabbit/brown.png");
    private static final ResourceLocation WHITE_RABBIT = new ResourceLocation("textures/entity/rabbit/white.png");
    private static final ResourceLocation BLACK_RABBIT = new ResourceLocation("textures/entity/rabbit/black.png");
    private static final ResourceLocation GOLD_RABBIT = new ResourceLocation("textures/entity/rabbit/gold.png");
    private static final ResourceLocation SALT_RABBIT = new ResourceLocation("textures/entity/rabbit/salt.png");
    private static final ResourceLocation WHITE_SPLOTCHED_RABBIT = new ResourceLocation("textures/entity/rabbit/white_splotched.png");
    private static final ResourceLocation TOAST = new ResourceLocation("textures/entity/rabbit/toast.png");
    private static final ResourceLocation KILLER_RABBIT = new ResourceLocation("textures/entity/rabbit/caerbannog.png");

    public SLRabbitRenderer(EntityRendererProvider.Context context) {
        super(context, new SLRabbitModel(context.bakeLayer(SLModelLayers.RABBIT)), 0.4F);
    }

    @Override
    public ResourceLocation getTextureLocation(Rabbit entity) {
        String s = ChatFormatting.stripFormatting(entity.getName().getString());
        if ("Toast".equals(s)) {
            return TOAST;
        } else {
            return switch (entity.getVariant()) {
                case BROWN -> BROWN_RABBIT;
                case WHITE -> WHITE_RABBIT;
                case BLACK -> BLACK_RABBIT;
                case GOLD -> GOLD_RABBIT;
                case SALT -> SALT_RABBIT;
                case WHITE_SPLOTCHED -> WHITE_SPLOTCHED_RABBIT;
                case EVIL -> KILLER_RABBIT;
            };
        }
    }
}
