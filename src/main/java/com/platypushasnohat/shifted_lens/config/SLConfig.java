package com.platypushasnohat.shifted_lens.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class SLConfig {

    public static ForgeConfigSpec.ConfigValue<Integer> SQUILL_SPAWN_HEIGHT;

    public static ForgeConfigSpec.ConfigValue<Boolean> BETTER_FISH_FLOPPING;

    public static ForgeConfigSpec.ConfigValue<Boolean> MILKABLE_SQUIDS;

    public static final ForgeConfigSpec COMMON;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.push("spawning");
        SQUILL_SPAWN_HEIGHT = builder.comment("Height for Squill to spawn at").define("squill-spawn-height", 128);
        builder.pop();

        builder.push("tweaks");
        BETTER_FISH_FLOPPING = builder.comment("Whether or not fish flopping is more realistic").define("better-fish-flopping", true);
        MILKABLE_SQUIDS = builder.comment("Whether or squids can be milked with buckets").define("milkable-squids", true);
        builder.pop();

        COMMON = builder.build();
    }
}
