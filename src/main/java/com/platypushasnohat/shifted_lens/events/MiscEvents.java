package com.platypushasnohat.shifted_lens.events;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.SLItems;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber(modid = ShiftedLens.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MiscEvents {

    private static final UUID WHIRLICAP_MODIFIER = UUID.fromString("5b397cd8-4dca-47a0-9496-e6dfb00d2387");
    private static final UUID WHIRLICAP_MODIFIER_SLOW_FALLING = UUID.fromString("494583ff-efd5-4146-be53-4814d6c407cf");

    private static final AttributeModifier WHIRLICAP_GRAVITY = new AttributeModifier(WHIRLICAP_MODIFIER, "whirlicap gravity modifier", -0.025D, AttributeModifier.Operation.ADDITION);
    private static final AttributeModifier WHIRLICAP_GRAVITY_SLOW_FALLING = new AttributeModifier(WHIRLICAP_MODIFIER_SLOW_FALLING, "whirlicap gravity modifier 2", -0.01D, AttributeModifier.Operation.ADDITION);

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        final var entity = event.getEntity();
        if (entity instanceof Player player) {
            if (entity.getAttributes().hasAttribute(ForgeMod.ENTITY_GRAVITY.get())) {
                final var attributes = entity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
                if (player.getItemBySlot(EquipmentSlot.HEAD).getItem() == SLItems.WHIRLICAP.get() || attributes.hasModifier(WHIRLICAP_GRAVITY) || attributes.hasModifier(WHIRLICAP_GRAVITY_SLOW_FALLING)) {
                    final var isFalling = player.getDeltaMovement().y <= 0.0D && !player.isCrouching();

                    if (player.hasEffect(MobEffects.SLOW_FALLING)) {
                        if (attributes.hasModifier(WHIRLICAP_GRAVITY)) {
                            attributes.removeModifier(WHIRLICAP_GRAVITY);
                        }
                        if (isFalling && !attributes.hasModifier(WHIRLICAP_GRAVITY_SLOW_FALLING)) {
                            attributes.addPermanentModifier(WHIRLICAP_GRAVITY_SLOW_FALLING);
                        }
                        if ((!isFalling || player.getItemBySlot(EquipmentSlot.HEAD).getItem() != SLItems.WHIRLICAP.get()) && attributes.hasModifier(WHIRLICAP_GRAVITY_SLOW_FALLING)) {
                            attributes.removeModifier(WHIRLICAP_GRAVITY_SLOW_FALLING);
                        }
                    } else {
                        if (attributes.hasModifier(WHIRLICAP_GRAVITY_SLOW_FALLING)) {
                            attributes.removeModifier(WHIRLICAP_GRAVITY_SLOW_FALLING);
                        }
                        if (isFalling && !attributes.hasModifier(WHIRLICAP_GRAVITY)) {
                            attributes.addPermanentModifier(WHIRLICAP_GRAVITY);
                        }
                        if ((!isFalling || player.getItemBySlot(EquipmentSlot.HEAD).getItem() != SLItems.WHIRLICAP.get()) && attributes.hasModifier(WHIRLICAP_GRAVITY)) {
                            attributes.removeModifier(WHIRLICAP_GRAVITY);
                        }
                    }
                }
            }
        }
    }
}
