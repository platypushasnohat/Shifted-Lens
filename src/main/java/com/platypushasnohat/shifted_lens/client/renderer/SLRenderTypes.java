package com.platypushasnohat.shifted_lens.client.renderer;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

public class SLRenderTypes extends RenderType {
    public SLRenderTypes(String string, VertexFormat vertexFormat, VertexFormat.Mode mode, int p_173181_, boolean p_173182_, boolean p_173183_, Runnable runnable, Runnable runnable1) {
        super(string, vertexFormat, mode, p_173181_, p_173182_, p_173183_, runnable, runnable1);
    }

    public static RenderType guardianBeam(ResourceLocation locationIn) {
        RenderStateShard.TextureStateShard renderstateshard$texturestateshard = new RenderStateShard.TextureStateShard(locationIn, false, false);
        return create("guardian_beam", DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.QUADS, 256, true, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_OUTLINE_SHADER).setTextureState(renderstateshard$texturestateshard).setTransparencyState(LIGHTNING_TRANSPARENCY).setWriteMaskState(COLOR_DEPTH_WRITE).setCullState(NO_CULL).setDepthTestState(LEQUAL_DEPTH_TEST).setOverlayState(OVERLAY).createCompositeState(true));
    }
}
