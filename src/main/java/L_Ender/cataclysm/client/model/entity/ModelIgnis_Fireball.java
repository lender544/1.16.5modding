package L_Ender.cataclysm.client.model.entity;

import L_Ender.cataclysm.entity.projectile.Ignis_Fireball_Entity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelIgnis_Fireball extends AdvancedEntityModel<Entity> {

    private final AdvancedModelBox root;
    private final AdvancedModelBox core;
    private final AdvancedModelBox out_line;

    public ModelIgnis_Fireball() {
        textureWidth = 64;
        textureHeight = 64;

        root = new AdvancedModelBox(this);
        root.setRotationPoint(0.0F, 0.0F, 0.0F);


        core = new AdvancedModelBox(this);
        core.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(core);
        core.setTextureOffset(20, 37).addBox(-4.0F, -4.0F, -1.0F, 8.0F, 8.0F, 2.0F, 0.0F, false);
        core.setTextureOffset(0, 31).addBox(-1.0F, -4.0F, -4.0F, 2.0F, 8.0F, 8.0F, 0.0F, false);
        core.setTextureOffset(0, 21).addBox(-4.0F, -1.0F, -4.0F, 8.0F, 2.0F, 8.0F, 0.0F, false);

        out_line = new AdvancedModelBox(this);
        out_line.setRotationPoint(0.0F, 0.0F, 0.0F);
        root.addChild(out_line);
        out_line.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(root,core,out_line);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.root.rotateAngleY = netHeadYaw * ((float) Math.PI / 180F);
        this.root.rotateAngleX = headPitch * ((float) Math.PI / 180F);
    }


    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

}