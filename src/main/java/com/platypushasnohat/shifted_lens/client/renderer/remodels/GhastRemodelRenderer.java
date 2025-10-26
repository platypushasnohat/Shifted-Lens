package com.platypushasnohat.shifted_lens.client.renderer.remodels;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import com.platypushasnohat.shifted_lens.client.renderer.layers.GhastGlowLayer;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import static com.platypushasnohat.shifted_lens.ShiftedLens.modPrefix;

@OnlyIn(Dist.CLIENT)
public class GhastRemodelRenderer extends MobRenderer<Ghast, SLGhastModel> {

    private static final ResourceLocation GHAST = modPrefix("textures/entity/ghast/ghast.png");
    private static final ResourceLocation RETRO_GHAST = modPrefix("textures/entity/ghast/retro_ghast.png");

    public GhastRemodelRenderer(EntityRendererProvider.Context context) {
        super(context, new SLGhastModel(context.bakeLayer(SLModelLayers.GHAST)), 1.5F);
        this.addLayer(new GhastGlowLayer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull Ghast entity) {
        return ShiftedLensConfig.RETRO_GHAST.get() ? RETRO_GHAST : GHAST;
    }
}
