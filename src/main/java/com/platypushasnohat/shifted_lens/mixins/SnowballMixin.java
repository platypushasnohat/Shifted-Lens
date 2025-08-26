package com.platypushasnohat.shifted_lens.mixins;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public abstract class SnowballMixin {

    @Inject(method = "onHitEntity(Lnet/minecraft/world/phys/EntityHitResult;)V", at = @At("TAIL"))
    protected void onHitEntity(EntityHitResult hitResult, CallbackInfo ci) {
        Entity entity = hitResult.getEntity();
        entity.setTicksFrozen(Math.min(entity.getTicksRequiredToFreeze() * 4, entity.getTicksFrozen() + 60));
    }
}
