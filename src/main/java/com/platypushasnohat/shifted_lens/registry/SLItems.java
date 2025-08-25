package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.items.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.level.material.Fluids;
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
    public static final RegistryObject<Item> TOOTHED_SNOWBALL = registerItem("toothed_snowball", () -> new ToothedSnowballItem(new Item.Properties().stacksTo(16)));

    public static final RegistryObject<Item> WHIRLICAP = registerItem("whirlicap", () -> new WhirlicapItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> LOLLIPOP = registerItem("lollipop", () -> new Item(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> RAW_BAITFISH = registerItemNoLang("baitfish", () -> new Item(foodItem(SLFoodValues.RAW_BAITFISH)));
    public static final RegistryObject<Item> COOKED_BAITFISH = registerItem("cooked_baitfish", () -> new Item(foodItem(SLFoodValues.COOKED_BAITFISH)));

    public static final RegistryObject<Item> FLYING_FISH = registerItem("flying_fish", () -> new Item(foodItem(SLFoodValues.FLYING_FISH)));
    public static final RegistryObject<Item> FLYING_FISH_BUCKET = registerItemNoLang("flying_fish_bucket", () -> new MobBucketItem(SLEntities.FLYING_FISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> SQUID_BUCKET = registerItemNoLang("squid_bucket", () -> new MobBucketItem(() -> EntityType.SQUID, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> SQUILL_BUCKET = registerItemNoLang("squill_bucket", () -> new SquillBucketItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> BAITFISH_SPAWN_EGG = registerSpawnEggItem("baitfish", SLEntities.BAITFISH, 0xe0e0e0, 0x757575);
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
