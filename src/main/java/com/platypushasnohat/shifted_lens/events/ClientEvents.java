package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.*;
import com.platypushasnohat.shifted_lens.client.renderer.*;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SLEntities.ANCHOVY.get(), AnchovyRenderer::new);
        event.registerEntityRenderer(SLEntities.SQUILL.get(), SquillRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SLModelLayers.GHAST, SLGhastModel::createBodyLayer);

        event.registerLayerDefinition(SLModelLayers.GUARDIAN, SLGuardianModel::createBodyLayer);

        event.registerLayerDefinition(SLModelLayers.ANCHOVY, AnchovyModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.COD, SLCodModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.SALMON, SLSalmonModel::createBodyLayer);

        event.registerLayerDefinition(SLModelLayers.SQUILL, SquillModel::createBodyLayer);
    }
}
