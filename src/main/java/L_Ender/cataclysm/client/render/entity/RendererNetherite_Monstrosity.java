package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelNetherite_Monstrosity;
import L_Ender.cataclysm.client.render.layer.Ender_Golem_Layer;
import L_Ender.cataclysm.client.render.layer.LayerGenericGlowing;
import L_Ender.cataclysm.client.render.layer.Netherite_Monstrosity_Layer;
import L_Ender.cataclysm.entity.Netherite_Monstrosity_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererNetherite_Monstrosity extends MobRenderer<Netherite_Monstrosity_Entity, ModelNetherite_Monstrosity> {

    private static final ResourceLocation NETHER_MONSTROSITY_TEXTURES = new ResourceLocation("cataclysm:textures/entity/netherite_monstrosity.png");

    public RendererNetherite_Monstrosity(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelNetherite_Monstrosity(), 2.5F);
        this.addLayer(new Netherite_Monstrosity_Layer(this));

    }
    @Override
    public ResourceLocation getEntityTexture(Netherite_Monstrosity_Entity entity) {
        return NETHER_MONSTROSITY_TEXTURES;
    }

    @Override
    protected void preRenderCallback(Netherite_Monstrosity_Entity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
    }

    @Override
    protected float getDeathMaxRotation(Netherite_Monstrosity_Entity entity) {
        return 0;
    }

}
