package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Squid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(SquidRenderer.class)
public abstract class SquidRendererMixin {

    @Shadow
    private static final ResourceLocation SQUID_LOCATION = new ResourceLocation("textures/entity/squid/squid.png");

    @Unique
    private static final ResourceLocation SQUID_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/squid.png");
    @Unique
    private static final ResourceLocation COLD_SQUID_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/cold_squid.png");

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Squid;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(Squid squid, CallbackInfoReturnable<ResourceLocation> cir) {
        int variant = ((VariantAccess) squid).getVariant();
        ResourceLocation texture = variant == 1 ? COLD_SQUID_TEXTURE : SQUID_TEXTURE;
        cir.setReturnValue(texture);
    }
}
