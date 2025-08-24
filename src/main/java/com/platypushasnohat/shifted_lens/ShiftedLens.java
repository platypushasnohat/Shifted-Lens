package com.platypushasnohat.shifted_lens;

import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.data.*;
import com.platypushasnohat.shifted_lens.events.MiscEvents;
import com.platypushasnohat.shifted_lens.registry.SLBrewingRecipes;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.SLPotions;
import com.platypushasnohat.shifted_lens.utils.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
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
    public static final CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public ShiftedLens() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext context = ModLoadingContext.get();

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::dataSetup);

        context.registerConfig(ModConfig.Type.COMMON, SLConfig.COMMON);

        SLItems.ITEMS.register(modEventBus);
        SLEntities.ENTITY_TYPES.register(modEventBus);
        SLPotions.POTIONS.register(modEventBus);
        ShiftedLensTab.CREATIVE_TAB.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new MiscEvents());
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(SLBrewingRecipes::registerPotionRecipes);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(PROXY::clientInit);
    }

    private void dataSetup(GatherDataEvent data) {
        DataGenerator generator = data.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> provider = data.getLookupProvider();
        ExistingFileHelper helper = data.getExistingFileHelper();

        boolean server = data.includeServer();

        SLDatapackBuiltinEntriesProvider datapackEntries = new SLDatapackBuiltinEntriesProvider(output, provider);
        generator.addProvider(server, datapackEntries);
        provider = datapackEntries.getRegistryProvider();

        SLBlockTagProvider blockTags = new SLBlockTagProvider(output, provider, helper);
        generator.addProvider(server, blockTags);
        generator.addProvider(server, new SLEntityTagProvider(output, provider, helper));
        generator.addProvider(server, new SLBiomeTagProvider(output, provider, helper));
        generator.addProvider(server, new SLDamageTypeTagProvider(output, provider, helper));
        generator.addProvider(server, new SLRecipeProvider(output));

        boolean client = data.includeClient();
        generator.addProvider(client, new SLItemModelProvider(data));
        generator.addProvider(client, new SLLanguageProvider(data));
    }

    public static ResourceLocation modPrefix(String name) {
        return new ResourceLocation(MOD_ID, name.toLowerCase(Locale.ROOT));
    }
}

