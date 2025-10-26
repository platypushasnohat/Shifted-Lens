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

    // common
    public static ForgeConfigSpec.BooleanValue BETTER_GHAST_AI;
    public static ForgeConfigSpec.BooleanValue MORE_GHAST_XP;

    public static ForgeConfigSpec.BooleanValue BETTER_SQUID_AI;
    public static ForgeConfigSpec.BooleanValue MILKABLE_SQUIDS;
    public static ForgeConfigSpec.BooleanValue BUCKETABLE_SQUIDS;
    public static ForgeConfigSpec.BooleanValue BLINDING_SQUIDS;
    public static ForgeConfigSpec.BooleanValue FLOPPING_SQUIDS;
    public static ForgeConfigSpec.IntValue BLINDING_DURATION;

    public static ForgeConfigSpec.BooleanValue GLOWING_SQUIDS;
    public static ForgeConfigSpec.IntValue GLOWING_DURATION;

    // client
    public static ForgeConfigSpec.BooleanValue COD_REMODEL;

    public static ForgeConfigSpec.BooleanValue ELDER_GUARDIAN_REMODEL;

    public static ForgeConfigSpec.BooleanValue GHAST_REMODEL;
    public static ForgeConfigSpec.BooleanValue RETRO_GHAST;

    public static ForgeConfigSpec.BooleanValue GLOW_SQUID_REMODEL;

    public static ForgeConfigSpec.BooleanValue GUARDIAN_REMODEL;
    public static ForgeConfigSpec.BooleanValue STAINLESS_GUARDIANS;

    public static ForgeConfigSpec.BooleanValue RABBIT_REMODEL;

    public static ForgeConfigSpec.BooleanValue SALMON_REMODEL;

    public static ForgeConfigSpec.BooleanValue SQUID_REMODEL;

    static {
        // common
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        // mobs
        COMMON_BUILDER.push(CATEGORY_MOBS);

        // ghast
        COMMON_BUILDER.push(CATEGORY_GHAST);
        BETTER_GHAST_AI = COMMON_BUILDER.comment("Whether ghasts have new ai").define("betterGhastAi", true);
        MORE_GHAST_XP = COMMON_BUILDER.comment("Whether ghasts drop more experience").define("moreGhastXp", true);
        COMMON_BUILDER.pop();

        // glow squid
        COMMON_BUILDER.push(CATEGORY_GLOW_SQUID);
        GLOWING_SQUIDS = COMMON_BUILDER.comment("Whether glow squid ink applies glowing").define("glowingSquids", true);
        GLOWING_DURATION = COMMON_BUILDER.comment("The duration of the glowing effect applied by glow squid ink").defineInRange("glowingDuration", 60, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        // squid
        COMMON_BUILDER.push(CATEGORY_SQUID);
        BETTER_SQUID_AI = COMMON_BUILDER.comment("Whether squids have new ai").define("betterSquidMovement", true);
        MILKABLE_SQUIDS = COMMON_BUILDER.comment("Whether squids are milkable with buckets").define("milkableSquids", true);
        BUCKETABLE_SQUIDS = COMMON_BUILDER.comment("Whether squids can be bucketed with water buckets").define("bucketableSquids", true);
        BLINDING_SQUIDS = COMMON_BUILDER.comment("Whether squid ink applies blindness").define("blindingSquids", true);
        FLOPPING_SQUIDS = COMMON_BUILDER.comment("Whether squids flop around on land").define("floppingSquids", true);
        BLINDING_DURATION = COMMON_BUILDER.comment("The duration of the blindness effect applied by squid ink").defineInRange("blindingDuration", 60, 0, Integer.MAX_VALUE);
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
        RETRO_GHAST = CLIENT_BUILDER.comment("Whether remodeled Ghasts have a more vanilla faithful texture").define("retroGhasts", false);
        CLIENT_BUILDER.pop();

        // glow squid
        CLIENT_BUILDER.push(CATEGORY_GLOW_SQUID);
        GLOW_SQUID_REMODEL = CLIENT_BUILDER.comment("Whether the Glow Squid model is replaced").define("glowSquidRemodel", true);
        CLIENT_BUILDER.pop();

        // guardian
        CLIENT_BUILDER.push(CATEGORY_GUARDIAN);
        GUARDIAN_REMODEL = CLIENT_BUILDER.comment("Whether the Guardian model is replaced").define("guardianRemodel", true);
        STAINLESS_GUARDIANS = CLIENT_BUILDER.comment("Whether remodeled Guardians have a cleaner looking texture").define("stainlessGuardians", false);
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
