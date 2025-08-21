package com.platypushasnohat.shifted_lens.mixins.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.platypushasnohat.shifted_lens.ShiftedLens;
import com.platypushasnohat.shifted_lens.client.models.SLTropicalFishModelB;
import com.platypushasnohat.shifted_lens.registry.SLModelLayers;
import net.minecraft.client.model.ColorableHierarchicalModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.TropicalFishModelA;
import net.minecraft.client.model.TropicalFishModelB;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.layers.TropicalFishPatternLayer;
import net.minecraft.resources.ResourceLocation;
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

@OnlyIn(Dist.CLIENT)
@Mixin(TropicalFishPatternLayer.class)
public abstract class TropicalFishPatternLayerMixin extends RenderLayer<TropicalFish, ColorableHierarchicalModel<TropicalFish>> {

    @Shadow
    private static final ResourceLocation KOB_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_1.png");
    @Shadow
    private static final ResourceLocation SUNSTREAK_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_2.png");
    @Shadow
    private static final ResourceLocation SNOOPER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_3.png");
    @Shadow
    private static final ResourceLocation DASHER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_4.png");
    @Shadow
    private static final ResourceLocation BRINELY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_5.png");
    @Shadow
    private static final ResourceLocation SPOTTY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_a_pattern_6.png");
    @Shadow
    private static final ResourceLocation FLOPPER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_1.png");
    @Shadow
    private static final ResourceLocation STRIPEY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_2.png");
    @Shadow
    private static final ResourceLocation GLITTER_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_3.png");
    @Shadow
    private static final ResourceLocation BLOCKFISH_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_4.png");
    @Shadow
    private static final ResourceLocation BETTY_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_5.png");
    @Shadow
    private static final ResourceLocation CLAYFISH_TEXTURE = new ResourceLocation("textures/entity/fish/tropical_b_pattern_6.png");

    @Unique
    private static final ResourceLocation TROPICAL_FISH_B_PATTERN_TEXTURE = new ResourceLocation(ShiftedLens.MOD_ID, "textures/entity/tropical_fish/tropical_fish_b_pattern_1.png");


    @Shadow
    @Final
    private TropicalFishModelA<TropicalFish> modelA;

    @Shadow
    @Final
    private TropicalFishModelB<TropicalFish> modelB;

    @Unique
    private SLTropicalFishModelB<TropicalFish> shiftedLens$remodelB;

    public TropicalFishPatternLayerMixin(RenderLayerParent<TropicalFish, ColorableHierarchicalModel<TropicalFish>> context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void TropicalFishRenderer(RenderLayerParent parent, EntityModelSet modelSet, CallbackInfo ci) {
        this.shiftedLens$remodelB = new SLTropicalFishModelB<>(modelSet.bakeLayer(SLModelLayers.TROPICAL_FISH_B_PATTERN));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int p_117614_, TropicalFish fish, float p_117616_, float p_117617_, float p_117618_, float p_117619_, float p_117620_, float p_117621_) {
        TropicalFish.Pattern pattern = fish.getVariant();
        Object object = switch (pattern.base()) {
            case SMALL -> this.modelA;
            case LARGE -> this.shiftedLens$remodelB;
        };

        EntityModel<TropicalFish> model = (EntityModel<TropicalFish>) object;
        ResourceLocation resourcelocation = getResourceLocation(pattern);
        float[] afloat = fish.getPatternColor().getTextureDiffuseColors();
        coloredCutoutModelCopyLayerRender(this.getParentModel(), model, resourcelocation, poseStack, bufferSource, p_117614_, fish, p_117616_, p_117617_, p_117619_, p_117620_, p_117621_, p_117618_, afloat[0], afloat[1], afloat[2]);
    }

    @Unique
    private static @NotNull ResourceLocation getResourceLocation(TropicalFish.Pattern tropicalfish$pattern) {
        ResourceLocation resourcelocation1 = switch (tropicalfish$pattern) {
            case KOB -> KOB_TEXTURE;
            case SUNSTREAK -> SUNSTREAK_TEXTURE;
            case SNOOPER -> SNOOPER_TEXTURE;
            case DASHER -> DASHER_TEXTURE;
            case BRINELY -> BRINELY_TEXTURE;
            case SPOTTY -> SPOTTY_TEXTURE;
            case FLOPPER -> TROPICAL_FISH_B_PATTERN_TEXTURE;
            case STRIPEY -> TROPICAL_FISH_B_PATTERN_TEXTURE;
            case GLITTER -> TROPICAL_FISH_B_PATTERN_TEXTURE;
            case BLOCKFISH -> TROPICAL_FISH_B_PATTERN_TEXTURE;
            case BETTY -> TROPICAL_FISH_B_PATTERN_TEXTURE;
            case CLAYFISH -> TROPICAL_FISH_B_PATTERN_TEXTURE;
        };

        ResourceLocation resourcelocation = resourcelocation1;
        return resourcelocation;
    }
}
