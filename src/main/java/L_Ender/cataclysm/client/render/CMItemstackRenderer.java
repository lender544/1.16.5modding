package L_Ender.cataclysm.client.render;

import L_Ender.cataclysm.client.model.item.ModelBulwark_of_the_flame;
import L_Ender.cataclysm.client.model.item.ModelGauntlet_of_Guard;
import L_Ender.cataclysm.init.ModItems;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CMItemstackRenderer extends ItemStackTileEntityRenderer {

    private static final ModelBulwark_of_the_flame BULWARK_OF_THE_FLAME_MODEL = new ModelBulwark_of_the_flame();
    private static final ModelGauntlet_of_Guard GAUNTLET_OF_GUARD_MODEL = new ModelGauntlet_of_Guard();
    private static final ResourceLocation BULWARK_OF_THE_FLAME_TEXTURE = new ResourceLocation("cataclysm:textures/items/bulwark_of_the_flame.png");
    private static final ResourceLocation GAUNTLET_OF_GUARD_TEXTURE = new ResourceLocation("cataclysm:textures/items/gauntlet_of_guard.png");

    @Override
    public void func_239207_a_(ItemStack itemStackIn, ItemCameraTransforms.TransformType p_239207_2_, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (itemStackIn.getItem() == ModItems.BULWARK_OF_THE_FLAME.get()) {
            matrixStackIn.push();
            matrixStackIn.translate(0.4F, -0.75F, 0.5F);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(-180));
            BULWARK_OF_THE_FLAME_MODEL.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(BULWARK_OF_THE_FLAME_TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }

        if (itemStackIn.getItem() == ModItems.GAUNTLET_OF_GUARD.get()) {
            matrixStackIn.push();
            matrixStackIn.translate(0.5F, 0.5F, 0.5F);
            GAUNTLET_OF_GUARD_MODEL.render(matrixStackIn, bufferIn.getBuffer(RenderType.getEntityCutoutNoCull(GAUNTLET_OF_GUARD_TEXTURE)), combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }


    }
}
