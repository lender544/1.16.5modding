package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelIgnis_Fireball;
import L_Ender.cataclysm.client.render.CMRenderTypes;
import L_Ender.cataclysm.entity.projectile.Ignis_Fireball_Entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class RendererIgnis_Fireball extends EntityRenderer<Ignis_Fireball_Entity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("cataclysm:textures/entity/ignis_fire_ball.png");
    private static final ResourceLocation TEXTURE_SOUL = new ResourceLocation("cataclysm:textures/entity/ignis_fire_ball_soul.png");
    private static final ResourceLocation TEXTURE_VOID = new ResourceLocation("cataclysm:textures/entity/ignis_fire_ball_void.png");
    private static ModelIgnis_Fireball MODEL = new ModelIgnis_Fireball();

    public RendererIgnis_Fireball(EntityRendererManager renderManager) {
        super(renderManager);
    }

    @Override
    public ResourceLocation getEntityTexture(Ignis_Fireball_Entity entity) {
        return entity.getVariant() == 2 ? TEXTURE_VOID : entity.getVariant() == 1 ? TEXTURE_SOUL : TEXTURE;
    }

    @Override
    public void render(Ignis_Fireball_Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(Vector3f.XP, 180F, true));
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationYaw, entityIn.rotationYaw)));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch)));
        matrixStackIn.push();
        MODEL.animate(entityIn, entityIn.ticksExisted + partialTicks);
        matrixStackIn.translate(0, -1.5F, 0);
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(CMRenderTypes.getBright(getEntityTexture(entityIn)));
        MODEL.render(matrixStackIn, ivertexbuilder, 210, NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
        matrixStackIn.pop();
    }


}
