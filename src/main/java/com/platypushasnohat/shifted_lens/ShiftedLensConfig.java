package com.platypushasnohat.shifted_lens;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShiftedLensConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    // common
    public static ForgeConfigSpec.ConfigValue<Boolean> MILKABLE_SQUIDS;
    public static ForgeConfigSpec.ConfigValue<Boolean> BUCKETABLE_SQUIDS;
    public static ForgeConfigSpec.ConfigValue<Boolean> BLINDING_SQUIDS;

    // client
    public static ForgeConfigSpec.ConfigValue<Boolean> RETRO_GHAST;
    public static ForgeConfigSpec.ConfigValue<Boolean> STAINLESS_GUARDIANS;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        MILKABLE_SQUIDS = COMMON_BUILDER
                .comment("If squids are milkable with buckets")
                .define("milkableSquids", true);

        BUCKETABLE_SQUIDS = COMMON_BUILDER
                .comment("If squids can be bucketed with water buckets")
                .define("bucketableSquids", true);

        BLINDING_SQUIDS = COMMON_BUILDER
                .comment("If squid ink applies blindness or glowing")
                .define("blindingSquids", true);

        COMMON_CONFIG = COMMON_BUILDER.build();

        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        RETRO_GHAST = CLIENT_BUILDER
                .comment("If Ghasts have a more vanilla faithful texture")
                .define("retroGhasts", false);

        STAINLESS_GUARDIANS = CLIENT_BUILDER
                .comment("If Guardians have a cleaner looking texture")
                .define("stainlessGuardians", false);

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

}
