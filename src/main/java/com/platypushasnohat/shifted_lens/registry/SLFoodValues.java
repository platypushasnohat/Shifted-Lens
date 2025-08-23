package com.platypushasnohat.shifted_lens.registry;

import net.minecraft.world.food.FoodProperties;

public class SLFoodValues {

    public static final FoodProperties RAW_ANCHOVY = (new FoodProperties.Builder())
            .nutrition(1).saturationMod(0.1F)
            .fast()
            .build();

    public static final FoodProperties COOKED_ANCHOVY = (new FoodProperties.Builder())
            .nutrition(3).saturationMod(0.5F)
            .fast()
            .build();

}
