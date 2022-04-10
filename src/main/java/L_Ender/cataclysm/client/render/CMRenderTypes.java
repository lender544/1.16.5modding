package L_Ender.cataclysm.client.render;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;

public class CMRenderTypes extends RenderType {
    public CMRenderTypes(String p_i225992_1_, VertexFormat p_i225992_2_, int p_i225992_3_, int p_i225992_4_, boolean p_i225992_5_, boolean p_i225992_6_, Runnable p_i225992_7_, Runnable p_i225992_8_) {
        super(p_i225992_1_, p_i225992_2_, p_i225992_3_, p_i225992_4_, p_i225992_5_, p_i225992_6_, p_i225992_7_, p_i225992_8_);
    }

    public static RenderType getBright(ResourceLocation locationIn) {
        RenderState.TextureState renderstate$texturestate = new RenderState.TextureState(locationIn, false, false);
        return makeType("bright", DefaultVertexFormats.ENTITY, 7, 256, false, true, RenderType.State.getBuilder()
                .texture(renderstate$texturestate)
                .diffuseLighting(DIFFUSE_LIGHTING_DISABLED)
                .transparency(TRANSLUCENT_TRANSPARENCY)
                .alpha(DEFAULT_ALPHA).cull(CULL_DISABLED)
                .lightmap(LIGHTMAP_ENABLED)
                .overlay(OVERLAY_ENABLED)
                .build(false));
    }
}
