package com.platypushasnohat.shifted_lens.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class Squill extends PathfinderMob implements FlyingAnimal {

    public Squill(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
//        this.moveControl = new AirSwimmingMoveControl(this, 85, 10, 1.0F, false);
//        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 1.0F)
                .add(Attributes.FLYING_SPEED, 1.0F);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AirSwimGoal(this));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

//    @Override
//    public boolean isNoGravity() {
//        return true;
//    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        FlyingPathNavigation navigation = new FlyingPathNavigation(this, pLevel);
        navigation.setCanOpenDoors(false);
        navigation.setCanFloat(true);
        navigation.setCanPassDoors(true);
        return navigation;
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi()) {
            this.moveRelative(0.01F, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9D));
            if (this.getTarget() == null) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.005D, 0.0D));
            }
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public float getWalkTargetValue(BlockPos pPos, LevelReader pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    // goals
    static class AirSwimGoal extends Goal {

        protected final PathfinderMob mob;
        private int preferredAltitude = -1;
        private int wanderAttemptsUntilAltitudeChange = 0;

        public AirSwimGoal(PathfinderMob mob) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.mob = mob;
        }

        public boolean canUse() {
            return mob.getNavigation().isDone() && mob.getRandom().nextInt(10) == 0;
        }

        public boolean canContinueToUse() {
            return mob.getNavigation().isInProgress();
        }

        public void start() {
            Vec3 vec3 = this.findPos();
            if (vec3 != null) {
                mob.getNavigation().moveTo(mob.getNavigation().createPath(BlockPos.containing(vec3), 1), 1.0);
            }
        }

        public Vec3 findPos() {
            Vec3 lookVec = mob.getViewVector(0.0F);

            if (preferredAltitude < 0 || wanderAttemptsUntilAltitudeChange <= 0) {
                BlockPos mobPos = mob.blockPosition();
                Level level = mob.level();

                int groundY = level.getHeight(Heightmap.Types.WORLD_SURFACE, mobPos.getX(), mobPos.getZ());
                int maxY = level.getMaxBuildHeight();
                int minAltitude = groundY + 40;
                int maxAltitude = Math.min(groundY + 80, maxY - 32);
                preferredAltitude = Mth.nextInt(mob.getRandom(), minAltitude, maxAltitude);
                wanderAttemptsUntilAltitudeChange = 16 + mob.getRandom().nextInt(16);
            } else {
                wanderAttemptsUntilAltitudeChange--;
            }

            double offsetX = lookVec.x * 8 + mob.getRandom().nextInt(5) - 2;
            double offsetZ = lookVec.z * 8 + mob.getRandom().nextInt(5) - 2;

            return new Vec3(mob.getX() + offsetX, preferredAltitude, mob.getZ() + offsetZ);
        }
    }
}
