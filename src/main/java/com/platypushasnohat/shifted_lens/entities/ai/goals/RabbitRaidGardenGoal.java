package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarrotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public class RabbitRaidGardenGoal extends MoveToBlockGoal {

    private final Rabbit rabbit;
    private boolean wantsToRaid;
    private boolean canRaid;

    public RabbitRaidGardenGoal(Rabbit rabbit) {
        super(rabbit, 0.7F, 16);
        this.rabbit = rabbit;
    }

    @Override
    public boolean canUse() {
     if (this.nextStartTick <= 0) {
        if (!ForgeEventFactory.getMobGriefingEvent(this.rabbit.level(), this.rabbit)) {
            return false;
        }
        this.canRaid = false;
        this.wantsToRaid = this.rabbit.wantsMoreFood();
        }
        return super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
     return this.canRaid && super.canContinueToUse();
    }

    @Override
    public void tick() {
        super.tick();
        this.rabbit.getLookControl().setLookAt((double) this.blockPos.getX() + 0.5D, this.blockPos.getY() + 1, (double) this.blockPos.getZ() + 0.5D, 10.0F, (float) this.rabbit.getMaxHeadXRot());
        if (this.isReachedTarget()) {
        Level level = this.rabbit.level();
        BlockPos blockpos = this.blockPos.above();
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        if (this.canRaid && block instanceof CarrotBlock) {
            int i = blockstate.getValue(CarrotBlock.AGE);
            if (i == 0) {
                level.setBlock(blockpos, Blocks.AIR.defaultBlockState(), 2);
                level.destroyBlock(blockpos, true, this.rabbit);
            } else {
                level.setBlock(blockpos, blockstate.setValue(CarrotBlock.AGE, i - 1), 2);
                level.levelEvent(2001, blockpos, Block.getId(blockstate));
            }
            this.rabbit.moreCarrotTicks = 40;
            }
            this.canRaid = false;
            this.nextStartTick = 10;
        }
    }

    @Override
    protected boolean isValidTarget(LevelReader level, BlockPos pos) {
    BlockState blockstate = level.getBlockState(pos);
    if (blockstate.is(Blocks.FARMLAND) && this.wantsToRaid && !this.canRaid) {
        blockstate = level.getBlockState(pos.above());
        if (blockstate.getBlock() instanceof CarrotBlock && ((CarrotBlock)blockstate.getBlock()).isMaxAge(blockstate)) {
            this.canRaid = true;
            return true;
        }
    }
    return false;
    }
}