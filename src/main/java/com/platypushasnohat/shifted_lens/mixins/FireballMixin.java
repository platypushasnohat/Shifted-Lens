package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.config.SLConfig;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Fireball.class)
public class FireballMixin extends AbstractHurtingProjectile {

    protected FireballMixin(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected boolean shouldBurn() {
        return SLConfig.FIREBALL_BURNING.get();
    }
}
