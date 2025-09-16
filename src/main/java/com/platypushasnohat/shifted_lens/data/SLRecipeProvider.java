package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.SLBlocks;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeCategory.*;
import static net.minecraft.data.recipes.ShapedRecipeBuilder.*;
import static net.minecraft.data.recipes.ShapelessRecipeBuilder.*;
import static net.minecraft.data.recipes.SimpleCookingRecipeBuilder.*;

public class SLRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public SLRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        cookingRecipes(SLItems.RAW_BAITFISH.get(), SLItems.COOKED_BAITFISH.get(), consumer);

        shapeless(MISC, SLItems.INFERNO_CHARGE.get()).requires(Items.GUNPOWDER).requires(Items.BLAZE_POWDER).requires(Items.GHAST_TEAR).requires(ItemTags.COALS).unlockedBy("has_ghast_tear", has(Items.GHAST_TEAR)).save(consumer);
        shaped(COMBAT, SLItems.WHIRLICAP.get()).define('#', Items.LEATHER_HELMET).define('X', SLBlocks.WHIRLIGIG.get()).pattern(" X ").pattern(" # ").unlockedBy("has_whirligig", has(SLBlocks.WHIRLIGIG.get())).save(consumer);
        shaped(COMBAT, SLItems.TOOTHED_SNOWBALL.get(), 4).define('#', Items.SNOWBALL).define('X', SLItems.SQUILL_TOOTH.get()).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_squill_tooth", has(SLItems.SQUILL_TOOTH.get())).save(consumer);
        shaped(REDSTONE, SLBlocks.WHIRLIBOX.get()).define('#', ItemTags.STONE_TOOL_MATERIALS).define('X', SLBlocks.WHIRLIGIG.get()).define('Y', Tags.Items.DUSTS_REDSTONE).pattern("###").pattern("#X#").pattern("#Y#").unlockedBy("has_whirligig", has(SLBlocks.WHIRLIGIG.get())).save(consumer);
    }

    private static void cookingRecipes(ItemLike ingredient, ItemLike result, Consumer<FinishedRecipe> consumer) {
        smelting(Ingredient.of(ingredient), RecipeCategory.FOOD, result, 0.35F, 200).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(result));
        campfireCooking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, 0.35F, 600).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(result) + "_from_campfire_cooking"));
        smoking(Ingredient.of(ingredient), RecipeCategory.FOOD, result, 0.35F, 100).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer, getSaveLocation(getName(result) + "_from_smoking"));
    }

    private static String getName(ItemLike object) {
        return ForgeRegistries.ITEMS.getKey(object.asItem()).getPath();
    }

    private static ResourceLocation getSaveLocation(ItemLike item) {
        return ForgeRegistries.ITEMS.getKey(item.asItem());
    }

    private static ResourceLocation getSaveLocation(String name) {
        return ShiftedLens.modPrefix(name);
    }

    private void wrap(RecipeBuilder builder, String name, Consumer<FinishedRecipe> consumer, ICondition... conditions) {
        wrap(builder, ShiftedLens.MOD_ID, name, consumer, conditions);
    }

    private void wrap(RecipeBuilder builder, String modid, String name, Consumer<FinishedRecipe> consumer, ICondition... conditions) {
        ResourceLocation loc = new ResourceLocation(modid, name);
        ConditionalRecipe.Builder cond;
        if (conditions.length > 1) {
            cond = ConditionalRecipe.builder().addCondition(and(conditions));
        } else if (conditions.length == 1) {
            cond = ConditionalRecipe.builder().addCondition(conditions[0]);
        } else {
            cond = ConditionalRecipe.builder();
        }
        FinishedRecipe[] recipe = new FinishedRecipe[1];
        builder.save(f -> recipe[0] = f, loc);
        cond.addRecipe(recipe[0]).generateAdvancement().build(consumer, loc);
    }
}
