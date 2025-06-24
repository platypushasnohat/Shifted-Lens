package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.SLGhast;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SLEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ShiftedLens.MOD_ID);

    public static final RegistryObject<EntityType<SLGhast>> SHIFTED_GHAST = ENTITY_TYPES.register(
            "ghast", () ->
            EntityType.Builder.of(SLGhast::new, MobCategory.MONSTER)
                    .sized(4.0F, 4.0F)
                    .clientTrackingRange(10)
                    .fireImmune()
                    .build(new ResourceLocation(ShiftedLens.MOD_ID, "ghast").toString())
    );
}
