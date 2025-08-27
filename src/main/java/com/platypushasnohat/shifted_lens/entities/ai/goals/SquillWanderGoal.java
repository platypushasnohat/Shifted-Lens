package com.platypushasnohat.shifted_lens.entities.ai.goals;

import com.platypushasnohat.shifted_lens.entities.Squill;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;
import java.util.Objects;

public class SquillWanderGoal extends Goal {

    private final Squill squill;
    private int cooldown;
    private double x, y, z;

    public SquillWanderGoal(Squill squill) {
        this.squill = squill;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.cooldown > 0) {
            this.cooldown--;
        } else {
            Level level = squill.level();
            int blockX = squill.getBlockX();
            int blockZ = squill.getBlockZ();
            int heightBelow = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockX, blockZ);
            boolean isAirBelow = level.getBlockState(new BlockPos(blockX, heightBelow, blockZ)).isAir();
            int blockY = squill.getBlockY();
            int maxHeight = (int) (squill.level().getMaxBuildHeight() / 1.25F);

            Vec3 randomPos;
            if ((!isAirBelow && blockY - heightBelow > 15) || (isAirBelow && blockY > maxHeight)) {
                RandomSource random = squill.getRandom();
                randomPos = squill.position().add(new Vec3(random.nextBoolean() ? (random.nextInt(12)) : (random.nextInt(12) * -1), -random.nextInt(32), random.nextBoolean() ? (random.nextInt(12)) : (random.nextInt(12) * -1)));
            } else {
                Vec3 view = squill.getViewVector(0.0F);
                randomPos = HoverRandomPos.getPos(squill, 32, 12, view.x, view.z, ((float) Math.PI / 2F), 3, 1);
            }
            if (randomPos != null) {
                this.x = randomPos.x();
                this.y = randomPos.y();
                this.z = randomPos.z();
                return true;
            }
        }
        return false;
    }

    @Override
    public void start() {
        squill.getMoveControl().setWantedPosition(this.x, this.y, this.z, 1.0F);
        this.cooldown = squill.getRandom().nextInt(80) + 60;
    }

    @Override
    public boolean canContinueToUse() {
        return this.squill.getMoveControl().hasWanted();
    }

    @Override
    public void stop() {
        this.squill.getNavigation().stop();
    }
}