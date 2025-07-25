package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLElderGuardianModel;
import com.platypushasnohat.shifted_lens.client.models.SLGhastModel;
import com.platypushasnohat.shifted_lens.client.models.SLGuardianModel;
import com.platypushasnohat.shifted_lens.client.models.SteelChariotModel;
import com.platypushasnohat.shifted_lens.client.renderer.SLElderGuardianRenderer;
import com.platypushasnohat.shifted_lens.client.renderer.SLGhastRenderer;
import com.platypushasnohat.shifted_lens.client.renderer.SLGuardianRenderer;
import com.platypushasnohat.shifted_lens.client.renderer.SteelChariotRenderer;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SLEntities.GHAST.get(), SLGhastRenderer::new);
        event.registerEntityRenderer(SLEntities.GHAST_FIREBALL.get(), (context) -> new ThrownItemRenderer<>(context, 3.0F, true));

        event.registerEntityRenderer(SLEntities.GUARDIAN.get(), SLGuardianRenderer::new);
        event.registerEntityRenderer(SLEntities.ELDER_GUARDIAN.get(), SLElderGuardianRenderer::new);

        event.registerEntityRenderer(SLEntities.STEEL_CHARIOT.get(), SteelChariotRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SLModelLayers.GHAST, SLGhastModel::createBodyLayer);

        event.registerLayerDefinition(SLModelLayers.GUARDIAN, SLGuardianModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.ELDER_GUARDIAN, SLElderGuardianModel::createBodyLayer);

        event.registerLayerDefinition(SLModelLayers.STEEL_CHARIOT, SteelChariotModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerCreativeModeTab(BuildCreativeModeTabContentsEvent event) {
        ResourceKey<CreativeModeTab> tabKey = event.getTabKey();
        MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries = event.getEntries();
        if (tabKey == CreativeModeTabs.INGREDIENTS) {
            entries.putAfter(new ItemStack(Items.FIRE_CHARGE), new ItemStack(SLItems.INFERNO_CHARGE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.putAfter(new ItemStack(Items.GLOW_INK_SAC), new ItemStack(SLItems.INK_ENGINE.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            entries.putAfter(new ItemStack(SLItems.INK_ENGINE.get()), new ItemStack(SLItems.WHIRLIGIG.get()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }
}
