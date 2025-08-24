package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.FlyingFishModel;
import com.platypushasnohat.shifted_lens.entities.FlyingFish;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class FlyingFishRenderer extends MobRenderer<FlyingFish, FlyingFishModel<FlyingFish>> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/flying_fish/flying_fish.png");

    public FlyingFishRenderer(EntityRendererProvider.Context context) {
        super(context, new FlyingFishModel<>(context.bakeLayer(SLModelLayers.FLYING_FISH)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(FlyingFish entity) {
        return TEXTURE;
    }

    @Override
    protected @Nullable RenderType getRenderType(FlyingFish entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutout(getTextureLocation(entity));
    }
}
