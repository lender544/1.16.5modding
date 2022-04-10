package L_Ender.cataclysm.client.render.layer;

import L_Ender.cataclysm.client.model.entity.ModelEnder_Golem;
import L_Ender.cataclysm.client.render.entity.RendererEnder_Golem;
import L_Ender.cataclysm.entity.Ender_Golem_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Ender_Golem_Layer extends LayerRenderer<Ender_Golem_Entity, ModelEnder_Golem> {
    private static final ResourceLocation ENDER_GOLEM_LAYER_TEXTURES  = new ResourceLocation("cataclysm:textures/entity/ender_golem_layer.png");

    public Ender_Golem_Layer(RendererEnder_Golem renderIn) {
        super(renderIn);

    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Ender_Golem_Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getIsAwaken() && entity.deathTime <= 45) {
            //need rework
            RenderType eyes = RenderType.getEyes(ENDER_GOLEM_LAYER_TEXTURES);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(eyes);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}


