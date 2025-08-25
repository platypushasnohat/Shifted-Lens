package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensTab;
import com.platypushasnohat.shifted_lens.registry.SLBlocks;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLPotions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Objects;
import java.util.function.Supplier;

public class SLLanguageProvider extends LanguageProvider {

    public SLLanguageProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), ShiftedLens.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {

        this.addTab(ShiftedLensTab.SHIFTED_LENS_TAB.get(), "Shifted Lens");

        SLItems.AUTO_TRANSLATE.forEach(this::forItems);
        SLBlocks.AUTO_TRANSLATE.forEach(this::forBlocks);

        this.addItem(SLItems.RAW_BAITFISH, "Raw Baitfish");

        this.addItem(SLItems.FLYING_FISH_BUCKET, "Bucket of Flying Fish");
        this.addItem(SLItems.SQUID_BUCKET, "Bucket of Squid");
        this.addItem(SLItems.SQUILL_BUCKET, "Bucket of Squill");

        this.forEntity(SLEntities.BAITFISH);
        this.forEntity(SLEntities.FLYING_FISH);
        this.forEntity(SLEntities.SQUILL);

        this.potion(SLPotions.LEVITATION_POTION, "Levitation", "levitation");
        this.potion(SLPotions.LONG_LEVITATION_POTION, "Levitation", "long_levitation");
        this.potion(SLPotions.STRONG_LEVITATION_POTION, "Levitation", "strong_levitation");
    }

    @Override
    public String getName() {
        return  ShiftedLens.MOD_ID + " Languages: en_us";
    }

    private void forBlocks(Supplier<? extends Block> block) {
        addBlock(block, SLTextUtils.createTranslation(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(block.get())).getPath()));
    }

    private void forItems(Supplier<? extends Item> item) {
        addItem(item, SLTextUtils.createTranslation(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item.get())).getPath()));
    }

    private void forEntity(Supplier<? extends EntityType<?>> entity) {
        addEntityType(entity, SLTextUtils.createTranslation(Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entity.get())).getPath()));
    }

    public void sound(Supplier<? extends SoundEvent> key, String subtitle){
        add("subtitles.opposing_force." + key.get().getLocation().getPath(), subtitle);
    }

    private void addEnchantmentWithDesc(Enchantment enchantment, String description) {
        String name = ForgeRegistries.ENCHANTMENTS.getKey(enchantment).getPath();
        this.add(enchantment, formatEnchantment(name));
        this.add(enchantment.getDescriptionId() + ".desc", description);
    }

    private String formatEnchantment(String path) {
        return WordUtils.capitalizeFully(path.replace("_", " ")).replace("Of ", "of ");
    }

    public void potion(Supplier<? extends Potion> key, String name, String regName) {
        potions(key.get(), name, regName);
    }

    public void potions(Potion key, String name, String regName) {
        add("item.minecraft.potion.effect." + regName, "Potion of " + name);
        add("item.minecraft.splash_potion.effect." + regName, "Splash Potion of " + name);
        add("item.minecraft.lingering_potion.effect." + regName, "Lingering Potion of " + name);
        add("item.minecraft.tipped_arrow.effect." + regName, "Arrow of " + name);
    }

    public void addTab(CreativeModeTab key, String name){
        add(key.getDisplayName().getString(), name);
    }

    public void addAdvancement(String key, String name) {
        this.add("advancement." + ShiftedLens.MOD_ID + "." + key, name);
    }

    public void addAdvancementDesc(String key, String name) {
        this.add("advancement." + ShiftedLens.MOD_ID + "." + key + ".desc", name);
    }
}
