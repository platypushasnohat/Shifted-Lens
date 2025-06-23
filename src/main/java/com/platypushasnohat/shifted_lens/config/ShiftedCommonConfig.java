package com.platypushasnohat.shifted_lens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShiftedCommonConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_GHAST;

    public static final ForgeConfigSpec COMMON;

    static {
        ForgeConfigSpec.Builder CONFIG_BUILDER = new ForgeConfigSpec.Builder();
        CONFIG_BUILDER.comment("Shifted Lens Configs").push("shifted_lens_common_config");

        CONFIG_BUILDER.comment("Replace Mobs").push("mob_config");

        REPLACE_GHAST = CONFIG_BUILDER.comment("Replace Ghast").define("replace_ghast", true);

        COMMON = CONFIG_BUILDER.build();
    }
}
