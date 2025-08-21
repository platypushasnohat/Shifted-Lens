package com.platypushasnohat.shifted_lens.items;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;

public class InfernoChargeItem extends Item {

    public InfernoChargeItem(Properties properties) {
        super(properties);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        player.gameEvent(GameEvent.ITEM_INTERACT_START);

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 0.5F, 0.8F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(itemStack.getItem(), 60);

        if (!level.isClientSide) {
            LargeFireball fireball = new LargeFireball(level, player, player.getLookAngle().x(), player.getLookAngle().y(), player.getLookAngle().z(), 1);
            fireball.setItem(itemStack);
            fireball.setPos(player.getX(), player.getEyePosition().y(), player.getZ());
            level.addFreshEntity(fireball);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }
}
