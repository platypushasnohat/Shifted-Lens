package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.entities.*;
import com.platypushasnohat.shifted_lens.entities.ai.goals.*;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID)
public class ServerEvents {

    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide && entity instanceof Squill squill) {
            squill.updatePull(entity.position());
        }

        if (entity instanceof Mob mob) {
            if (mob instanceof Ghast ghast) {
                ghast.goalSelector.addGoal(0, new SLGhastWanderGoal(ghast));
                ghast.goalSelector.addGoal(1, new Ghast.GhastLookGoal(ghast));
                ghast.goalSelector.addGoal(2, new SLGhastAttackGoal(ghast));
            }

            if (mob instanceof Guardian guardian) {
                guardian.goalSelector.addGoal(2, new SLGuardianAttackGoal(guardian));
                guardian.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(guardian, LivingEntity.class, 20, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.GUARDIAN_TARGETS) && livingEntity.distanceToSqr(guardian) > 7.0D));
            }
            if (mob instanceof ElderGuardian guardian) {
                guardian.goalSelector.addGoal(2, new SLGuardianAttackGoal(guardian));
                guardian.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(guardian, LivingEntity.class, 20, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.GUARDIAN_TARGETS) && livingEntity.distanceToSqr(guardian) > 7.0D));
            }
        }
    }

    @SubscribeEvent
    public static void onInteractEntity(PlayerInteractEvent.EntityInteract event) {
        Entity entity = event.getTarget();
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (stack.getItem() == Items.WATER_BUCKET && entity.isAlive() && entity instanceof Squid squid) {
            squid.playSound(SoundEvents.BUCKET_FILL_FISH, 1.0F, 1.0F);
            ItemStack bucket;
            if (squid.getType() == EntityType.SQUID) {
                bucket = new ItemStack(SLItems.SQUID_BUCKET.get());
            } else {
                return;
            }

            Bucketable.saveDefaultDataToBucketTag(squid, bucket);
            ItemStack itemstack2 = ItemUtils.createFilledResult(stack, player, bucket, false);
            player.setItemInHand(event.getHand(), itemstack2);
            if (!event.getLevel().isClientSide()) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, bucket);
            }

            entity.discard();
            event.setCanceled(true);
            event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide()));
        }
    }


}
