package com.platypushasnohat.shifted_lens.items;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.registry.enums.SLArmorMaterials;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import javax.annotation.Nullable;

public class WhirlicapItem extends ArmorItem {

    private int flightTime = 0;

    public WhirlicapItem(Properties properties) {
        super(SLArmorMaterials.WHIRLICAP, Type.HELMET, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        Vec3 motion = player.getDeltaMovement();
        player.resetFallDistance();

        if (!onGround(player) && motion.y < 0.08 + 0.2) {
            if (player.jumping && !player.isCrouching() && flightTime <= 40) {
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
