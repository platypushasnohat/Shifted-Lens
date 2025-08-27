package com.platypushasnohat.shifted_lens.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class SLMobBucketItem extends MobBucketItem {

    public SLMobBucketItem(Supplier<? extends EntityType<?>> entity, Fluid fluid, Item.Properties properties) {
        super(entity, () -> fluid, () -> SoundEvents.BUCKET_EMPTY_FISH, properties);
    }

    @OnlyIn(Dist.CLIENT)
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        ChatFormatting[] chatFormatting = new ChatFormatting[]{ChatFormatting.ITALIC, ChatFormatting.GRAY};
        if (getFishType() == EntityType.SQUID) {
            CompoundTag compoundtag = stack.getTag();
            if (compoundtag != null && compoundtag.contains("Variant", 3)) {
                int i = compoundtag.getInt("Variant");

                String variant = "shifted_lens.squid.variant" + i;

                MutableComponent mutablecomponent = Component.translatable(variant);
                mutablecomponent.withStyle(chatFormatting);
                components.add(mutablecomponent);
            }
        }
    }

    @Override
    public void checkExtraContent(@Nullable Player player, Level level, ItemStack stack, BlockPos pos) {
        if (level instanceof ServerLevel) {
            this.spawnFish((ServerLevel)level, stack, pos);
            level.gameEvent(player, GameEvent.ENTITY_PLACE, pos);
        }
    }

    private void spawnFish(ServerLevel serverLevel, ItemStack stack, BlockPos pos) {
        Entity entity = getFishType().spawn(serverLevel, stack, null, pos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof Bucketable bucketable) {
            bucketable.loadFromBucketTag(stack.getOrCreateTag());
            bucketable.setFromBucket(true);
        }
        addExtraAttributes(entity, stack);
    }

    private void addExtraAttributes(Entity entity, ItemStack stack) {
    }
}
