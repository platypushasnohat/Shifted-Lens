package com.platypushasnohat.shifted_lens.entities;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;

public class Squill extends PathfinderMob implements FlyingAnimal {

    private Vec3 prevPull = Vec3.ZERO, pull = Vec3.ZERO;

    public final AnimationState idleAnimationState = new AnimationState();
    public final AnimationState aggroAnimationState = new AnimationState();

    public Squill(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new SquillMoveController(this);
        setPersistenceRequired();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.FLYING_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SquillAttackGoal(this));
        this.goalSelector.addGoal(2, new SquillSchoolGoal(this));
        this.goalSelector.addGoal(3, new AirSwimGoal(this));
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this));
    }

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
    protected PathNavigation createNavigation(Level level) {
        SquillPathNavigation navigation = new SquillPathNavigation(this, level);
        return navigation;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isEffectiveAi()) {
            this.moveRelative(0.1F, travelVector);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.8D));
        } else {
            super.travel(travelVector);
        }
    }

    @Override
    public boolean isFlying() {
        return !this.onGround();
    }

    public void updatePull(Vec3 pos) {
        this.prevPull = this.pull = pos.subtract(0.0F, 1.0F, 0.0F);
    }

    public static Vec3 lerp(Vec3 prev, Vec3 current, float ptc) {
        return prev.add(current.subtract(prev).scale(ptc));
    }

    public Vec3 getPull(float partialTicks) {
        return lerp(this.prevPull, this.pull, partialTicks);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            setupAnimationStates();

            this.prevPull = this.pull;
            Vec3 pos = this.position();
            this.pull = pos.add(this.pull.subtract(pos).normalize().scale(0.25F));
        }
    }

    public void setupAnimationStates() {
        this.idleAnimationState.animateWhen(this.isAlive() && !this.isAggressive(), this.tickCount);
        this.aggroAnimationState.animateWhen(this.isAlive() && this.isAggressive(), this.tickCount);
    }

    @Override
    public AABB getBoundingBoxForCulling() {
        return this.getBoundingBox().inflate(3, 3, 3);
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double distance) {
        return Math.sqrt(distance) < 1024.0D;
    }

    // goals
    static class AirSwimGoal extends Goal {

        private final Squill squill;
        private int cooldown;
        private double x, y, z;

        public AirSwimGoal(Squill squill) {
            this.squill = squill;
            this.setFlags(EnumSet.of(Flag.MOVE));
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
                Vec3 randomPos;
                if ((!isAirBelow && blockY - heightBelow > 15) || (isAirBelow && blockY > level.getSeaLevel() + 15)) {
                    RandomSource random = squill.getRandom();
                    randomPos = squill.position().add(new Vec3(random.nextBoolean() ? (random.nextInt(12)) : (random.nextInt(12) * -1), -random.nextInt(16), random.nextBoolean() ? (random.nextInt(12)) : (random.nextInt(12) * -1)));
                } else {
                    Vec3 view = squill.getViewVector(0.0F);
                    randomPos = HoverRandomPos.getPos(squill, 32, 16, view.x, view.z, ((float) Math.PI / 2F), 3, 1);
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
            this.cooldown = squill.getRandom().nextInt(40) + 20;
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

    static class SquillAttackGoal extends Goal {

        private final Squill squill;
        @Nullable
        private Path path;
        private int delayCounter;

        public SquillAttackGoal(Squill squill) {
            this.squill = squill;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (shouldFollowTarget(squill)) {
                this.path = squill.getNavigation().createPath(squill.getTarget(), 0);
                return this.path != null;
            }
            return false;
        }

        @Override
        public void start() {
            squill.getNavigation().moveTo(this.path, 2F);
            squill.setAggressive(true);
            this.delayCounter = 0;
        }

        @Override
        public boolean canContinueToUse() {
            return shouldFollowTarget(squill) && squill.getNavigation().isInProgress();
        }

        @Override
        public void tick() {
            this.delayCounter = Math.max(this.delayCounter - 1, 0);
            LivingEntity target = squill.getTarget();
            double distanceToTargetSq = squill.distanceToSqr(target);
            RandomSource random = squill.getRandom();
            if (this.delayCounter <= 0 && random.nextFloat() <= 0.05F) {
                this.delayCounter = 20 + random.nextInt(10);
                PathNavigation pathNavigator = squill.getNavigation();
                if (distanceToTargetSq >= 5.0F) {
                    Path path = pathNavigator.createPath(findAirPosAboveTarget(squill.level(), target), 0);
                    if (path == null || !pathNavigator.moveTo(path, 5F)) {
                        this.delayCounter += 60;
                    }
                } else {
                    squill.getMoveControl().setWantedPosition(target.getX(), target.getY(), target.getZ(), 5F);
                }
            }

            if (distanceToTargetSq <= getAttackReachSqr(target)) {
                squill.doHurtTarget(target);
                squill.swing(InteractionHand.MAIN_HAND);
            }
        }

        protected double getAttackReachSqr(LivingEntity target) {
            return squill.getBbWidth() * 1.9F * squill.getBbWidth() * 1.9F + target.getBbWidth();
        }

        @Override
        public void stop() {
            LivingEntity livingentity = squill.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
                squill.setTarget(null);
            }
            squill.setAggressive(false);
            squill.getNavigation().stop();
        }

        public static boolean shouldFollowTarget(Squill squill) {
            LivingEntity attackTarget = squill.getTarget();
            return attackTarget != null && attackTarget.isAlive() && (!(attackTarget instanceof Player) || !attackTarget.isSpectator() && !((Player) attackTarget).isCreative());
        }

        public static BlockPos findAirPosAboveTarget(Level world, LivingEntity target) {
            BlockPos.MutableBlockPos mutable = target.blockPosition().mutable();
            int maxHeight = target.getRandom().nextInt(3) + 5;
            for (int y = 0; y < maxHeight; y++) {
                mutable.move(0, 1, 0);
                if (!world.isEmptyBlock(mutable)) {
                    mutable.move(0, -1, 0);
                    break;
                }
            }
            return mutable;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }

    static class SquillSchoolGoal extends Goal {
        private final Squill squill;
        @Nullable
        public Squill leader;
        private int timeToRecalcPath;

        public SquillSchoolGoal(Squill purp) {
            this.squill = purp;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            List<? extends Squill> list = squill.level().getEntitiesOfClass(Squill.class, squill.getBoundingBox().inflate(12.0D, 12.0D, 12.0D));
            Squill newLeader = null;
            double smallestDistance = Double.MAX_VALUE;
            for (Squill squills : list) {
                double distance = squill.distanceToSqr(squills);
                if (distance < smallestDistance) {
                    smallestDistance = distance;
                    newLeader = squills;
                }
            }

            if (newLeader == null || smallestDistance < 9.0D) return false;
            this.leader = newLeader;
            return true;
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
        }

        @Override
        public void tick() {
            if (--this.timeToRecalcPath <= 0) {
                this.timeToRecalcPath = this.adjustedTickDelay(10);
                this.squill.getNavigation().moveTo(this.leader, 1.0F);
            }
        }

        @Override
        public boolean canContinueToUse() {
            if (!squill.isBaby() || leader == null || !leader.isAlive()) return false;
            double distanceToLeader = squill.distanceToSqr(leader);
            return distanceToLeader >= 9.0D && distanceToLeader <= 256.0D;
        }

        @Override
        public void stop() {
            this.leader = null;
            this.squill.getNavigation().stop();
        }
    }

    static class SquillMoveController extends MoveControl {

        private Squill squill;
        private Vec3 prevPos;
        private int stuckTicks;

        public SquillMoveController(Squill squill) {
            super(squill);
            this.prevPos = squill.position();
            this.squill = squill;
        }

        @Override
        public void setWantedPosition(double x, double y, double z, double speedIn) {
            super.setWantedPosition(x, y, z, speedIn);
            this.stuckTicks = 0;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                Vec3 pos = squill.position();
                double x = pos.x();
                double z = pos.z();
                Vec3 vector3d = new Vec3(this.wantedX - x, this.wantedY - pos.y(), this.wantedZ - z);
                double distance = vector3d.length();
                if (distance <= 0.2F) {
                    this.operation = Operation.WAIT;
                } else {
                    double dx = vector3d.x;
                    double dz = vector3d.z;
                    squill.setYRot(squill.yBodyRot = this.rotlerp(squill.getYRot(), (float) (Mth.atan2(dz, dx) * (double) (180F / (float) Math.PI)) - 90.0F, 90.0F));
                    float newMoveSpeed = Mth.lerp(0.125F, squill.getSpeed(), (float) (this.speedModifier * squill.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    squill.setSpeed(newMoveSpeed);
                    double normalizedY = vector3d.y / distance;
                    squill.setDeltaMovement(squill.getDeltaMovement().add(0.0F, newMoveSpeed * normalizedY * 0.1D, 0.0F));
                    LookControl lookControl = squill.getLookControl();
                    double d11 = lookControl.getWantedX();
                    double d12 = lookControl.getWantedY();
                    double d13 = lookControl.getWantedZ();
                    double d8 = x + (dx / distance) * 2.0D;
                    double d9 = squill.getEyeY() + normalizedY / distance;
                    double d10 = z + (dz / distance) * 2.0D;
                    if (!lookControl.isLookingAtTarget()) {
                        d11 = d8;
                        d12 = d9;
                        d13 = d10;
                    }

                    lookControl.setLookAt(Mth.lerp(0.125D, d11, d8), Mth.lerp(0.125D, d12, d9), Mth.lerp(0.125D, d13, d10), 10.0F, 40.0F);

                    if (this.prevPos.distanceToSqr(pos) <= 0.005F) {
                        if (++this.stuckTicks >= 60) {
                            this.operation = Operation.WAIT;
                        }
                    } else {
                        this.stuckTicks = 0;
                    }
                }
            } else {
                squill.setSpeed(0.0F);
                squill.setDeltaMovement(squill.getDeltaMovement().add(0.0F, 0.01F, 0.0F));
            }
            this.prevPos = squill.position();
        }
    }

    static class SquillPathNavigation extends PathNavigation {

        public SquillPathNavigation(Mob mob, Level worldIn) {
            super(mob, worldIn);
        }

        @Override
        protected PathFinder createPathFinder(int idleTime) {
            this.nodeEvaluator = new FlyNodeEvaluator();
            this.nodeEvaluator.setCanPassDoors(true);
            return new PathFinder(this.nodeEvaluator, idleTime);
        }

        @Override
        protected boolean canUpdatePath() {
            return !this.isInLiquid();
        }

        @Override
        protected Vec3 getTempMobPos() {
            return new Vec3(this.mob.getX(), this.mob.getY() + (double) this.mob.getBbHeight() * 0.5D, this.mob.getZ());
        }

        @Override
        public void tick() {
            this.tick++;
            if (this.hasDelayedRecomputation) {
                this.recomputePath();
            }

            if (!this.isDone()) {
                if (this.canUpdatePath()) {
                    this.followThePath();
                } else if (this.path != null && this.path.getNextNodeIndex() < this.path.getNodeCount()) {
                    Vec3 Vector3d = this.path.getEntityPosAtNode(this.mob, this.path.getNextNodeIndex());
                    if (Mth.floor(this.mob.getX()) == Mth.floor(Vector3d.x) && Mth.floor(this.mob.getY()) == Mth.floor(Vector3d.y) && Mth.floor(this.mob.getZ()) == Mth.floor(Vector3d.z)) {
                        this.path.setNextNodeIndex(this.path.getNextNodeIndex() + 1);
                    }
                }

                DebugPackets.sendPathFindingPacket(this.level, this.mob, this.path, this.maxDistanceToWaypoint);

                if (!this.isDone()) {
                    Vec3 Vector3d1 = this.path.getNextEntityPos(this.mob);
                    this.mob.getMoveControl().setWantedPosition(Vector3d1.x, Vector3d1.y, Vector3d1.z, this.speedModifier);
                }
            }
        }

        @Override
        protected void followThePath() {
            if (this.path != null) {
                Vec3 entityPos = this.getTempMobPos();
                float f = this.mob.getBbWidth();
                float f1 = f > 0.75F ? f / 2.0F : 0.75F - f / 2.0F;
                Vec3 Vector3d1 = this.mob.getDeltaMovement();
                if (Math.abs(Vector3d1.x) > 0.2D || Math.abs(Vector3d1.z) > 0.2D) {
                    f1 = (float) ((double) f1 * Vector3d1.length() * 6.0D);
                }

                Vec3 Vector3d2 = Vec3.atBottomCenterOf(this.path.getNextNodePos());
                if (Math.abs(this.mob.getX() - (Vector3d2.x + 0.5D)) < (double) f1 && Math.abs(this.mob.getZ() - (Vector3d2.z + 0.5D)) < (double) f1 && Math.abs(this.mob.getY() - Vector3d2.y) < (double) (f1 * 2.0F)) {
                    this.path.advance();
                }

                for (int j = Math.min(this.path.getNextNodeIndex() + 6, this.path.getNodeCount() - 1); j > this.path.getNextNodeIndex(); --j) {
                    Vector3d2 = this.path.getEntityPosAtNode(this.mob, j);
                    if (!(Vector3d2.distanceToSqr(entityPos) > 36.0D) && this.canMoveDirectly(entityPos, Vector3d2)) {
                        this.path.setNextNodeIndex(j);
                        break;
                    }
                }

                this.doStuckDetection(entityPos);
            }
        }

        @Override
        protected void doStuckDetection(Vec3 positionVec3) {
            if (this.tick - this.lastStuckCheck > 100) {
                if (positionVec3.distanceToSqr(this.lastStuckCheckPos) < 2.25D) {
                    this.stop();
                }
                this.lastStuckCheck = this.tick;
                this.lastStuckCheckPos = positionVec3;
            }

            if (this.path != null && !this.path.isDone()) {
                Vec3i vector3i = this.path.getNextNodePos();
                if (vector3i.equals(this.timeoutCachedNode)) {
                    this.timeoutTimer += Util.getMillis() - this.lastTimeoutCheck;
                } else {
                    this.timeoutCachedNode = vector3i;
                    double d0 = positionVec3.distanceTo(Vec3.atCenterOf(this.timeoutCachedNode));
                    this.timeoutLimit = this.mob.getSpeed() > 0.0F ? d0 / this.mob.getSpeed() * 100.0D : 0.0D;
                }

                if (this.timeoutLimit > 0.0D && this.timeoutTimer > this.timeoutLimit * 2.0D) {
                    this.timeoutCachedNode = Vec3i.ZERO;
                    this.timeoutTimer = 0L;
                    this.timeoutLimit = 0.0D;
                    this.stop();
                }

                this.lastTimeoutCheck = Util.getMillis();
            }
        }

        @Override
        protected boolean canMoveDirectly(Vec3 posVec31, Vec3 posVec32) {
            Vec3 Vector3d = new Vec3(posVec32.x, posVec32.y + (double) this.mob.getBbHeight() * 0.5D, posVec32.z);
            return this.level.clip(new ClipContext(posVec31, Vector3d, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob)).getType() == HitResult.Type.MISS;
        }

        @Override
        public boolean isStableDestination(BlockPos pos) {
            return this.level.isEmptyBlock(pos);
        }
    }
}
