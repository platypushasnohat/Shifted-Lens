package com.platypushasnohat.shifted_lens.mixins.client;

import net.minecraft.client.renderer.entity.DragonFireballRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;

@OnlyIn(Dist.CLIENT)
@Mixin(DragonFireballRenderer.class)
public class LargeFireballRendererMixin {
}
