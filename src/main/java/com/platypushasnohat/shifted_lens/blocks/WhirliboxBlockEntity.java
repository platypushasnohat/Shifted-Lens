package com.platypushasnohat.shifted_lens.blocks;

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

    public static void tick(Level level, BlockPos pos, BlockState state, WhirliboxBlockEntity blockEntity) {
        boolean powered = state.getValue(WhirliboxBlock.POWERED);

        if (powered) {
            Direction direction = state.getValue(WhirliboxBlock.FACING);
            int power = level.getBestNeighborSignal(pos);
            blockEntity.push(state, direction, direction, power);
        }
    }

    private void push(BlockState state, Direction direction, Direction moveDirection, int power) {
        if (level == null) return;

        int blockDist = 1;
        for (; blockDist <= power; blockDist++) {
            worldPosition.relative(direction, blockDist);
        }

        if (blockDist > 1) {
            var entities = level.getEntities(null, new AABB(worldPosition).expandTowards(new Vec3(direction.step().mul(blockDist))));
            for (Entity entity : entities) {
                pushEntity(direction, entity);
            }
        }

        if (level.isClientSide()) {
            double xOff = direction.getStepX() * 0.06F;
            double yOff = direction.getStepY() * 0.06F;
            double zOff = direction.getStepZ() * 0.06F;

            for (int j = 1; j < blockDist; j++) {
                if (level.random.nextFloat() <= 0.35F) {
                    BlockPos targetPos = worldPosition.relative(direction, j);
                    double x = targetPos.getX() + getParticlePos(xOff, level.random);
                    double y = targetPos.getY() + getParticlePos(yOff, level.random);
                    double z = targetPos.getZ() + getParticlePos(zOff, level.random);
                    level.addParticle(SLParticles.WHIRLIWIND.get(), x, y, z, xOff, yOff, zOff);
                }
            }
        }
    }

    private double getParticlePos(double offset, RandomSource random) {
        return (offset == 0 ? 0.5F + (random.nextFloat() + random.nextFloat() - 1) / 2F : (0.5F * (random.nextFloat() - 1.25)));
    }

    private void pushEntity(Direction direction, Entity entity) {
        Vec3 vec3 = new Vec3(direction.step()).normalize().scale(0.09D);
        Vec3 vec31 = new Vec3(direction.step()).normalize().scale(0.055D);

        if (entity.isCrouching()) {
            entity.setDeltaMovement(entity.getDeltaMovement().add(vec31));
        } else {
            entity.setDeltaMovement(entity.getDeltaMovement().add(vec3));
        }
        if (entity instanceof FallingBlockEntity fallingBlock) {
            fallingBlock.time--;
        }
        entity.resetFallDistance();
    }
}
