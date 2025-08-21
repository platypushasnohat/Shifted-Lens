package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLTropicalFishModelB;
import com.platypushasnohat.shifted_lens.config.SLConfig;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.TropicalFishRenderer;
import net.minecraft.client.renderer.entity.layers.TropicalFishPatternLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@OnlyIn(Dist.CLIENT)
@Mixin(TropicalFishRenderer.class)
public abstract class TropicalFishRendererMixin extends MobRenderer<TropicalFish, ColorableHierarchicalModel<TropicalFish>> {

    @Shadow
    private static final ResourceLocation MODEL_A_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a.png");
    @Shadow
    private static final ResourceLocation MODEL_B_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b.png");

    @Unique
    private static final ResourceLocation TROPICAL_FISH_B_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/tropical_fish/tropical_fish_b.png");

    @Shadow
    @Final
    private ColorableHierarchicalModel<TropicalFish> modelA;

    @Shadow
    @Final
    private ColorableHierarchicalModel<TropicalFish> modelB;

    @Unique
    private SLTropicalFishModelB<TropicalFish> shiftedLens$remodelB;

    public TropicalFishRendererMixin(EntityRendererProvider.Context context, ColorableHierarchicalModel<TropicalFish> model, float p_174306_) {
        super(context, model, p_174306_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void TropicalFishRenderer(EntityRendererProvider.Context context, CallbackInfo callbackInfo) {
        this.shiftedLens$remodelB = new SLTropicalFishModelB<>(context.bakeLayer(SLModelLayers.TROPICAL_FISH_B));
    }

    @Override
    public void render(@NotNull TropicalFish fish, float f, float g, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int i) {
        if (SLConfig.REPLACE_TROPICAL_FISH.get()) {
            ColorableHierarchicalModel<TropicalFish> colorablehierarchicalmodel = getTropicalFishModel(fish);
            this.model = colorablehierarchicalmodel;
            float[] afloat = fish.getBaseColor().getTextureDiffuseColors();
            colorablehierarchicalmodel.setColor(afloat[0], afloat[1], afloat[2]);
            super.render(fish, f, g, poseStack, bufferSource, i);
            colorablehierarchicalmodel.setColor(1.0F, 1.0F, 1.0F);
        }
        super.render(fish, f, g, poseStack, bufferSource, i);
    }

    @Unique
    private ColorableHierarchicalModel<TropicalFish> getTropicalFishModel(@NotNull TropicalFish fish) {
        ColorableHierarchicalModel<TropicalFish> colorablehierarchicalmodel1 = switch (fish.getVariant().base()) {
            case SMALL -> this.modelA;
            case LARGE -> this.shiftedLens$remodelB;
        };

        ColorableHierarchicalModel<TropicalFish> colorablehierarchicalmodel = colorablehierarchicalmodel1;
        return colorablehierarchicalmodel;
    }

    @Inject(method = "getTextureLocation(Lnet/minecraft/world/entity/animal/TropicalFish;)Lnet/minecraft/resources/ResourceLocation;", at = @At("RETURN"), cancellable = true)
    private void getTextureLocation(TropicalFish fish, CallbackInfoReturnable<ResourceLocation> cir) {
        ResourceLocation texture = switch (fish.getVariant().base()) {
            case SMALL -> MODEL_A_TEXTURE;
            case LARGE -> TROPICAL_FISH_B_TEXTURE;
        };
        ResourceLocation vanillaTexture = switch (fish.getVariant().base()) {
            case SMALL -> MODEL_A_TEXTURE;
            case LARGE -> MODEL_B_TEXTURE;
        };
        cir.setReturnValue(SLConfig.REPLACE_TROPICAL_FISH.get() ? texture : vanillaTexture);
    }

    @Override
    public void setupRotations(TropicalFish fish, PoseStack poseStack, float p_116228_, float p_116229_, float p_116230_) {
        if (SLConfig.BETTER_FISH_FLOPPING.get()) {
            super.setupRotations(fish, poseStack, p_116228_, p_116229_, p_116230_);
        } else {
            super.setupRotations(fish, poseStack, p_116228_, p_116229_, p_116230_);
            float f = 4.3F * Mth.sin(0.6F * p_116228_);
            poseStack.mulPose(Axis.YP.rotationDegrees(f));
            if (!fish.isInWater()) {
                poseStack.translate(0.2F, 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
    }
}
