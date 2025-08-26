package com.platypushasnohat.shifted_lens.blocks;

import com.platypushasnohat.shifted_lens.blocks.blockentity.WhirligigBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class WhirligigBlock extends Block implements EntityBlock {

    public WhirligigBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new WhirligigBlockEntity(pos, state);
    }
}
