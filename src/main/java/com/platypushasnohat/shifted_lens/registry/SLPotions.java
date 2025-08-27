package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SLPotions {

    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, ShiftedLens.MOD_ID);

    public static final RegistryObject<Potion> LEVITATION_POTION = POTIONS.register("levitation", () -> new Potion(new MobEffectInstance(MobEffects.LEVITATION, 400)));
}
