package com.platypushasnohat.shifted_lens;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShiftedLensConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static final String CATEGORY_MOBS = "mobs";
    public static final String CATEGORY_COD = "cod";
    public static final String CATEGORY_ELDER_GUARDIAN = "elder_guardian";
    public static final String CATEGORY_GHAST = "ghast";
    public static final String CATEGORY_GLOW_SQUID = "glow_squid";
    public static final String CATEGORY_GUARDIAN = "guardian";
    public static final String CATEGORY_RABBIT = "rabbit";
    public static final String CATEGORY_SALMON = "salmon";
    public static final String CATEGORY_SQUID = "squid";

    public static final String CLIENT_CATEGORY_MOBS = "mobs";
    public static final String CLIENT_CATEGORY_COD = "cod";
    public static final String CLIENT_CATEGORY_ELDER_GUARDIAN = "elder_guardian";
    public static final String CLIENT_CATEGORY_GHAST = "ghast";
    public static final String CLIENT_CATEGORY_GLOW_SQUID = "glow_squid";
    public static final String CLIENT_CATEGORY_GUARDIAN = "guardian";
    public static final String CLIENT_CATEGORY_RABBIT = "rabbit";
    public static final String CLIENT_CATEGORY_SALMON = "salmon";
    public static final String CLIENT_CATEGORY_SQUID = "squid";

    // common
    public static ForgeConfigSpec.ConfigValue<Boolean> MILKABLE_SQUIDS;
    public static ForgeConfigSpec.ConfigValue<Boolean> BUCKETABLE_SQUIDS;
    public static ForgeConfigSpec.ConfigValue<Boolean> BLINDING_SQUIDS;

    // client
    public static ForgeConfigSpec.ConfigValue<Boolean> COD_REMODEL;

    public static ForgeConfigSpec.ConfigValue<Boolean> ELDER_GUARDIAN_REMODEL;

    public static ForgeConfigSpec.ConfigValue<Boolean> GHAST_REMODEL;
    public static ForgeConfigSpec.ConfigValue<Boolean> RETRO_GHAST;

    public static ForgeConfigSpec.ConfigValue<Boolean> GLOW_SQUID_REMODEL;

    public static ForgeConfigSpec.ConfigValue<Boolean> GUARDIAN_REMODEL;
    public static ForgeConfigSpec.ConfigValue<Boolean> STAINLESS_GUARDIANS;

    public static ForgeConfigSpec.ConfigValue<Boolean> RABBIT_REMODEL;

    public static ForgeConfigSpec.ConfigValue<Boolean> SALMON_REMODEL;

    public static ForgeConfigSpec.ConfigValue<Boolean> SQUID_REMODEL;

    static {
        // common
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        // mobs
        COMMON_BUILDER.push(CATEGORY_MOBS);

        // squid
        COMMON_BUILDER.push(CATEGORY_SQUID);
        MILKABLE_SQUIDS = COMMON_BUILDER.comment("If squids are milkable with buckets").define("milkableSquids", true);
        BUCKETABLE_SQUIDS = COMMON_BUILDER.comment("If squids can be bucketed with water buckets").define("bucketableSquids", true);
        BLINDING_SQUIDS = COMMON_BUILDER.comment("If squid ink applies blindness or glowing").define("blindingSquids", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();

        // client
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        // mobs
        CLIENT_BUILDER.push(CATEGORY_MOBS);

        // cod
        CLIENT_BUILDER.push(CATEGORY_COD);
        COD_REMODEL = CLIENT_BUILDER.comment("Whether the Cod model is replaced").define("codRemodel", true);
        CLIENT_BUILDER.pop();

        // elder guardian
        CLIENT_BUILDER.push(CATEGORY_ELDER_GUARDIAN);
        ELDER_GUARDIAN_REMODEL = CLIENT_BUILDER.comment("Whether the Elder Guardian model is replaced").define("elderGuardianRemodel", true);
        CLIENT_BUILDER.pop();

        // ghast
        CLIENT_BUILDER.push(CATEGORY_GHAST);
        GHAST_REMODEL = CLIENT_BUILDER.comment("Whether the Ghast model is replaced").define("ghastRemodel", true);
        RETRO_GHAST = CLIENT_BUILDER.comment("Whether Ghasts have a more vanilla faithful texture").define("retroGhasts", false);
        CLIENT_BUILDER.pop();

        // glow squid
        CLIENT_BUILDER.push(CATEGORY_GLOW_SQUID);
        GLOW_SQUID_REMODEL = CLIENT_BUILDER.comment("Whether the Glow Squid model is replaced").define("glowSquidRemodel", true);
        CLIENT_BUILDER.pop();

        // guardian
        CLIENT_BUILDER.push(CATEGORY_GUARDIAN);
        GUARDIAN_REMODEL = CLIENT_BUILDER.comment("Whether the Guardian model is replaced").define("guardianRemodel", true);
        STAINLESS_GUARDIANS = CLIENT_BUILDER.comment("Whether Guardians have a cleaner looking texture").define("stainlessGuardians", false);
        CLIENT_BUILDER.pop();

        // rabbit
        CLIENT_BUILDER.push(CATEGORY_RABBIT);
        RABBIT_REMODEL = CLIENT_BUILDER.comment("Whether the Rabbit model is replaced").define("rabbitRemodel", true);
        CLIENT_BUILDER.pop();

        // salmon
        CLIENT_BUILDER.push(CATEGORY_SALMON);
        SALMON_REMODEL = CLIENT_BUILDER.comment("Whether the Salmon model is replaced").define("salmonRemodel", true);
        CLIENT_BUILDER.pop();

        // squid
        CLIENT_BUILDER.push(CATEGORY_SQUID);
        SQUID_REMODEL = CLIENT_BUILDER.comment("Whether the Squid model is replaced").define("squidRemodel", true);
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.pop();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

}
