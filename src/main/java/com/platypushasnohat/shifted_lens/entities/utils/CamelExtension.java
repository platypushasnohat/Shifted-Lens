package com.platypushasnohat.shifted_lens.entities.utils;

import com.platypushasnohat.shifted_lens.registry.tags.SLBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class CamelExtension extends Camel {
    public CamelExtension(EntityType<? extends Camel> entityType, Level level) {
        super(entityType, level);
    }

    public static boolean checkCamelSpawnRules(EntityType<Camel> entityType, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        return level.getBlockState(pos.below()).is(SLBlockTags.CAMEL_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(level, pos) && random.nextFloat() <= 0.1F;
    }
}
