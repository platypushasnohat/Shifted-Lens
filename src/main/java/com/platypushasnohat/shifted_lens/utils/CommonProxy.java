package com.platypushasnohat.shifted_lens.utils;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {
    public void init() {
    }

    public void clientInit() {
    }

    public Object getArmorRenderProperties() {
        return null;
    }

    public Object getISTERProperties() {
        return null;
    }
}
