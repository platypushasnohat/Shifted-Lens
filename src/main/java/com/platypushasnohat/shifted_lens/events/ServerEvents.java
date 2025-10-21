package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.ShiftedLensConfig;
import com.platypushasnohat.shifted_lens.entities.Squill;
import com.platypushasnohat.shifted_lens.entities.ai.goals.RabbitRaidGardenGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SLRabbitAvoidEntityGoal;
import com.platypushasnohat.shifted_lens.entities.ai.goals.SLRabbitPanicGoal;
import com.platypushasnohat.shifted_lens.registry.tags.SLEntityTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID)
public class ServerEvents {

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onEntityJoinWorld(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity.level().isClientSide && entity instanceof Squill squill) {
            squill.updatePull(entity.position());
        }

        if (entity instanceof Mob mob) {

            if (mob instanceof Guardian guardian) {
                guardian.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(guardian, LivingEntity.class, 10, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.GUARDIAN_TARGETS) && livingEntity.distanceToSqr(guardian) > 9.0D));
            }
            if (mob instanceof ElderGuardian elderGuardian) {
                elderGuardian.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(elderGuardian, LivingEntity.class, 10, true, false, livingEntity -> livingEntity.getType().is(SLEntityTags.GUARDIAN_TARGETS) && livingEntity.distanceToSqr(elderGuardian) > 9.0D));
            }

            if (mob instanceof Rabbit rabbit) {
                rabbit.goalSelector.addGoal(1, new SLRabbitPanicGoal(rabbit, 2.2D));
                rabbit.goalSelector.addGoal(4, new SLRabbitAvoidEntityGoal<>(rabbit, Player.class));
                rabbit.goalSelector.addGoal(4, new SLRabbitAvoidEntityGoal<>(rabbit, LivingEntity.class, livingEntity -> livingEntity.getType().is(SLEntityTags.RABBIT_AVOIDS)));
                rabbit.goalSelector.addGoal(5, new RabbitRaidGardenGoal(rabbit));
            }
        }
    }

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
}
