package com.platypushasnohat.shifted_lens;

import com.platypushasnohat.shifted_lens.registry.SLBlocks;
import com.platypushasnohat.shifted_lens.registry.SLBrewingRecipes;
import com.platypushasnohat.shifted_lens.registry.SLPotions;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.platypushasnohat.shifted_lens.registry.SLItems.*;

public class ShiftedLensTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShiftedLens.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SHIFTED_LENS_TAB = CREATIVE_TAB.register("shifted_lens",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(WHIRLICAP.get()))
                    .title(Component.translatable("itemGroup.shifted_lens"))
                    .displayItems((parameters, output) -> {

                        output.accept(SQUILL_SPAWN_EGG.get());
                        output.accept(SQUILL_TOOTH.get());
                        output.accept(TOOTHED_SNOWBALL.get());
                        output.accept(WHIRLIGIG.get());
                        output.accept(WHIRLICAP.get());
                        output.accept(SLBlocks.WHIRLIBOX.get());
                        output.accept(LOLLIPOP.get());
                        output.accept(SQUILL_BUCKET.get());
                        output.accept(BAITFISH_SPAWN_EGG.get());
                        output.accept(RAW_BAITFISH.get());
                        output.accept(COOKED_BAITFISH.get());
                        output.accept(FLYING_FISH_SPAWN_EGG.get());
                        output.accept(FLYING_FISH.get());
                        output.accept(FLYING_FISH_BUCKET.get());
                        output.accept(INFERNO_CHARGE.get());
                        output.accept(SQUID_BUCKET.get());

                        output.accept(SLBrewingRecipes.registerPotion(SLPotions.LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerPotion(SLPotions.LONG_LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerPotion(SLPotions.STRONG_LEVITATION_POTION.get()));

                        output.accept(SLBrewingRecipes.registerSplashPotion(SLPotions.LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerSplashPotion(SLPotions.LONG_LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerSplashPotion(SLPotions.STRONG_LEVITATION_POTION.get()));

                        output.accept(SLBrewingRecipes.registerLingeringPotion(SLPotions.LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerLingeringPotion(SLPotions.LONG_LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerLingeringPotion(SLPotions.STRONG_LEVITATION_POTION.get()));

                        output.accept(SLBrewingRecipes.registerTippedArrow(SLPotions.LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerTippedArrow(SLPotions.LONG_LEVITATION_POTION.get()));
                        output.accept(SLBrewingRecipes.registerTippedArrow(SLPotions.STRONG_LEVITATION_POTION.get()));

                    }).build());
}