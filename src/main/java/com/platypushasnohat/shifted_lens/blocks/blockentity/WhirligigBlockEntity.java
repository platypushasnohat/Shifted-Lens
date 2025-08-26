package com.platypushasnohat.shifted_lens.blocks.blockentity;

import com.platypushasnohat.shifted_lens.registry.SLBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WhirligigBlockEntity extends BlockEntity {

    public WhirligigBlockEntity(BlockPos pos, BlockState state) {
        super(SLBlockEntities.WHIRLIGIG_BLOCK_ENTITY.get(), pos, state);
    }
}
