package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import static com.platypushasnohat.shifted_lens.registry.SLItems.*;

public class SLCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ShiftedLens.MOD_ID);

    public static final RegistryObject<CreativeModeTab> SHIFTED_LENS_TAB = CREATIVE_TABS.register("shifted_lens",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(INFERNO_CHARGE.get()))
                    .title(Component.translatable("itemGroup.shifted_lens"))
                    .displayItems((pParameters, tabOutput) -> {

                        tabOutput.accept(ANCHOVY_SPAWN_EGG.get());
                        tabOutput.accept(Items.ELDER_GUARDIAN_SPAWN_EGG);
                        tabOutput.accept(Items.GHAST_SPAWN_EGG);
                        tabOutput.accept(Items.GUARDIAN_SPAWN_EGG);
                        tabOutput.accept(Items.SALMON_SPAWN_EGG);
                        tabOutput.accept(Items.TROPICAL_FISH_SPAWN_EGG);

                        tabOutput.accept(Items.FIRE_CHARGE);
                        tabOutput.accept(INFERNO_CHARGE.get());

                    }).build());
}