package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.*;
import com.platypushasnohat.shifted_lens.client.models.armor.WhirlicapModel;
import com.platypushasnohat.shifted_lens.client.particles.WhirlibubbleParticle;
import com.platypushasnohat.shifted_lens.client.particles.WhirliwindParticle;
import com.platypushasnohat.shifted_lens.client.renderer.*;
import com.platypushasnohat.shifted_lens.client.renderer.blockentity.WhirligigRenderer;
import com.platypushasnohat.shifted_lens.registry.SLBlockEntities;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import com.platypushasnohat.shifted_lens.registry.SLParticles;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class ClientEvents {

    @SubscribeEvent
    public static void registerParticles(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(SLParticles.WHIRLIWIND.get(), WhirliwindParticle.Factory::new);
        event.registerSpriteSet(SLParticles.WHIRLIBUBBLE.get(), WhirlibubbleParticle.Factory::new);
    }

    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(SLEntities.BAITFISH.get(), BaitfishRenderer::new);
        event.registerEntityRenderer(SLEntities.FLYING_FISH.get(), FlyingFishRenderer::new);
        event.registerEntityRenderer(EntityType.SQUID, SLSquidRenderer::new);
        event.registerEntityRenderer(SLEntities.SQUILL.get(), SquillRenderer::new);
        event.registerEntityRenderer(SLEntities.TOOTHED_SNOWBALL.get(), ThrownItemRenderer::new);
    }

    @SubscribeEvent
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
    public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(SLBlockEntities.WHIRLIGIG_BLOCK_ENTITY.get(), WhirligigRenderer::new);
    }

    @SubscribeEvent
    public static void registerEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SLModelLayers.BAITFISH, BaitfishModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.COD, SLCodModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.ELDER_GUARDIAN, SLElderGuardianModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.FLYING_FISH, FlyingFishModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.GHAST, SLGhastModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.GUARDIAN, SLGuardianModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.SALMON, SLSalmonModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.SQUID, SLSquidModel::createBodyLayer);
        event.registerLayerDefinition(SLModelLayers.SQUILL, SquillModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerArmorLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SLModelLayers.WHIRLICAP, WhirlicapModel::createArmorLayer);
    }

    @SubscribeEvent
    public static void registerBlockEntityLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(SLModelLayers.WHIRLIGIG, WhirligigRenderer::createMesh);
    }
}
