package com.platypushasnohat.shifted_lens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SLClientConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> RETRO_GHAST;
    public static ForgeConfigSpec.ConfigValue<Boolean> CLEAN_GUARDIANS;

    public static final ForgeConfigSpec COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("misc");
        RETRO_GHAST = builder.comment("Whether or not Ghasts have a more vanilla faithful texture").define("retroGhast", false);
        CLEAN_GUARDIANS = builder.comment("Whether or not Guardians have a more clean looking texture").define("cleanGuardians", false);
        builder.pop();

        COMMON = builder.build();
    }
}
