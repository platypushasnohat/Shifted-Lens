package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.registry.tags.SLDamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Camel.class)
public abstract class CamelMixin extends Entity {

    protected CamelMixin(EntityType<? extends Entity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        return super.isInvulnerableTo(source) || source.is(SLDamageTypeTags.CAMEL_IMMUNE_TO);
    }
}
