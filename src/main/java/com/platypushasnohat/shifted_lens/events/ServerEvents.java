package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.entities.SLElderGuardian;
import com.platypushasnohat.shifted_lens.entities.SLGhast;
import com.platypushasnohat.shifted_lens.entities.SLGuardian;
import com.platypushasnohat.shifted_lens.entities.SLSalmon;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
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

        boolean invalidSpawnType = event.getSpawnType() == MobSpawnType.STRUCTURE;
        if (!invalidSpawnType) {
            if (event.getResult() != Result.DENY) {
                if (entity.getType() == EntityType.GHAST && SLCommonConfig.REPLACE_GHAST.get()) {
                    Ghast ghast = (Ghast) entity;
                    SLGhast slGhast = SLEntities.GHAST.get().create((Level) level);
                    if (slGhast != null) {
                        slGhast.copyPosition(ghast);
                        level.addFreshEntity(slGhast);
                    }
                    event.setSpawnCancelled(true);
                    event.setResult(Result.DENY);
                }

                if (entity.getType() == EntityType.GUARDIAN && SLCommonConfig.REPLACE_GUARDIAN.get()) {
                    Guardian guardian = (Guardian) entity;
                    SLGuardian slGuardian = SLEntities.GUARDIAN.get().create((Level) level);
                    if (slGuardian != null) {
                        slGuardian.copyPosition(guardian);
                        level.addFreshEntity(slGuardian);
                    }
                    event.setSpawnCancelled(true);
                    event.setResult(Result.DENY);
                }
                if (entity.getType() == EntityType.ELDER_GUARDIAN && SLCommonConfig.REPLACE_ELDER_GUARDIAN.get()) {
                    ElderGuardian elderGuardian = (ElderGuardian) entity;
                    SLElderGuardian slElderGuardian = SLEntities.ELDER_GUARDIAN.get().create((Level) level);
                    if (slElderGuardian != null) {
                        slElderGuardian.copyPosition(elderGuardian);
                        level.addFreshEntity(slElderGuardian);
                    }
                    event.setSpawnCancelled(true);
                    event.setResult(Result.DENY);
                }
                if (entity.getType() == EntityType.SALMON && SLCommonConfig.REPLACE_SALMON.get()) {
                    Salmon salmon = (Salmon) entity;
                    SLSalmon slSalmon = SLEntities.SALMON.get().create((Level) level);
                    if (slSalmon != null) {
                        slSalmon.copyPosition(salmon);
                        level.addFreshEntity(slSalmon);
                    }
                    event.setSpawnCancelled(true);
                    event.setResult(Result.DENY);
                }
            }
        }
    }
}
