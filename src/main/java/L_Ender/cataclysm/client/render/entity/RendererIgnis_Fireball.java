package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelEnder_Guardian_Bullet;
import L_Ender.cataclysm.client.model.entity.ModelIgnis_Fireball;
import L_Ender.cataclysm.client.render.CMRenderTypes;
import L_Ender.cataclysm.entity.Ignis_Entity;
import L_Ender.cataclysm.entity.projectile.Ignis_Fireball_Entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;

import static net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY;

public class RendererIgnis_Fireball extends EntityRenderer<Ignis_Fireball_Entity> {
    private static final ResourceLocation IGNIS_FIRE_BALL = new ResourceLocation("cataclysm:textures/entity/ignis_fireball.png");
    private static final ResourceLocation IGNIS_FIRE_BALL_SOUL = new ResourceLocation("cataclysm:textures/entity/ignis_fireball_soul.png");

    public ModelIgnis_Fireball model;

    public RendererIgnis_Fireball(EntityRendererManager renderManager) {
        super(renderManager);
        this.model = new ModelIgnis_Fireball();
    }

    @Override
    public ResourceLocation getEntityTexture(Ignis_Fireball_Entity entity) {
        return entity.isSoul() ? IGNIS_FIRE_BALL_SOUL : IGNIS_FIRE_BALL;
    }



    @Override
    protected int getBlockLight(Ignis_Fireball_Entity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    public void render(Ignis_Fireball_Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
    {
        matrixStackIn.push();
        float f = rotLerp(entityIn.prevRotationYaw, entityIn.rotationYaw, partialTicks);
        float f1 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
        float f2 = (float) entityIn.ticksExisted + partialTicks;
        matrixStackIn.translate(0.0D, (double) 0.15F, 0.0D);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(f2 * 0.1F) * 180.0F));
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.cos(f2 * 0.1F) * 180.0F));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.sin(f2 * 0.15F) * 360.0F));
        this.model.setRotationAngles(entityIn, 0.0F, 0.0F, 0.0F, f, f1);
        IVertexBuilder VertexConsumer = bufferIn.getBuffer(this.model.getRenderType(getEntityTexture(entityIn)));
        this.model.render(matrixStackIn, VertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();

        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    /**
     * A helper method to do some Math Magic
     */
    private float rotLerp(float prevRotation, float rotation, float partialTicks)
    {
        float f;
        for(f = rotation - prevRotation; f < -180.0F; f += 360.0F)
        {
            ;
        }

        while(f >= 180.0F)
        {
            f -= 360.0F;
        }

        return prevRotation + partialTicks * f;
    }


}
