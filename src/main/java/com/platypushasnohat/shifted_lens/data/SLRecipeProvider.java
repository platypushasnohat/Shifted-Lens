package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.SLBlocks;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

import static net.minecraft.data.recipes.RecipeCategory.*;

public class SLRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public SLRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        ShapelessRecipeBuilder.shapeless(MISC, SLItems.INFERNO_CHARGE.get()).requires(Items.GUNPOWDER).requires(Items.BLAZE_POWDER).requires(Items.GHAST_TEAR).requires(ItemTags.COALS).unlockedBy("has_ghast_tear", has(Items.GHAST_TEAR)).save(consumer);
        ShapedRecipeBuilder.shaped(COMBAT, SLItems.WHIRLICAP.get()).define('#', Items.LEATHER_HELMET).define('X', SLItems.WHIRLIGIG.get()).pattern(" X ").pattern(" # ").unlockedBy("has_whirligig", has(SLItems.WHIRLIGIG.get())).save(consumer);
        ShapedRecipeBuilder.shaped(COMBAT, SLItems.TOOTHED_SNOWBALL.get(), 4).define('#', Items.SNOWBALL).define('X', SLItems.SQUILL_TOOTH.get()).pattern(" # ").pattern("#X#").pattern(" # ").unlockedBy("has_squill_tooth", has(SLItems.SQUILL_TOOTH.get())).save(consumer);
        ShapedRecipeBuilder.shaped(REDSTONE, SLBlocks.WHIRLIBOX.get()).define('#', ItemTags.STONE_TOOL_MATERIALS).define('X', SLItems.WHIRLIGIG.get()).define('Y', Tags.Items.DUSTS_REDSTONE).pattern("###").pattern("#X#").pattern("#Y#").unlockedBy("has_whirligig", has(SLItems.WHIRLIGIG.get())).save(consumer);
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
