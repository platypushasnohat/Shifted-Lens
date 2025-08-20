package com.platypushasnohat.shifted_lens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SLCommonConfig {

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_GHAST;

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_GUARDIAN;
    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_ELDER_GUARDIAN;

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_COD;
    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_SALMON;
    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_TROPICAL_FISH;

    public static ForgeConfigSpec.ConfigValue<Boolean> REPLACE_SQUID;

    public static ForgeConfigSpec.ConfigValue<Integer> SQUILL_SPAWN_HEIGHT;

    public static final ForgeConfigSpec COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("mob-spawning");
        REPLACE_GHAST = builder.comment("Replace Vanilla Ghast").define("replace-ghast", true);

        REPLACE_GUARDIAN = builder.comment("Replace Vanilla Guardian").define("replace-guardian", true);
        REPLACE_ELDER_GUARDIAN = builder.comment("Replace Vanilla Elder Guardian").define("replace-elder-guardian", true);

        REPLACE_COD = builder.comment("Replace Vanilla Cod").define("replace-cod", true);
        REPLACE_SALMON = builder.comment("Replace Vanilla Salmon").define("replace-salmon", true);
        REPLACE_TROPICAL_FISH = builder.comment("Replace Vanilla Tropical Fish").define("replace-tropical-fish", true);

        REPLACE_SQUID = builder.comment("Replace Vanilla Squid").define("replace-squid", true);

        SQUILL_SPAWN_HEIGHT = builder.comment("Height for Squill to spawn at").define("squill-spawn-height", 128);
        builder.pop();

        COMMON = builder.build();
    }
}
