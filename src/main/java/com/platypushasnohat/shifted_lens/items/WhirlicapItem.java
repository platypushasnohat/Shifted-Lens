package com.platypushasnohat.shifted_lens.items;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.enums.SLArmorMaterials;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.ForgeMod;

import javax.annotation.Nullable;
import java.util.UUID;

public class WhirlicapItem extends ArmorItem {

    private static final AttributeModifier WHIRLICAP_SLOW_FALL = new AttributeModifier(UUID.fromString("5b397cd8-4dca-47a0-9496-e6dfb00d2387"), "Falling acceleration reduction", -0.05, AttributeModifier.Operation.ADDITION);

    private int flightTime = 0;
    private final int maxflightTime = 50;

    public WhirlicapItem(Properties properties) {
        super(SLArmorMaterials.WHIRLICAP, Type.HELMET, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        Vec3 motion = player.getDeltaMovement();
        AttributeInstance gravity = player.getAttribute(ForgeMod.ENTITY_GRAVITY.get());

        boolean isSlowFalling = player.getDeltaMovement().y <= 0.0D && !player.isCrouching();
        if (isSlowFalling) {
            if (!gravity.hasModifier(WHIRLICAP_SLOW_FALL)) {
                gravity.addTransientModifier(WHIRLICAP_SLOW_FALL);
            }
        } else if (gravity.hasModifier(WHIRLICAP_SLOW_FALL)) {
            gravity.removeModifier(WHIRLICAP_SLOW_FALL);
        }

        if (!onGround(player) && motion.y < 0.08 + 0.2) {
            player.resetFallDistance();
            if (player.jumping && !player.isCrouching() && flightTime <= maxflightTime) {
                this.flightTime++;
                if (flightTime > 2) {
                    player.setDeltaMovement(motion.x, motion.y + 0.09F, motion.z);
                }
            }
        }

        if (onGround(player)) {
            flightTime = 0;
        }
    }

    private static boolean onGround(Player player) {
        return player.onGround() || player.isInFluidType();
    }

    @Override
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) ShiftedLens.PROXY.getArmorRenderProperties());
    }

    @Nullable
    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return ShiftedLens.MOD_ID + ":textures/models/armor/whirlicap_layer_1.png";
    }
}
