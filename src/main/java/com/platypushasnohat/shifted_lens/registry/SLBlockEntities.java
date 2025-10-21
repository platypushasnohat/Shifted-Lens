package com.platypushasnohat.shifted_lens.registry;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.blocks.blockentity.WhirliboxBlockEntity;
import com.platypushasnohat.shifted_lens.blocks.blockentity.WhirligigBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SLBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, ShiftedLens.MOD_ID);

    public static final RegistryObject<BlockEntityType<WhirligigBlockEntity>> WHIRLIGIG_BLOCK_ENTITY = BLOCK_ENTITIES.register("whirligig", () -> BlockEntityType.Builder.of(WhirligigBlockEntity::new, SLBlocks.WHIRLIGIG.get()).build(null));

    public static final RegistryObject<BlockEntityType<WhirliboxBlockEntity>> WHIRLIBOX_BLOCK_ENTITY = BLOCK_ENTITIES.register("whirlibox", () -> BlockEntityType.Builder.of(WhirliboxBlockEntity::new, SLBlocks.WHIRLIBOX.get()).build(null));
}
