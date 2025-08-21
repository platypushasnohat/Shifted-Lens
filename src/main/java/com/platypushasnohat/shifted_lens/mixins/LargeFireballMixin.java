package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LargeFireball.class)
public abstract class LargeFireballMixin extends Fireball {
    public LargeFireballMixin(EntityType<? extends Fireball> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack itemstack = this.getItemRaw();
        return itemstack.isEmpty() ? new ItemStack(SLItems.INFERNO_CHARGE.get()) : itemstack;
    }
}
