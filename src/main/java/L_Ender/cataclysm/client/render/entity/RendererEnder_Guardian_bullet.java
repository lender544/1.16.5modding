package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelEnder_Guardian_Bullet;
import L_Ender.cataclysm.entity.projectile.Ender_Guardian_Bullet_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererEnder_Guardian_bullet extends EntityRenderer<Ender_Guardian_Bullet_Entity>
{
	private static final ResourceLocation ENDER_GUARDIAN_TEXTURE = new ResourceLocation("cataclysm:textures/entity/shulkerbullet.png");
	private static final RenderType ENDER_GUARDIAN_RENDER_TYPE = RenderType.getEntityTranslucent(ENDER_GUARDIAN_TEXTURE);
	public ModelEnder_Guardian_Bullet<Ender_Guardian_Bullet_Entity> model;

	public RendererEnder_Guardian_bullet(EntityRendererManager manager)
	{
		super(manager);
		this.model = new ModelEnder_Guardian_Bullet<>();
	}
	
	@Override
	protected int getBlockLight(Ender_Guardian_Bullet_Entity entity, BlockPos pos)
	{
		return 15;
	}

	@Override
	public void render(Ender_Guardian_Bullet_Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		matrixStackIn.push();
		float f = rotLerp(entityIn.prevRotationYaw, entityIn.rotationYaw, partialTicks);
		float f1 = MathHelper.lerp(partialTicks, entityIn.prevRotationPitch, entityIn.rotationPitch);
		float f2 = (float) entityIn.ticksExisted + partialTicks;
		matrixStackIn.translate(0.0D, (double) 0.15F, 0.0D);
		matrixStackIn.rotate(Vector3f.YP.rotationDegrees(MathHelper.sin(f2 * 0.1F) * 180.0F));
		matrixStackIn.rotate(Vector3f.XP.rotationDegrees(MathHelper.cos(f2 * 0.1F) * 180.0F));
		matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(MathHelper.sin(f2 * 0.15F) * 360.0F));
		matrixStackIn.scale(-0.5F, -0.5F, 0.5F);
		this.model.setRotationAngles(entityIn, 0.0F, 0.0F, 0.0F, f, f1);
		IVertexBuilder ivertexbuilder = bufferIn.getBuffer(this.model.getRenderType(ENDER_GUARDIAN_TEXTURE));
		this.model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
		matrixStackIn.scale(1.5F, 1.5F, 1.5F);
		IVertexBuilder ivertexbuilder1 = bufferIn.getBuffer(ENDER_GUARDIAN_RENDER_TYPE);
		this.model.render(matrixStackIn, ivertexbuilder1, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.15F);
		matrixStackIn.pop();
		super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
	}

	@Override
	public ResourceLocation getEntityTexture(Ender_Guardian_Bullet_Entity entity)
	{
		return ENDER_GUARDIAN_TEXTURE;
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