package com.platypushasnohat.shifted_lens.data;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.registries.RegistryObject;

import static com.platypushasnohat.shifted_lens.registry.SLItems.*;

public class SLItemModelProvider extends ItemModelProvider {

    public SLItemModelProvider(GatherDataEvent event) {
        super(event.getGenerator().getPackOutput(), ShiftedLens.MOD_ID, event.getExistingFileHelper());
    }

    @Override
    protected void registerModels() {
        item(INFERNO_CHARGE);
    }

    private ItemModelBuilder item(RegistryObject<?> item) {
        return generated(item.getId().getPath(), modLoc("item/" + item.getId().getPath()));
    }

    private ItemModelBuilder generated(String name, ResourceLocation... layers) {
        ItemModelBuilder builder = withExistingParent(name, "item/generated");
        for (int i = 0; i < layers.length; i++) {
            builder = builder.texture("layer" + i, layers[i]);
        }
        return builder;
    }
}
