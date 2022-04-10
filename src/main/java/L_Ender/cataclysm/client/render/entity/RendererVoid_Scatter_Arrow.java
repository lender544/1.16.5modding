package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.entity.projectile.Void_Scatter_Arrow_Entity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;

public class RendererVoid_Scatter_Arrow extends ArrowRenderer<Void_Scatter_Arrow_Entity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("cataclysm:textures/entity/void_scatter_arrow.png");

    public RendererVoid_Scatter_Arrow(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public ResourceLocation getEntityTexture(Void_Scatter_Arrow_Entity entity) {
        return TEXTURE;
    }
}
