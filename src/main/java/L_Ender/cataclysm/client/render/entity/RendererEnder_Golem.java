package L_Ender.cataclysm.client.render.entity;


import L_Ender.cataclysm.client.model.entity.ModelEnder_Golem;
import L_Ender.cataclysm.client.render.layer.Ender_Golem_Layer;
import L_Ender.cataclysm.client.render.layer.LayerGenericGlowing;
import L_Ender.cataclysm.entity.Ender_Golem_Entity;
import L_Ender.cataclysm.entity.Netherite_Monstrosity_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererEnder_Golem extends MobRenderer<Ender_Golem_Entity, ModelEnder_Golem> {

    private static final ResourceLocation ENDER_GOLEM_TEXTURES = new ResourceLocation("cataclysm:textures/entity/ender_golem.png");
    ;

    public RendererEnder_Golem(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelEnder_Golem(), 1.5F);
        this.addLayer(new Ender_Golem_Layer(this));

    }
    @Override
    public ResourceLocation getEntityTexture(Ender_Golem_Entity entity) {
        return ENDER_GOLEM_TEXTURES;
    }

    @Override
    protected void preRenderCallback(Ender_Golem_Entity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1F, 1F, 1F);
    }

    @Override
    protected float getDeathMaxRotation(Ender_Golem_Entity entity) {
        return 0;
    }

}

