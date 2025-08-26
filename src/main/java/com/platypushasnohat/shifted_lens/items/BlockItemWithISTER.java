package com.platypushasnohat.shifted_lens.items;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.registries.RegistryObject;

import java.util.Properties;

public class BlockItemWithISTER extends BlockItemWithSupplier {

    public BlockItemWithISTER(RegistryObject<Block> blockSupplier, Properties props) {
        super(blockSupplier, props);
    }

    @Override
    public void initializeClient(java.util.function.Consumer<IClientItemExtensions> consumer) {
        consumer.accept((IClientItemExtensions) ShiftedLens.PROXY.getISTERProperties());
    }
}
