package L_Ender.cataclysm.client.render.layer;

import L_Ender.cataclysm.client.model.entity.ModelEnder_Guardian;
import L_Ender.cataclysm.client.render.entity.RendererEnder_Guardian;
import L_Ender.cataclysm.entity.Ender_Guardian_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class Ender_Guardian_Layer extends LayerRenderer<Ender_Guardian_Entity, ModelEnder_Guardian> {
    private static final ResourceLocation ENDER_GUARDIAN_LAYER_TEXTURES  = new ResourceLocation("cataclysm:textures/entity/ender_guardian_layer.png");

    public Ender_Guardian_Layer(RendererEnder_Guardian renderIn) {
        super(renderIn);

    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Ender_Guardian_Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.deathTime < 100) {
            //need rework
            RenderType eyes = RenderType.getEyes(ENDER_GUARDIAN_LAYER_TEXTURES);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(eyes);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}


