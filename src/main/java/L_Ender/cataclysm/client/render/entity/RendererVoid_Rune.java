package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelVoid_Rune;
import L_Ender.cataclysm.client.render.CMRenderTypes;
import L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RendererVoid_Rune extends EntityRenderer<Void_Rune_Entity> {
    private static final ResourceLocation VOID_RUNE = new ResourceLocation("cataclysm:textures/entity/void_rune.png");
    private final ModelVoid_Rune model = new ModelVoid_Rune();
    private final Random rnd = new Random();

    public RendererVoid_Rune(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    public void render(Void_Rune_Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.scale(-2, -2, -2);
        matrixStackIn.translate(0.0D, -1.5F, 0.0D);
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F - entityIn.rotationYaw));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(CMRenderTypes.getBright(this.getEntityTexture(entityIn)));
        model.setRotationAngles(entityIn, 0, 0, entityIn.ticksExisted + partialTicks, 0, 0);
        model.render(matrixStackIn, ivertexbuilder, 210, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStackIn.pop();
    }

    public Vector3d getRenderOffset(Void_Rune_Entity entityIn, float partialTicks) {
        if (entityIn.activateProgress == 10) {
            return super.getRenderOffset(entityIn, partialTicks);
        } else {
            double d0 = 0.02D;
            return new Vector3d(this.rnd.nextGaussian() * d0, 0.0D, this.rnd.nextGaussian() * d0);
        }
    }

    protected int getBlockLight(Void_Rune_Entity entityIn, BlockPos pos) {
        return 15;
    }

    /**
     * Returns the location of an entity's texture.
     */
    public ResourceLocation getEntityTexture(Void_Rune_Entity entity) {
        return VOID_RUNE;
    }
}
