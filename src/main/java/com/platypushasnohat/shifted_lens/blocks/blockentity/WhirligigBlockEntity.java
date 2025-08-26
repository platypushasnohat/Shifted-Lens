package com.platypushasnohat.shifted_lens.blocks.blockentity;

import com.platypushasnohat.shifted_lens.blocks.WhirligigBlock;
import com.platypushasnohat.shifted_lens.registry.SLBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class WhirligigBlockEntity extends BlockEntity {

    public int tickCount;
    public float activeRotation;

    public WhirligigBlockEntity(BlockPos pos, BlockState state) {
        super(SLBlockEntities.WHIRLIGIG_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WhirligigBlockEntity whirligig) {
        whirligig.tickCount++;

        if (level.getGameTime() % 20L == 0L) {
            Block block = state.getBlock();
            if (block instanceof WhirligigBlock) {
                WhirligigBlock.updatePower(state, level, pos);
            }
        }

        if (level.isClientSide()) {
            whirligig.activeRotation += 1.0F;
            if (state.getValue(WhirligigBlock.WATERLOGGED) && level.getBlockState(pos.above()) == Blocks.WATER.defaultBlockState()) {
                float angle = (float) Math.toRadians(state.getValue(WhirligigBlock.ROTATION) * 360.0F) / 16.0F;

                double xDirection = -Math.sin(angle);
                double zDirection = Math.cos(angle);

                if (level.random.nextFloat() <= 0.1F) {
                    double x = pos.getX() + 0.5 + xDirection * 0.2D;
                    double y = pos.getY() + 1;
                    double z = pos.getZ() + 0.5 + zDirection * 0.2D;
                    level.addParticle(ParticleTypes.BUBBLE, x, y, z, xDirection * 0.35F, 0, zDirection * 0.35F);
                }
            }
        }
    }

    public float getActiveRotation(float f) {
        if (this.getBlockState().getValue(WhirligigBlock.WATERLOGGED)) {
            return (this.activeRotation + f) * (level.isThundering() ? 0.25F : level.isRaining() ? 0.1F : 0.05F);
        } else {
            return (this.activeRotation + f) * (level.isThundering() ? 0.5F : level.isRaining() ? 0.2F : 0.1F);
        }
    }
}
