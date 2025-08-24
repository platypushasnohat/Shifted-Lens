package com.platypushasnohat.shifted_lens.utils;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.renderer.items.SLArmorRenderProperties;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.Mod;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public void init() {
    }

    public void clientInit() {
    }

    @Override
    public Object getArmorRenderProperties() {
        return new SLArmorRenderProperties();
    }
}
