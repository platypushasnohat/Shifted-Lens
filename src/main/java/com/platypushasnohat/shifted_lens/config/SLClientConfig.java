package com.platypushasnohat.shifted_lens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SLClientConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> RETRO_GHAST;

    public static final ForgeConfigSpec COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("misc");
        RETRO_GHAST = builder.comment("Whether or not Ghasts have a more vanilla faithful texture").define("retro-ghast", true);
        builder.pop();

        COMMON = builder.build();
    }
}
