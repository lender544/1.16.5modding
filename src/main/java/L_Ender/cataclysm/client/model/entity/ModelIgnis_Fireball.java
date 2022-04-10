package L_Ender.cataclysm.client.model.entity;

import L_Ender.cataclysm.entity.projectile.Ignis_Fireball_Entity;
import com.github.alexthe666.citadel.client.model.AdvancedEntityModel;
import com.github.alexthe666.citadel.client.model.AdvancedModelBox;
import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelIgnis_Fireball extends AdvancedEntityModel<Entity> {
    private final AdvancedModelBox root;
    private final AdvancedModelBox glass;
    private final AdvancedModelBox cube;

    public ModelIgnis_Fireball() {
        textureWidth = 64;
        textureHeight = 64;

        root = new AdvancedModelBox(this);
        root.setRotationPoint(0.0F, 24.0F, 0.0F);


        glass = new AdvancedModelBox(this);
        glass.setRotationPoint(0.0F, -5.0F, 0.0F);
        root.addChild(glass);
        glass.setTextureOffset(0, 21).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

        cube = new AdvancedModelBox(this);
        cube.setRotationPoint(0.0F, -5.0F, 0.0F);
        root.addChild(cube);
        cube.setTextureOffset(0, 0).addBox(-5.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F, 0.0F, false);
        this.updateDefaultPose();
    }

    @Override
    public Iterable<ModelRenderer> getParts() {
        return ImmutableList.of(root);
    }

    @Override
    public Iterable<AdvancedModelBox> getAllParts() {
        return ImmutableList.of(root, cube, glass);
    }

    @Override
    public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
        this.resetToDefaultPose();
    }


    public void setRotationAngle(AdvancedModelBox AdvancedModelBox, float x, float y, float z) {
        AdvancedModelBox.rotateAngleX = x;
        AdvancedModelBox.rotateAngleY = y;
        AdvancedModelBox.rotateAngleZ = z;
    }

    public void animate(Ignis_Fireball_Entity entityIn, float ageInTicks) {
        this.resetToDefaultPose();
        float innerScale = (float) (1.0F + 0.25F * Math.abs(Math.sin(ageInTicks * 0.6F)));
        float outerScale = (float) (1.0F + 0.5F * Math.abs(Math.cos(ageInTicks * 0.2F)));
        this.glass.setScale(innerScale, innerScale, innerScale);
        this.glass.rotateAngleX += ageInTicks * 0.25F;
        this.cube.rotateAngleX += ageInTicks * 0.5F;
        this.glass.setShouldScaleChildren(false);
        this.cube.setScale(outerScale, outerScale, outerScale);
    }
}