package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelNameless_Sorcerer;
import L_Ender.cataclysm.entity.Nameless_Sorcerer_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererNameless_Sorcerer extends MobRenderer<Nameless_Sorcerer_Entity, ModelNameless_Sorcerer> {

    private static final ResourceLocation NAMELESS_SORCERER_TEXTURES = new ResourceLocation("cataclysm:textures/entity/nameless_sorcerer.png");

    public RendererNameless_Sorcerer(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelNameless_Sorcerer(), 0.5F);

    }
    @Override
    public ResourceLocation getEntityTexture(Nameless_Sorcerer_Entity entity) {
        return NAMELESS_SORCERER_TEXTURES;
    }

    @Override
    protected void preRenderCallback(Nameless_Sorcerer_Entity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(0.9375F, 0.9375F, 0.9375F);
    }

}
