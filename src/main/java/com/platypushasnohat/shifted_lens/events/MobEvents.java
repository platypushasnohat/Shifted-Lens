package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.SLElderGuardian;
import com.platypushasnohat.shifted_lens.entities.SLGhast;
import com.platypushasnohat.shifted_lens.entities.SLGuardian;
import com.platypushasnohat.shifted_lens.entities.SLSalmon;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEvents {

    @SubscribeEvent
    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(SLEntities.GHAST.get(), SLGhast.createAttributes().build());

        event.put(SLEntities.GUARDIAN.get(), SLGuardian.createAttributes().build());
        event.put(SLEntities.ELDER_GUARDIAN.get(), SLElderGuardian.createAttributes().build());
        event.put(SLEntities.SALMON.get(), SLSalmon.createAttributes().build());
    }
}
