package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SLSoundEvents {

    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ShiftedLens.MOD_ID);

    public static final RegistryObject<SoundEvent> FISH_HURT = registerSoundEvent("fish_hurt");
    public static final RegistryObject<SoundEvent> FISH_DEATH = registerSoundEvent("fish_death");
    public static final RegistryObject<SoundEvent> FISH_FLOP = registerSoundEvent("fish_flop");

    private static RegistryObject<SoundEvent> registerSoundEvent(final String soundName) {
        return SOUND_EVENTS.register(soundName, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(ShiftedLens.MOD_ID, soundName)));
    }
}
