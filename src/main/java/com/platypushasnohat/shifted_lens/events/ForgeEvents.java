package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ForgeEvents {

    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();

        if (stack.is(Items.BUCKET) && ShiftedLensConfig.MILKABLE_SQUIDS.get() && event.getTarget() instanceof Squid) {
            player.playSound(SoundEvents.COW_MILK, 1.0F, 1.0F);
            ItemStack result = ItemUtils.createFilledResult(stack, player, Items.MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(event.getHand(), result);
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
        }
    }

    @SubscribeEvent
    public static void onEntitySize(EntityEvent.Size event) {
        Entity entity = event.getEntity();
        if (entity instanceof Rabbit rabbit) {
            if (rabbit.isBaby()) {
                event.setNewSize(EntityDimensions.scalable(0.3F, 0.4F), true);
            } else {
                event.setNewSize(EntityDimensions.scalable(0.6F, 0.7F), true);
            }
        }
    }
}
