package L_Ender.cataclysm.client.render;


import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector4f;

public class RenderUtils {
    public static void matrixStackFromModel(MatrixStack matrixStack, AdvancedModelBox modelRenderer) {
        AdvancedModelBox parent = modelRenderer.getParent();
        if (parent != null) matrixStackFromModel(matrixStack, parent);
        modelRenderer.translateRotate(matrixStack);
    }

    public static Vector3d getWorldPosFromModel(Entity entity, float entityYaw, AdvancedModelBox modelRenderer) {
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.translate(entity.getPosX(), entity.getPosY(), entity.getPosZ());
        matrixStack.rotate(new Quaternion(0, -entityYaw + 180, 0, true));
        matrixStack.scale(-1, -1, 1);
        matrixStack.translate(0, -1.5f, 0);
        RenderUtils.matrixStackFromModel(matrixStack, modelRenderer);
        MatrixStack.Entry matrixEntry = matrixStack.getLast();
        Matrix4f matrix4f = matrixEntry.getMatrix();

        Vector4f vec = new Vector4f(0, 0, 0, 1);
        vec.transform(matrix4f);
        return new Vector3d(vec.getX(), vec.getY(), vec.getZ());
    }

}
