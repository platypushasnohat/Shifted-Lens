package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.entities.SLGhast;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onLivingSpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        LevelAccessor level = event.getLevel();
        if (event.getResult() != Result.DENY) {
            if (entity.getType() == EntityType.GHAST && SLCommonConfig.REPLACE_GHAST.get()) {
                Ghast ghast = (Ghast) entity;
                SLGhast shiftedGhast = SLEntities.SHIFTED_GHAST.get().create((Level) level);
                if (shiftedGhast != null) {
                    shiftedGhast.copyPosition(ghast);
                    level.addFreshEntity(shiftedGhast);
                }
                event.setSpawnCancelled(true);
                event.setResult(Result.DENY);
            }
        }
    }
}
