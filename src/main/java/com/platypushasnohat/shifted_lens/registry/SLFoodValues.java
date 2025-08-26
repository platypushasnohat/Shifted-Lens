package com.platypushasnohat.shifted_lens.registry;

import net.minecraft.world.food.FoodProperties;

public class SLFoodValues {

    public static final FoodProperties RAW_BAITFISH = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.1F)
            .fast()
            .build();

    public static final FoodProperties COOKED_BAITFISH = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.5F)
            .fast()
            .build();

    public static final FoodProperties FLYING_FISH = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.2F)
            .build();

    public static final FoodProperties LOLLIPOP = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.2F)
            .build();

}
