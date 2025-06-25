package com.platypushasnohat.shifted_lens.entities.projectiles;

import com.platypushasnohat.shifted_lens.registry.SLEntities;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class GhastFireball extends Fireball {

    private int explosionPower = 1;

    public GhastFireball(EntityType<? extends GhastFireball> fireball, Level level) {
        super(fireball, level);
    }

    public GhastFireball(Level level, LivingEntity entity, double x, double y, double z) {
        super(SLEntities.GHAST_FIREBALL.get(), entity, x, y, z, level);
    }

    public GhastFireball(Level level, LivingEntity entity, double x, double y, double z, int explosionPower) {
        super(SLEntities.GHAST_FIREBALL.get(), entity, x, y, z, level);
        this.explosionPower = explosionPower;
    }

    @Override
    public ItemStack getItem() {
        ItemStack itemStack = this.getItemRaw();
        return itemStack.isEmpty() ? new ItemStack(SLItems.INFERNO_CHARGE.get()) : itemStack;
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        if (!this.level().isClientSide) {
            boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level(), this.getOwner());
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionPower, flag, Level.ExplosionInteraction.MOB);
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        if (!this.level().isClientSide) {
            Entity entity = hitResult.getEntity();
            Entity owner = this.getOwner();
            entity.hurt(this.damageSources().fireball(this, owner), 6.0F);
            if (owner instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity) owner, entity);
            }
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putByte("ExplosionPower", (byte)this.explosionPower);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if (compoundTag.contains("ExplosionPower", 99)) {
            this.explosionPower = compoundTag.getByte("ExplosionPower");
        }
    }
}
