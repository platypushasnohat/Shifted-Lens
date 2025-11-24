package com.platypushasnohat.shifted_lens.mixins;

import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.FireChargeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(FireChargeItem.class)
public abstract class FireChargeItemMixin extends Item {

    public FireChargeItemMixin(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (ShiftedLensConfig.THROWABLE_FIRE_CHARGES.get()) {
            ItemStack itemStack = player.getItemInHand(hand);
            player.gameEvent(GameEvent.ITEM_INTERACT_START);
            level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
            player.getCooldowns().addCooldown(itemStack.getItem(), 20);

            if (!level.isClientSide) {
                SmallFireball fireCharge = new SmallFireball(level, player, player.getLookAngle().x(), player.getLookAngle().y(), player.getLookAngle().z());
                fireCharge.setPos(player.getX(), player.getEyePosition().y(), player.getZ());
                level.addFreshEntity(fireCharge);
            }

            player.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));

            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }

            return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
        } else {
            return super.use(level, player, hand);
        }
    }
}
