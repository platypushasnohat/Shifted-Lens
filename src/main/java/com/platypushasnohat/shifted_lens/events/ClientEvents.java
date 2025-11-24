package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.*;
import com.platypushasnohat.shifted_lens.client.renderer.*;
import com.platypushasnohat.shifted_lens.client.renderer.remodels.*;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.RabbitModel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void registerVanillaEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.COD, SLCodRenderer::new);
        event.registerEntityRenderer(EntityType.ELDER_GUARDIAN, SLElderGuardianRenderer::new);
        event.registerEntityRenderer(EntityType.GHAST, SLGhastRenderer::new);
        event.registerEntityRenderer(EntityType.GLOW_SQUID, SLGlowSquidRenderer::new);
        event.registerEntityRenderer(EntityType.GUARDIAN, SLGuardianRenderer::new);
        event.registerEntityRenderer(EntityType.SALMON, SLSalmonRenderer::new);
        event.registerEntityRenderer(EntityType.SQUID, SLSquidRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SLModelLayers.COD, SLCodModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.ELDER_GUARDIAN, SLElderGuardianModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.GHAST, SLGhastModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.GLOW_SQUID, SLSquidModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.GUARDIAN, SLGuardianModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.RABBIT, RabbitModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.SALMON, SLSalmonModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.SQUID, SLSquidModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> key = event.getTabKey();
        MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();

        if (key == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            entries.putAfter(new ItemStack(Items.PUFFERFISH_BUCKET), new ItemStack(SLItems.SQUID_BUCKET.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
