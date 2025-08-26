package com.platypushasnohat.shifted_lens.utils;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.renderer.blockentity.WhirligigRenderer;
import com.platypushasnohat.shifted_lens.client.renderer.items.SLArmorRenderProperties;
import com.platypushasnohat.shifted_lens.registry.SLBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public void init() {
    }

    public void clientInit() {
        BlockEntityRenderers.register(SLBlockEntities.WHIRLIGIG_BLOCK_ENTITY.get(), WhirligigRenderer::new);
    }

    @Override
    public Object getArmorRenderProperties() {
        return new SLArmorRenderProperties();
    }
}
