package com.platypushasnohat.shifted_lens.blocks.blockentity;

import com.platypushasnohat.shifted_lens.blocks.WhirliboxBlock;
import com.platypushasnohat.shifted_lens.registry.SLBlockEntities;
import com.platypushasnohat.shifted_lens.registry.SLParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class WhirliboxBlockEntity extends BlockEntity {

    public WhirliboxBlockEntity(BlockPos pos, BlockState state) {
        super(SLBlockEntities.WHIRLIBOX_BLOCK_ENTITY.get(), pos, state);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, WhirliboxBlockEntity whirlibox) {
        if (state.getValue(WhirliboxBlock.POWERED)) {
            whirlibox.push(state, state.getValue(WhirliboxBlock.FACING), state.getValue(WhirliboxBlock.FACING), level.getBestNeighborSignal(pos));
        }
    }

    private void push(BlockState state, Direction direction, Direction moveDirection, int power) {
        if (level == null) return;

        int blockDist = 1;

        for (; blockDist <= power; blockDist++) {
            BlockPos targetPos = worldPosition.relative(direction, blockDist);
            BlockState targetState = level.getBlockState(targetPos);
            if (!(targetState.isAir() || targetState.getCollisionShape(level, targetPos).isEmpty())) break;
        }

        if (blockDist > 1) {
            var entities = level.getEntities(null, new AABB(worldPosition).expandTowards(new Vec3(direction.step().mul(blockDist))));
            for (Entity entity : entities) {
                Vec3 vec3 = new Vec3(direction.step()).normalize().scale(0.09D);
                Vec3 vec31 = new Vec3(direction.step()).normalize().scale(0.055D);

                if (entity.isCrouching()) entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
                else entity.setDeltaMovement(entity.getDeltaMovement().add(vec3));
                if (entity instanceof FallingBlockEntity fallingBlock) fallingBlock.time--;
                entity.resetFallDistance();
            }
        }

        if (level.isClientSide()) {
            double xOff = direction.getStepX() * 0.09F;
            double yOff = direction.getStepY() * 0.09F;
            double zOff = direction.getStepZ() * 0.09F;

            double particleOffset = moveDirection.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 1 : -1;

            for (int j = 1; j < blockDist; j++) {
                if (level.random.nextFloat() <= 0.2F) {
                    BlockPos targetPos = worldPosition.relative(direction, j);
                    double x = targetPos.getX() + getParticlePos(xOff, level.random, particleOffset);
                    double y = targetPos.getY() + getParticlePos(yOff, level.random, particleOffset);
                    double z = targetPos.getZ() + getParticlePos(zOff, level.random, particleOffset);
                    level.addParticle(SLParticles.WHIRLIWIND.get(), x, y, z, xOff, yOff, zOff);
                }
            }
        }
    }

    private double getParticlePos(double offset, RandomSource random, double offset1) {
        return (offset == 0 ? 0.5f + (random.nextFloat() + random.nextFloat() - 1) / 2f : (0.5f + offset1 * (random.nextFloat() - 1.25)));
    }
}
