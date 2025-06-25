package com.platypushasnohat.shifted_lens;

import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.data.SLEntityTagProvider;
import com.platypushasnohat.shifted_lens.data.SLItemModelProvider;
import com.platypushasnohat.shifted_lens.data.SLLanguageProvider;
import com.platypushasnohat.shifted_lens.data.SLRecipeProvider;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Mod(ShiftedLens.MOD_ID)
public class ShiftedLens {

    public static final String MOD_ID = "shifted_lens";

    public ShiftedLens() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::dataSetup);

        context.registerConfig(ModConfig.Type.COMMON, SLCommonConfig.COMMON);

        SLItems.ITEMS.register(modEventBus);
        SLEntities.ENTITY_TYPES.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {

    }

    private void clientSetup(final FMLClientSetupEvent event) {

    }

    private void dataSetup(GatherDataEvent data) {
        DataGenerator generator = data.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = data.getLookupProvider();
        ExistingFileHelper helper = data.getExistingFileHelper();

        boolean server = data.includeServer();
        generator.addProvider(server, new SLEntityTagProvider(output, provider, helper));
        generator.addProvider(server, new SLRecipeProvider(output));

        boolean client = data.includeClient();
        generator.addProvider(client, new SLItemModelProvider(data));
        generator.addProvider(client, new SLLanguageProvider(data));
    }

    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }
}

