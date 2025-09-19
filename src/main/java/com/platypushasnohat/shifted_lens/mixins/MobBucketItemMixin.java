package com.platypushasnohat.shifted_lens.mixins;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MobBucketItem.class)
public abstract class MobBucketItemMixin {

    @Shadow
    protected abstract EntityType<?> getFishType();

    @Inject(method = "appendHoverText(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/level/Level;Ljava/util/List;Lnet/minecraft/world/item/TooltipFlag;)V", at = @At("TAIL"))
    public void appendHoverText(ItemStack stack, Level level, List<Component> components, TooltipFlag flag, CallbackInfo ci) {
        ChatFormatting[] chatFormatting = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
        if (getFishType() == EntityType.COD) {
            CompoundTag compoundtag = stack.getTag();
            if (compoundtag != null && compoundtag.contains("BucketVariantTag", 3)) {
                int i = compoundtag.getInt("BucketVariantTag");

                String variant = "shifted_lens.cod.variant" + i;

                MutableComponent mutablecomponent = Component.translatable(variant);
                mutablecomponent.withStyle(chatFormatting);
                components.add(mutablecomponent);
            }
        }
        if (getFishType() == EntityType.SALMON) {
            CompoundTag compoundtag = stack.getTag();
            if (compoundtag != null && compoundtag.contains("BucketVariantTag", 3)) {
                int i = compoundtag.getInt("BucketVariantTag");

                String variant = "shifted_lens.salmon.variant" + i;

                MutableComponent mutablecomponent = Component.translatable(variant);
                mutablecomponent.withStyle(chatFormatting);
                components.add(mutablecomponent);
            }
        }
    }
}
