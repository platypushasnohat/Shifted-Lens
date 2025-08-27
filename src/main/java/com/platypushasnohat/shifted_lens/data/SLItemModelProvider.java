package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

import static com.platypushasnohat.shifted_lens.registry.SLItems.*;

public class SLItemModelProvider extends ItemModelProvider {

    public SLItemModelProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), ShiftedLens.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void registerModels() {
        this.item(SQUILL_TOOTH);
        this.item(TOOTHED_SNOWBALL);
        this.item(SQUILL_BUCKET);
        this.item(WHIRLICAP);

        this.item(RAW_BAITFISH);
        this.item(COOKED_BAITFISH);
        this.item(BAITFISH_BUCKET);

        this.item(FLYING_FISH);
        this.item(FLYING_FISH_BUCKET);

        this.item(INFERNO_CHARGE);
        this.item(SQUID_BUCKET);

        for (Item item : BuiltInRegistries.ITEM) {
            if (item instanceof SpawnEggItem && Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getNamespace().equals(ShiftedLens.MOD_ID)) {
                getBuilder(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath()).parent(getExistingFile(new ResourceLocation("item/template_spawn_egg")));
            }
        }
    }

    private ItemModelBuilder item(RegistryObject<?> item) {
        return generated(item.getId().getPath(), modLoc("item/" + item.getId().getPath()));
    }

    private ItemModelBuilder handheldItem(RegistryObject<?> item) {
        return handheld(item.getId().getPath(), modLoc("item/" + item.getId().getPath()));
    }

    // utils
    private ItemModelBuilder generated(String name, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, "item/generated");
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        return builder;
    }

    private ItemModelBuilder handheld(String name, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, "item/handheld");
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        return builder;
    }
}
