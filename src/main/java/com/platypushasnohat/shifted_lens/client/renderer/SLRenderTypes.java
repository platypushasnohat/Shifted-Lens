package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SLRenderTypes extends RenderType {
    public SLRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int i, boolean b, boolean c, Runnable runnable, Runnable runnable1) {
        super(string, vertexFormat, mode, i, b, c, runnable, runnable1);
    }

    public static RenderType guardianBeam(ResourceLocation resourceLocation) {
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(resourceLocation, false, false);
        return create("guardian_beam", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_OUTLINE_SHADER).setTextureState(textureStateShard).setTransparencyState(LIGHTNING_TRANSPARENCY).setWriteMaskState(COLOR_DEPTH_WRITE).setCullState(NO_CULL).setDepthTestState(LEQUAL_DEPTH_TEST).setOverlayState(OVERLAY).createCompositeState(true));
    }
}