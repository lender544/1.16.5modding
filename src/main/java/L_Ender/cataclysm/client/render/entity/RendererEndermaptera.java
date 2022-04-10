package L_Ender.cataclysm.client.render.entity;

import L_Ender.cataclysm.client.model.entity.ModelEndermaptera;
import L_Ender.cataclysm.client.render.layer.LayerGenericGlowing;
import L_Ender.cataclysm.entity.Endermaptera_Entity;
import L_Ender.cataclysm.entity.Netherite_Monstrosity_Entity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Pose;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RendererEndermaptera extends MobRenderer<Endermaptera_Entity, ModelEndermaptera> {

    private static final ResourceLocation SSAPBUG_TEXTURES = new ResourceLocation("cataclysm:textures/entity/ender_ssap_bug.png");
    private static final ResourceLocation SSAPBUG_LAYER_TEXTURES = new ResourceLocation("cataclysm:textures/entity/ender_ssap_bug_layer.png");

    public RendererEndermaptera(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new ModelEndermaptera(), 0.7F);
        this.addLayer(new LayerGenericGlowing(this, SSAPBUG_LAYER_TEXTURES));

    }
    @Override
    public ResourceLocation getEntityTexture(Endermaptera_Entity entity) {
        return SSAPBUG_TEXTURES;
    }

    @Override
    protected void preRenderCallback(Endermaptera_Entity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        matrixStackIn.scale(1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void applyRotations(Endermaptera_Entity entityLiving, MatrixStack matrixStackIn, float ageInTicks, float rotationYaw, float partialTicks) {
        if (this.func_230495_a_(entityLiving)) {
            rotationYaw += (float)(Math.cos((double)entityLiving.ticksExisted * 3.25D) * Math.PI * (double)0.4F);
        }
        float trans = 0.5F;
        Pose pose = entityLiving.getPose();
        if (pose != Pose.SLEEPING) {
            float progresso = 1F - (entityLiving.prevAttachChangeProgress + (entityLiving.attachChangeProgress - entityLiving.prevAttachChangeProgress) * partialTicks);

            if(entityLiving.getAttachmentFacing() == Direction.DOWN){
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees (180.0F - rotationYaw));
                matrixStackIn.translate(0.0D, trans, 0.0D);
                if(entityLiving.prevPosY < entityLiving.getPosY()){
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90 * (1 - progresso)));
                }else{
                    matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90 * (1 - progresso)));
                }
                matrixStackIn.translate(0.0D, -trans, 0.0D);

            }else if(entityLiving.getAttachmentFacing() == Direction.UP){
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees (180.0F - rotationYaw));
                matrixStackIn.rotate(Vector3f.XP.rotationDegrees(180));
                matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
                matrixStackIn.translate(0.0D, -trans, 0.0D);

            }else{
                matrixStackIn.translate(0.0D, trans, 0.0D);
                switch (entityLiving.getAttachmentFacing()){
                    case NORTH:
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F * progresso));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(0));
                        break;
                    case SOUTH:
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180.0F));
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F * progresso ));
                        break;
                    case WEST:
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F));
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90F - 90.0F * progresso));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(-90.0F));
                        break;
                    case EAST:
                        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90.0F ));
                        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90.0F * progresso - 90F));
                        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(90.0F));
                        break;
                }
                if(entityLiving.getMotion().y <= -0.001F){
                    matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-180.0F));
                }
                matrixStackIn.translate(0.0D, -trans, 0.0D);
            }
        }

        if (entityLiving.deathTime > 0) {
            float f = ((float)entityLiving.deathTime + partialTicks - 1.0F) / 20.0F * 1.6F;
            f = MathHelper.sqrt(f);
            if (f > 1.0F) {
                f = 1.0F;
            }

            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(f * this.getDeathMaxRotation(entityLiving)));
        } else if (entityLiving.isSpinAttacking()) {
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(-90.0F - entityLiving.rotationPitch));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(((float)entityLiving.ticksExisted + partialTicks) * -75.0F));
        } else if (pose == Pose.SLEEPING) {

        } else if (entityLiving.hasCustomName() ) {
            String s = TextFormatting.getTextWithoutFormattingCodes(entityLiving.getName().getString());
            if (("Dinnerbone".equals(s) || "Grumm".equals(s))) {
                matrixStackIn.translate(0.0D, (double)(entityLiving.getHeight() + 0.1F), 0.0D);
                matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(180.0F));
            }
        }
    }

}