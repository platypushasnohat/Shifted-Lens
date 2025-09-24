package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SLRabbitPanicGoal extends Goal {

    protected final Rabbit rabbit;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public SLRabbitPanicGoal(Rabbit rabbit, double speedModifier) {
        this.rabbit = rabbit;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.shouldPanic()) {
            return false;
        } else {
            if (this.rabbit.isOnFire()) {
                BlockPos blockpos = this.lookForWater(this.rabbit.level(), this.rabbit, 5);
                if (blockpos != null) {
                    this.posX = blockpos.getX();
                    this.posY = blockpos.getY();
                    this.posZ = blockpos.getZ();
                    return true;
                }
            }
            return this.findRandomPosition();
        }
    }

    protected boolean shouldPanic() {
        return (this.rabbit.getLastHurtByMob() != null || this.rabbit.isFreezing() || this.rabbit.isOnFire()) && this.rabbit.getVariant() != Rabbit.Variant.EVIL;
    }

    protected boolean findRandomPosition() {
        Vec3 vec3 = DefaultRandomPos.getPos(this.rabbit, 10, 7);
        if (vec3 == null) {
            return false;
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void start() {
        this.rabbit.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.rabbit.getNavigation().isDone();
    }

    @Nullable
    protected BlockPos lookForWater(BlockGetter blockGetter, Entity entity, int distance) {
        BlockPos blockpos = entity.blockPosition();
        return !blockGetter.getBlockState(blockpos).getCollisionShape(blockGetter, blockpos).isEmpty() ? null : BlockPos.findClosestMatch(entity.blockPosition(), distance, 1, (blockPos) -> blockGetter.getFluidState(blockPos).is(FluidTags.WATER)).orElse(null);
    }
}