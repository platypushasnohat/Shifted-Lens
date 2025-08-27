package com.platypushasnohat.shifted_lens.entities.ai.goals;

import com.platypushasnohat.shifted_lens.entities.FlyingFish;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlyingFishGlideGoal extends Goal {

    private final FlyingFish fish;
    private BlockPos surfacePos;

    public FlyingFishGlideGoal(FlyingFish fish) {
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        this.fish = fish;
    }

    @Override
    public boolean canUse() {
        if (!fish.isInWaterOrBubble()) {
            return false;
        } else if (fish.glideCooldown == 0) {
            BlockPos surfacePos = findSurfacePos();
            if (surfacePos != null) {
                this.surfacePos = surfacePos;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return surfacePos != null && (!fish.onGround() || fish.isInWaterOrBubble());
    }

    @Override
    public void stop() {
        this.fish.setGliding(false);
        this.fish.glideCooldown = fish.getRandom().nextInt(50 * 8 * 4) + 180;
        this.surfacePos = null;
    }

    @Override
    public void tick() {
        if (fish.isInWaterOrBubble() && fish.distanceToSqr(Vec3.atCenterOf(surfacePos)) > 3F) {
            fish.getNavigation().moveTo(surfacePos.getX() + 0.5F, surfacePos.getY() + 1F, surfacePos.getZ() + 0.5F, 1.4F);
            if (fish.isGliding()) {
                stop();
            }
        } else {
            this.fish.getNavigation().stop();
            double y = 0;
            if (!fish.isGliding()) {
                y = 1F + (fish.getRandom().nextFloat() * 0.2F);
            } else if (fish.isGliding() && fish.isInWaterOrBubble()) {
                stop();
            }
            Direction direction = this.fish.getMotionDirection();
            Vec3 movement = new Vec3(direction.getStepX(), 0, direction.getStepZ()).normalize().scale(0.4F);
            Vec3 glide = new Vec3(movement.x, y, movement.z);
            fish.setYRot(((float) Mth.atan2(glide.z, glide.x)) * Mth.RAD_TO_DEG - 90F);
            fish.yRotO = (float) Mth.atan2(glide.z, glide.x) * Mth.RAD_TO_DEG - 90F;
            fish.yBodyRot = (float) Mth.atan2(glide.z, glide.x) * Mth.RAD_TO_DEG - 90F;
            fish.yHeadRot = (float) Mth.atan2(glide.z, glide.x) * Mth.RAD_TO_DEG - 90F;
            fish.setDeltaMovement(glide);
            this.fish.setGliding(true);
        }
    }

    private BlockPos findSurfacePos(){
        BlockPos fishPos = fish.blockPosition();
        for (int i = 0; i < 15; i++) {
            BlockPos offset = fishPos.offset(fish.getRandom().nextInt(16) - 8, 0, fish.getRandom().nextInt(16) - 8);
            while (fish.level().isWaterAt(offset) && offset.getY() < fish.level().getMaxBuildHeight()) {
                offset = offset.above();
            }
            if (!fish.level().isWaterAt(offset) && fish.level().isWaterAt(offset.below()) && canSeeBlock(offset)) {
                return offset;
            }
        }
        return null;
    }

    private boolean canSeeBlock(BlockPos destinationBlock) {
        Vec3 Vector3d = new Vec3(fish.getX(), fish.getEyeY(), fish.getZ());
        Vec3 blockVec = net.minecraft.world.phys.Vec3.atCenterOf(destinationBlock);
        BlockHitResult result = fish.level().clip(new ClipContext(Vector3d, blockVec, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, fish));
        return result.getBlockPos().equals(destinationBlock);
    }
}