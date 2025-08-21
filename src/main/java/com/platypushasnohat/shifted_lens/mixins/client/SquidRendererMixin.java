package com.platypushasnohat.shifted_lens.mixins.client;

import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.mixin_utils.VariantAccess;
import net.minecraft.client.model.SquidModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.SquidRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Squid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@OnlyIn(Dist.CLIENT)
@Mixin(SquidRenderer.class)
public abstract class SquidRendererMixin extends MobRenderer<Squid, SquidModel<Squid>> implements VariantAccess {

    @Shadow
    private static final ResourceLocation SQUID_LOCATION = new ResourceLocation("textures/entity/squid/squid.png");

    @Unique
    private static final ResourceLocation SQUID_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/squid.png");
    @Unique
    private static final ResourceLocation COLD_SQUID_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/squid/cold_squid.png");

    public SquidRendererMixin(EntityRendererProvider.Context context, SquidModel<Squid> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Override
    public ResourceLocation getTextureLocation(Squid squid) {
        int variant = ((VariantAccess) squid).getVariant();
        if (SLConfig.REPLACE_SQUID.get()) {
            if (variant == 1) return COLD_SQUID_TEXTURE;
            else return SQUID_TEXTURE;
        } else {
            return SQUID_LOCATION;
        }
    }
}
