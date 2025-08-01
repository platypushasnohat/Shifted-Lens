package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.config.SLCommonConfig;
import com.platypushasnohat.shifted_lens.entities.*;
import com.platypushasnohat.shifted_lens.registry.SLEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Salmon;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.Event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onLivingSpawn(MobSpawnEvent.FinalizeSpawn event) {
        LivingEntity entity = event.getEntity();
        ServerLevelAccessor level = event.getLevel();
        DifficultyInstance difficulty = event.getDifficulty();
        MobSpawnType spawnType = event.getSpawnType();
        SpawnGroupData spawnData = event.getSpawnData();
        CompoundTag compoundTag = event.getSpawnTag();

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
                        slSalmon.finalizeSpawn(level, difficulty, spawnType, spawnData, compoundTag);
                        level.addFreshEntity(slSalmon);
                    }
                    event.setSpawnCancelled(true);
                    event.setResult(Result.DENY);
                }
            }
        }
    }
}
