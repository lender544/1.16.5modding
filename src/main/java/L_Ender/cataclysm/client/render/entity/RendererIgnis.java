package L_Ender.cataclysm.client.render.entity;


import L_Ender.cataclysm.client.model.entity.ModelIgnis;
import L_Ender.cataclysm.entity.Ignis_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererIgnis extends MobRenderer<Ignis_Entity, ModelIgnis> {

    private static final ResourceLocation IGNIS_TEXTURES = new ResourceLocation("cataclysm:textures/entity/ignis.png");

    private static final ResourceLocation IGNIS_SOUL_TEXTURES = new ResourceLocation("cataclysm:textures/entity/ignis_soul.png");

    public RendererIgnis(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelIgnis(), 1.0F);

    }
    @Override
    public ResourceLocation getEntityTexture(Ignis_Entity entity) {
        return entity.getBossPhase() > 0 ? IGNIS_SOUL_TEXTURES : IGNIS_TEXTURES;
    }

    @Override
    protected void preRenderCallback(Ignis_Entity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    protected int getBlockLight(Ignis_Entity entityIn, BlockPos pos) {
        return 15;
    }

    @Override
    protected float getDeathMaxRotation(Ignis_Entity entity) {
        return 0;
    }

}