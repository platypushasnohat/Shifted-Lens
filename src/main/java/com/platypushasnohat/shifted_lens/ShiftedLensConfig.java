package com.platypushasnohat.shifted_lens;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShiftedLensConfig {

    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static final String CATEGORY_ITEMS = "items";

    public static final String CATEGORY_MOBS = "mobs";
    public static final String CATEGORY_COD = "cod";
    public static final String CATEGORY_GHAST = "ghast";
    public static final String CATEGORY_GUARDIAN = "guardian";
    public static final String CATEGORY_PUFFERFISH = "pufferfish";
    public static final String CATEGORY_RABBIT = "rabbit";
    public static final String CATEGORY_SALMON = "salmon";
    public static final String CATEGORY_SQUID = "squid";
    public static final String CATEGORY_TADPOLE = "tadpole";
    public static final String CATEGORY_TROPICAL_FISH = "tropical_fish";

    // common

    // items
    public static ForgeConfigSpec.BooleanValue FREEZING_SNOWBALLS;
    public static ForgeConfigSpec.BooleanValue THROWABLE_FIRE_CHARGES;

    // mobs
    public static ForgeConfigSpec.BooleanValue BETTER_FISH_FLOPPING;
    public static ForgeConfigSpec.BooleanValue MILKABLE_EVERYTHING;

    public static ForgeConfigSpec.BooleanValue COD_REVAMP;

    public static ForgeConfigSpec.BooleanValue GHAST_REVAMP;
    public static ForgeConfigSpec.BooleanValue MORE_GHAST_XP;

    public static ForgeConfigSpec.BooleanValue GUARDIAN_REVAMP;

    public static ForgeConfigSpec.BooleanValue PUFFERFISH_REVAMP;

    public static ForgeConfigSpec.BooleanValue RABBIT_REVAMP;
    public static ForgeConfigSpec.BooleanValue RABBIT_STEP_UP;
    public static ForgeConfigSpec.BooleanValue RABBIT_FALL_DAMAGE_TWEAK;
    public static ForgeConfigSpec.BooleanValue NATURAL_KILLER_RABBITS;

    public static ForgeConfigSpec.BooleanValue SALMON_REVAMP;

    public static ForgeConfigSpec.BooleanValue SQUID_REVAMP;
    public static ForgeConfigSpec.BooleanValue MILKABLE_SQUIDS;
    public static ForgeConfigSpec.BooleanValue BUCKETABLE_SQUIDS;
    public static ForgeConfigSpec.BooleanValue FLOPPING_SQUIDS;
    public static ForgeConfigSpec.BooleanValue BLINDING_SQUIDS;
    public static ForgeConfigSpec.IntValue BLINDING_DURATION;
    public static ForgeConfigSpec.BooleanValue GLOWING_SQUIDS;
    public static ForgeConfigSpec.IntValue GLOWING_DURATION;

    public static ForgeConfigSpec.BooleanValue TROPICAL_FISH_REVAMP;

    // client
    public static ForgeConfigSpec.BooleanValue RETRO_GHAST;

    public static ForgeConfigSpec.BooleanValue STAINLESS_GUARDIANS;

    public static ForgeConfigSpec.BooleanValue TADPOLE_TILTING;

    static {
        // common
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();

        COMMON_BUILDER.push(CATEGORY_ITEMS);

        FREEZING_SNOWBALLS = COMMON_BUILDER.comment("Whether snowballs apply freezing ticks on hit").define("freezingSnowballs", true);
        THROWABLE_FIRE_CHARGES = COMMON_BUILDER.comment("Whether fire charges can be thrown").define("throwableFireCharges", true);

        COMMON_BUILDER.pop();

        // Mobs
        COMMON_BUILDER.push(CATEGORY_MOBS);

        // Misc
        BETTER_FISH_FLOPPING = COMMON_BUILDER.comment("Whether fish flop less often").define("betterFishFlopping", true);
        MILKABLE_EVERYTHING = COMMON_BUILDER.comment("Whether you can milk any entity, I don't know why you would want this").define("milkableEverything", false);

        // Cod
        COMMON_BUILDER.push(CATEGORY_COD);
        COD_REVAMP = COMMON_BUILDER.comment("Whether cod have updated ai and a new model").define("codRevamp", true);
        COMMON_BUILDER.pop();

        // Ghast
        COMMON_BUILDER.push(CATEGORY_GHAST);
        GHAST_REVAMP = COMMON_BUILDER.comment("Whether ghasts have updated ai and a new model").define("ghastRevamp", true);
        MORE_GHAST_XP = COMMON_BUILDER.comment("Whether ghasts drop more experience").define("moreGhastXp", true);
        COMMON_BUILDER.pop();

        // Guardian
        COMMON_BUILDER.push(CATEGORY_GUARDIAN);
        GUARDIAN_REVAMP = COMMON_BUILDER.comment("Whether guardiands have updated ai and a new model").define("guardianRevamp", true);
        COMMON_BUILDER.pop();

        // Pufferfish
        COMMON_BUILDER.push(CATEGORY_PUFFERFISH);
        PUFFERFISH_REVAMP = COMMON_BUILDER.comment("Whether pufferfish have updated ai").define("pufferfishRevamp", true);
        COMMON_BUILDER.pop();

        // Rabbit
        COMMON_BUILDER.push(CATEGORY_RABBIT);
        RABBIT_REVAMP = COMMON_BUILDER.comment("Whether rabbits are upscaled and have updated ai").define("rabbitRevamp", true);
        RABBIT_STEP_UP = COMMON_BUILDER.comment("Whether rabbits can step up blocks to prevent getting stuck on the edges of blocks").define("rabbitStepUp", true);
        RABBIT_FALL_DAMAGE_TWEAK = COMMON_BUILDER.comment("Whether rabbits need to fall further before taking fall damage").define("rabbitFallDamageTweak", true);
        NATURAL_KILLER_RABBITS = COMMON_BUILDER.comment("Whether killer rabbits can spawn naturally").define("naturalKillerRabbits", true);
        COMMON_BUILDER.pop();

        // Salmon
        COMMON_BUILDER.push(CATEGORY_SALMON);
        SALMON_REVAMP = COMMON_BUILDER.comment("Whether salmon have updated ai and a new model").define("salmonRevamp", true);
        COMMON_BUILDER.pop();

        // Squid
        COMMON_BUILDER.push(CATEGORY_SQUID);
        SQUID_REVAMP = COMMON_BUILDER.comment("Whether squids have updated ai and new animations").define("squidRevamp", true);
        MILKABLE_SQUIDS = COMMON_BUILDER.comment("Whether squids are milkable with buckets").define("milkableSquids", true);
        BUCKETABLE_SQUIDS = COMMON_BUILDER.comment("Whether squids can be bucketed with water buckets").define("bucketableSquids", true);
        FLOPPING_SQUIDS = COMMON_BUILDER.comment("Whether squids flop around on land").define("floppingSquids", true);
        BLINDING_SQUIDS = COMMON_BUILDER.comment("Whether squid ink applies blindness").define("blindingSquids", true);
        BLINDING_DURATION = COMMON_BUILDER.comment("The duration of the blindness effect applied by squid ink").defineInRange("blindingDuration", 60, 0, Integer.MAX_VALUE);
        GLOWING_SQUIDS = COMMON_BUILDER.comment("Whether glow squid ink applies glowing").define("glowingSquids", true);
        GLOWING_DURATION = COMMON_BUILDER.comment("The duration of the glowing effect applied by glow squid ink").defineInRange("glowingDuration", 60, 0, Integer.MAX_VALUE);
        COMMON_BUILDER.pop();

        // Tropical fish
        COMMON_BUILDER.push(CATEGORY_TROPICAL_FISH);
        TROPICAL_FISH_REVAMP = COMMON_BUILDER.comment("Whether tropical fish have updated ai").define("tropicalFishRevamp", true);
        COMMON_BUILDER.pop();

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();

        // client
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();

        // Mobs client
        CLIENT_BUILDER.push(CATEGORY_MOBS);

        // Ghast client
        CLIENT_BUILDER.push(CATEGORY_GHAST);
        RETRO_GHAST = CLIENT_BUILDER.comment("Whether remodeled ghasts have a more vanilla faithful texture").define("retroGhasts", false);
        CLIENT_BUILDER.pop();

        // Guardian client
        CLIENT_BUILDER.push(CATEGORY_GUARDIAN);
        STAINLESS_GUARDIANS = CLIENT_BUILDER.comment("Whether remodeled guardians have a cleaner looking texture").define("stainlessGuardians", false);
        CLIENT_BUILDER.pop();

        // Tadpole client
        CLIENT_BUILDER.push(CATEGORY_TADPOLE);
        TADPOLE_TILTING = CLIENT_BUILDER.comment("Whether tadpoles tilt up and down while swimming").define("tadpoleTilting", false);
        CLIENT_BUILDER.pop();

        CLIENT_BUILDER.pop();

        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

}
