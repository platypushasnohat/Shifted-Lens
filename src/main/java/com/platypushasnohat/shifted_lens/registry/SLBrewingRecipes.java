package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.effects.SLBrewingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.RegistryObject;

public class SLBrewingRecipes {

    public static void registerPotionRecipes() {
        BrewingRecipeRegistry.addRecipe(new SLBrewingRecipe(Ingredient.of(registerPotion(Potions.SLOW_FALLING)), Ingredient.of(SLItems.SQUILL_TOOTH.get()), registerPotion(SLPotions.LEVITATION_POTION)));
    }

    public static ItemStack registerPotion(RegistryObject<Potion> potion) {
        return registerPotion(potion.get());
    }

    public static ItemStack registerPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.POTION), potion);
    }

    public static ItemStack registerSplashPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion);
    }

    public static ItemStack registerLingeringPotion(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.LINGERING_POTION), potion);
    }

    public static ItemStack registerTippedArrow(Potion potion) {
        return PotionUtils.setPotion(new ItemStack(Items.TIPPED_ARROW), potion);
    }
}
