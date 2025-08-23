package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.items.InfernoChargeItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class SLItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ShiftedLens.MOD_ID);
    public static List<RegistryObject<? extends Item>> AUTO_TRANSLATE = new ArrayList<>();

    public static final RegistryObject<Item> INFERNO_CHARGE = registerItem("inferno_charge", () -> new InfernoChargeItem(new Item.Properties()));
    public static final RegistryObject<Item> WHIRLIGIG = registerItem("whirligig", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> SQUILL_TOOTH = registerItem("squill_tooth", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RAW_ANCHOVY = registerItemNoLang("anchovy", () -> new Item(foodItem(SLFoodValues.RAW_ANCHOVY)));
    public static final RegistryObject<Item> COOKED_ANCHOVY = registerItem("cooked_anchovy", () -> new Item(foodItem(SLFoodValues.COOKED_ANCHOVY)));

    public static final RegistryObject<Item> ANCHOVY_SPAWN_EGG = registerSpawnEggItem("anchovy", SLEntities.ANCHOVY, 0xe0e0e0, 0x757575);
    public static final RegistryObject<Item> FLYING_FISH_SPAWN_EGG = registerSpawnEggItem("flying_fish", SLEntities.FLYING_FISH, 0x147bb5, 0xffe0db);
    public static final RegistryObject<Item> SQUILL_SPAWN_EGG = registerSpawnEggItem("squill", SLEntities.SQUILL, 0xe1f7fe, 0x95c0d7);

    private static <I extends Item> RegistryObject<I> registerItem(String name, Supplier<? extends I> supplier) {
        RegistryObject<I> item = ITEMS.register(name, supplier);
        AUTO_TRANSLATE.add(item);
        return item;
    }

    private static <I extends Item> RegistryObject<I> registerItemNoLang(String name, Supplier<? extends I> supplier) {
        RegistryObject<I> item = ITEMS.register(name, supplier);
        return item;
    }

    private static RegistryObject<Item> registerSpawnEggItem(String name, RegistryObject type, int baseColor, int spotColor) {
        return registerItem(name + "_spawn_egg", () -> new ForgeSpawnEggItem(type, baseColor, spotColor, new Item.Properties()));
    }

    public static Item.Properties foodItem(FoodProperties food) {
        return new Item.Properties().food(food);
    }
}
