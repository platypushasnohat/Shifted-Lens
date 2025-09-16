package com.platypushasnohat.shifted_lens.client.renderer;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.BaitfishModel;
import com.platypushasnohat.shifted_lens.entities.Baitfish;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class BaitfishRenderer extends MobRenderer<Baitfish, BaitfishModel> {

    private static final ResourceLocation TEXTURE_RIVER = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/baitfish/baitfish.png");
    private static final ResourceLocation TEXTURE_OCEAN = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/baitfish/baitfish.png");

    public BaitfishRenderer(EntityRendererProvider.Context context) {
        super(context, new BaitfishModel(context.bakeLayer(SLModelLayers.BAITFISH)), 0.2F);
    }

    @Override
    public ResourceLocation getTextureLocation(Baitfish entity) {
        if (entity.getVariant() == 1) return TEXTURE_OCEAN;
        else return TEXTURE_RIVER;
    }

    @Override
    protected @Nullable RenderType getRenderType(Baitfish entity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityCutout(getTextureLocation(entity));
    }
}
