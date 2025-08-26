package com.platypushasnohat.shifted_lens.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class LollipopItem extends Item {

    public LollipopItem(Properties properties) {
        super(properties);
    }

//    @Override
//    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
//        stack.hurtAndBreak(1, entity, (entity1) -> entity1.broadcastBreakEvent(InteractionHand.MAIN_HAND));
//        return this.isEdible() ? entity.eat(level, stack) : stack;
//    }
}
