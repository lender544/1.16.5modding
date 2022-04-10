package L_Ender.cataclysm.client.render.layer;

import L_Ender.cataclysm.client.model.entity.ModelNetherite_Monstrosity;
import L_Ender.cataclysm.client.render.entity.RendererNetherite_Monstrosity;
import L_Ender.cataclysm.entity.Netherite_Monstrosity_Entity;
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
public class Netherite_Monstrosity_Layer extends LayerRenderer<Netherite_Monstrosity_Entity, ModelNetherite_Monstrosity> {
    private static final ResourceLocation NETHERITE_MONSTRISITY_LAYER_TEXTURES  = new ResourceLocation("cataclysm:textures/entity/netherite_monstrosity_layer.png");

    public Netherite_Monstrosity_Layer(RendererNetherite_Monstrosity renderIn) {
        super(renderIn);

    }

    @Override
    public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Netherite_Monstrosity_Entity entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.getIsAwaken() && entity.deathTime <= 75) {
            //need rework
            RenderType eyes = RenderType.getEyes(NETHERITE_MONSTRISITY_LAYER_TEXTURES);
            IVertexBuilder ivertexbuilder = bufferIn.getBuffer(eyes);
            this.getEntityModel().render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}


