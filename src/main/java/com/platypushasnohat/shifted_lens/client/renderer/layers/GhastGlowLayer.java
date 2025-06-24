package com.platypushasnohat.shifted_lens.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.platypushasnohat.shifted_lens.client.models.ShiftedGhastModel;
import com.platypushasnohat.shifted_lens.entities.ShiftedGhast;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class GhastGlowLayer<T extends ShiftedGhast, M extends ShiftedGhastModel<T>> extends RenderLayer<T, M> {

    private final ResourceLocation texture;
    private final AlphaFunction<T> alphaFunction;
    private final DrawSelector<T, M> drawSelector;

    public GhastGlowLayer(RenderLayerParent<T, M> parent, ResourceLocation resourceLocation, AlphaFunction<T> alpha, DrawSelector<T, M> drawSelector) {
        super(parent);
        this.texture = resourceLocation;
        this.alphaFunction = alpha;
        this.drawSelector = drawSelector;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, int p_234904_, T entity, float p_234906_, float p_234907_, float p_234908_, float p_234909_, float p_234910_, float p_234911_) {
        if (!entity.isInvisible()) {
            this.onlyDrawSelectedParts();
            VertexConsumer vertexconsumer = buffer.getBuffer(RenderType.entityTranslucentEmissive(this.texture));
            this.getParentModel().renderToBuffer(poseStack, vertexconsumer, p_234904_, LivingEntityRenderer.getOverlayCoords(entity, 0.0F), 1.0F, 1.0F, 1.0F, this.alphaFunction.apply(entity, p_234908_, p_234909_));
            this.resetDrawForAllParts();
        }
    }

    private void onlyDrawSelectedParts() {
        List<ModelPart> list = this.drawSelector.getPartsToDraw(this.getParentModel());
        this.getParentModel().root().getAllParts().forEach((modelPart) -> {
            modelPart.skipDraw = true;
        });
        list.forEach((modelPart) -> {
            modelPart.skipDraw = false;
        });
    }

    private void resetDrawForAllParts() {
        this.getParentModel().root().getAllParts().forEach((modelPart) -> {
            modelPart.skipDraw = false;
        });
    }

    @OnlyIn(Dist.CLIENT)
    public interface AlphaFunction<T extends ShiftedGhast> {
        float apply(T entity, float p_234921_, float p_234922_);
    }

    @OnlyIn(Dist.CLIENT)
    public interface DrawSelector<T extends ShiftedGhast, M extends EntityModel<T>> {
        List<ModelPart> getPartsToDraw(M model);
    }
}


