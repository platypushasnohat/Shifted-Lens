package com.platypushasnohat.shifted_lens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SLCommonConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_GHAST;

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_GUARDIAN;
    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_ELDER_GUARDIAN;

    public static final ForgeConfigSpec COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("mob-spawning");
        REPLACE_GHAST = builder.comment("Replace Vanilla Ghast").define("replace-ghast", true);

        REPLACE_GUARDIAN = builder.comment("Replace Vanilla Guardian").define("replace-guardian", true);
        REPLACE_ELDER_GUARDIAN = builder.comment("Replace Vanilla Elder Guardian").define("replace-elder-guardian", true);
        builder.pop();

        COMMON = builder.build();
    }
}
