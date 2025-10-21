package com.platypushasnohat.shifted_lens.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class SLGhastFlightGoal extends Goal {

    private final Ghast ghast;
    private final int rangeXZ;
    private final int rangeY;
    private final int chance;
    private final float speed;
    private Vec3 moveToPoint = null;

    public SLGhastFlightGoal(Ghast ghast, int rangeXZ, int rangeY, int chance, float speed) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.ghast = ghast;
        this.rangeXZ = rangeXZ;
        this.rangeY = rangeY;
        this.chance = chance;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        return ghast.getRandom().nextInt(chance) == 0 && !ghast.getMoveControl().hasWanted();
    }

    @Override
    public void stop() {
        moveToPoint = null;
    }

    @Override
    public boolean canContinueToUse() {
        return ghast.getMoveControl().hasWanted() && ghast.distanceToSqr(moveToPoint) > 1F;
    }

    @Override
    public void start() {
        moveToPoint = this.getRandomLocation();
        if (moveToPoint != null) {
            ghast.getMoveControl().setWantedPosition(moveToPoint.x, moveToPoint.y, moveToPoint.z, (ghast.getTarget() != null && ghast.getTarget().isAlive()) ? speed * 1.5F : speed);
        }
    }

    @Nullable
    private Vec3 getRandomLocation() {
        RandomSource random = ghast.getRandom();
        BlockPos blockpos = null;
        BlockPos origin = ghast.hasRestriction() ? this.ghast.getRestrictCenter() : ghast.blockPosition();
        for (int i = 0; i < 15; i++) {
            BlockPos blockpos1 = origin.offset(random.nextInt(rangeXZ * 2) - rangeXZ, random.nextInt(rangeY * 2) - rangeY, random.nextInt(rangeXZ * 2) - rangeXZ);
            if (canBlockPosBeSeen(blockpos1) && this.ghast.level().isEmptyBlock(blockpos1)) {
                blockpos = blockpos1;
            }
        }
        return blockpos == null ? null : new Vec3(blockpos.getX() + 0.5D, blockpos.getY() + 0.5D, blockpos.getZ() + 0.5D);
    }

    public boolean canBlockPosBeSeen(BlockPos pos) {
        double x = pos.getX() + 0.5F;
        double y = pos.getY() + 0.5F;
        double z = pos.getZ() + 0.5F;
        HitResult result = ghast.level().clip(new ClipContext(new Vec3(ghast.getX(), ghast.getY() + (double) ghast.getEyeHeight(), ghast.getZ()), new Vec3(x, y, z), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, ghast));
        double dist = result.getLocation().distanceToSqr(x, y, z);
        return dist <= 1.0D || result.getType() == HitResult.Type.MISS;
    }
}