package com.platypushasnohat.shifted_lens.entities;

import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;

public class SteelChariot extends Entity {

    private static final EntityDataAccessor<Integer> HURT_TIME = SynchedEntityData.defineId(SteelChariot.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> HURT_DURATION = SynchedEntityData.defineId(SteelChariot.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> DAMAGE = SynchedEntityData.defineId(SteelChariot.class, EntityDataSerializers.FLOAT);

    public SteelChariot(EntityType<?> entityType, Level level) {
        super(entityType, level);
        this.blocksBuilding = true;
    }

    @Override
    protected float getEyeHeight(Pose pose, EntityDimensions dimensions) {
        return dimensions.height;
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return canVehicleCollide(this, entity);
    }

    public static boolean canVehicleCollide(Entity entity, Entity entity1) {
        return (entity1.canBeCollidedWith() || entity1.isPushable()) && !entity.isPassengerOfSameVehicle(entity1);
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    protected void destroy(DamageSource source) {
        this.spawnAtLocation(this.getDropItem());
    }

    public Item getDropItem() {
        return SLItems.STEEL_CHARIOT.get();
    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getHurtTime() > 0) {
            this.setHurtTime(this.getHurtTime() - 1);
        }

        if (this.getDamage() > 0.0F) {
            this.setDamage(this.getDamage() - 1.0F);
        }

        if (!this.isNoGravity()) {
            double yVelocity = -0.04D;
            FluidType fluidType = this.getEyeInFluidType();

            if (fluidType != ForgeMod.EMPTY_TYPE.get()) {
                yVelocity *= this.getFluidMotionScale(fluidType);
            }

            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, yVelocity, 0.0D));
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        } else if (!this.level().isClientSide && !this.isRemoved()) {
            this.setHurtDuration(-this.getHurtDuration());
            this.setHurtTime(10);
            this.setDamage(this.getDamage() + amount * 10.0F);
            this.markHurt();
            this.gameEvent(GameEvent.ENTITY_DAMAGE, source.getEntity());
            boolean flag = source.getEntity() instanceof Player && ((Player)source.getEntity()).getAbilities().instabuild;
            if (flag || this.getDamage() > 40.0F) {
                if (!flag && this.level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                    this.destroy(source);
                }
                this.discard();
            }
            return true;
        } else {
            return true;
        }
    }

    public void animateHurt(float amount) {
        this.setHurtDuration(-this.getHurtDuration());
        this.setHurtTime(10);
        this.setDamage(this.getDamage() * 11.0F);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(HURT_TIME, 0);
        this.entityData.define(HURT_DURATION, 1);
        this.entityData.define(DAMAGE, 0.0F);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    public void setDamage(float damage) {
        this.entityData.set(DAMAGE, damage);
    }

    public float getDamage() {
        return this.entityData.get(DAMAGE);
    }

    public void setHurtTime(int hurtTime) {
        this.entityData.set(HURT_TIME, hurtTime);
    }

    public int getHurtTime() {
        return this.entityData.get(HURT_TIME);
    }

    public void setHurtDuration(int duration) {
        this.entityData.set(HURT_DURATION, duration);
    }

    public int getHurtDuration() {
        return this.entityData.get(HURT_DURATION);
    }
}
