package com.platypushasnohat.shifted_lens.mixins;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public final class ItemMixin {

    @Inject(at = @At("HEAD"), method = "use", cancellable = true)
    public void use(Level level, Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> callbackInfo) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (itemStack.is(Items.FIRE_CHARGE)) {
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.getCooldowns().addCooldown(itemStack.getItem(), 10);

            if (!level.isClientSide) {
                SmallFireball fireCharge = new SmallFireball(level, player, player.getLookAngle().x(), player.getLookAngle().y(), player.getLookAngle().z());
                fireCharge.setPos(player.getX(), player.getEyePosition().y(), player.getZ());
                level.addFreshEntity(fireCharge);
            }

            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            callbackInfo.setReturnValue(InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide()));
        }
    }
}
