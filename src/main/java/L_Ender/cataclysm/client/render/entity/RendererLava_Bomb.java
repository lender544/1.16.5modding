package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelLava_Bomb;
import L_Ender.cataclysm.entity.projectile.Lava_Bomb_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class RendererLava_Bomb extends EntityRenderer<Lava_Bomb_Entity> {

    private static final ResourceLocation FIRE_BOMB_TEXTURES = new ResourceLocation("cataclysm:textures/entity/fire_bomb.png");
    private final ModelLava_Bomb model = new ModelLava_Bomb();


    public RendererLava_Bomb(EntityRendererManager renderManagerIn) {
        super(renderManagerIn);
    }

    @Override
    public void render(Lava_Bomb_Entity entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn) {
        matrixStackIn.push();
        matrixStackIn.rotate(new Quaternion(new Vector3f(0, -1, 0), entityYaw, true));
        IVertexBuilder ivertexbuilder = bufferIn.getBuffer(RenderType.getEntityTranslucent(this.getEntityTexture(entityIn)));
        model.setRotationAngles(entityIn, 0, 0, entityIn.ticksExisted + partialTicks, 0, 0);
        model.render(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        matrixStackIn.pop();
    }

    protected int getBlockLight(Lava_Bomb_Entity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    public ResourceLocation getEntityTexture(Lava_Bomb_Entity entity) {
        return FIRE_BOMB_TEXTURES;
    }
}
