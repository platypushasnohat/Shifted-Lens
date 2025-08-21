package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.config.SLConfig;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractHurtingProjectile.class)
public abstract class AbstractHurtingProjectileMixin extends Entity {

    public AbstractHurtingProjectileMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"))
    private void waterKillsFire(CallbackInfo ci) {
        AbstractHurtingProjectile projectile = ((AbstractHurtingProjectile) (Object) this);
        if (projectile instanceof Fireball && SLConfig.FIREBALLS_EXTINGUISH.get()) {
            if (projectile.isInWaterOrBubble()) {
                for (int i = 0; i < 3; i++) {
                    projectile.level().addParticle(ParticleTypes.LARGE_SMOKE, projectile.getRandomX(0.5), projectile.getRandomY(), projectile.getRandomZ(0.5), 0, 0, 0);
                }
                projectile.playSound(SoundEvents.FIRE_EXTINGUISH, 0.5F, 1.0F);
                projectile.discard();
            }
        }
    }
}
