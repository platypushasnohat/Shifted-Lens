package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import net.minecraft.client.renderer.entity.ElderGuardianRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(ElderGuardianRenderer.class)
public abstract class ElderGuardianRendererMixin {

    @Shadow
    public static final ResourceLocation GUARDIAN_ELDER_LOCATION = new ResourceLocation("textures/entity/guardian_elder.png");

    @Unique
    private static final ResourceLocation ELDER_GUARDIAN_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/guardian/elder_guardian.png");

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/monster/Guardian;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    public void getTextureLocation(Guardian guardian, CallbackInfoReturnable<ResourceLocation> cir) {
        cir.setReturnValue(ELDER_GUARDIAN_TEXTURE);
    }
}
