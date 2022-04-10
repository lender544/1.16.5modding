package L_Ender.cataclysm.client.model.armor;

import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ArmorStandEntity;

public class ModelIgnitium_Armor extends BipedModel {
    public ModelRenderer right_helmet;
    public ModelRenderer left_helmet;
    public ModelRenderer left_horn;
    public ModelRenderer left_horn2;
    public ModelRenderer left_horn3;
    public ModelRenderer right_horn;
    public ModelRenderer right_horn2;
    public ModelRenderer right_horn3;
    public ModelRenderer headplate;
    public ModelRenderer outer_body;
    public ModelRenderer inner_body;
    public ModelRenderer right_shoulderpad;
    public ModelRenderer left_shoulderpad;
    public ModelRenderer right_spike;
    public ModelRenderer left_spike;
    public ModelRenderer side_spike;
    public ModelRenderer side_spike2;

    public ModelIgnitium_Armor(float modelSize) {
        super(modelSize, 0, 64, 64);
        this.textureWidth = 64;
        this.textureHeight = 64;
        right_helmet = new ModelRenderer(this);
        right_helmet.setRotationPoint(-4.75F, 0.3F, -4.75F);
        bipedHead.addChild(right_helmet);
        setRotateAngle(right_helmet, 0.0F, -0.829F, 0.0F);
        right_helmet.setTextureOffset(0, 35).addBox(0.0F, -1.5F, -4.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);

        left_helmet = new ModelRenderer(this);
        left_helmet.setRotationPoint(4.75F, 0.3F, -4.75F);
        bipedHead.addChild(left_helmet);
        setRotateAngle(left_helmet, 0.0F, 0.829F, 0.0F);
        left_helmet.setTextureOffset(0, 35).addBox(0.0F, -1.5F, -4.0F, 0.0F, 3.0F, 6.0F, 0.0F, true);

        left_horn = new ModelRenderer(this);
        left_horn.setRotationPoint(3.6F, -6.5F, -3.6F);
        bipedHead.addChild(left_horn);
        setRotateAngle(left_horn, 0.3927F, -0.2182F, 0.1309F);
        left_horn.setTextureOffset(54, 43).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

        left_horn2 = new ModelRenderer(this);
        left_horn2.setRotationPoint(0.0F, -5.0F, -1.0F);
        left_horn.addChild(left_horn2);
        setRotateAngle(left_horn2, -1.3526F, 0.0F, 0.0F);
        left_horn2.setTextureOffset(13, 41).addBox(-0.5F, -7.0F, 0.0F, 1.0F, 7.0F, 2.0F, 0.0F, false);

        left_horn3 = new ModelRenderer(this);
        left_horn3.setRotationPoint(0.0F, -7.0F, 2.0F);
        left_horn2.addChild(left_horn3);
        setRotateAngle(left_horn3, -0.5236F, 0.0F, 0.0F);
        left_horn3.setTextureOffset(53, 37).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 1.0F, 4.0F, -0.01F, false);

        right_horn = new ModelRenderer(this);
        right_horn.setRotationPoint(-3.6F, -6.5F, -3.6F);
        bipedHead.addChild(right_horn);
        setRotateAngle(right_horn, 0.3927F, 0.2182F, -0.1309F);
        right_horn.setTextureOffset(54, 43).addBox(-1.0F, -5.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, true);

        right_horn2 = new ModelRenderer(this);
        right_horn2.setRotationPoint(0.0F, -5.0F, -1.0F);
        right_horn.addChild(right_horn2);
        setRotateAngle(right_horn2, -1.3526F, 0.0F, 0.0F);
        right_horn2.setTextureOffset(13, 41).addBox(-0.5F, -7.0F, 0.0F, 1.0F, 7.0F, 2.0F, 0.0F, true);

        right_horn3 = new ModelRenderer(this);
        right_horn3.setRotationPoint(0.0F, -7.0F, 2.0F);
        right_horn2.addChild(right_horn3);
        setRotateAngle(right_horn3, -0.5236F, 0.0F, 0.0F);
        right_horn3.setTextureOffset(53, 37).addBox(-0.5F, 0.0F, -4.0F, 1.0F, 1.0F, 4.0F, -0.01F, true);

        headplate = new ModelRenderer(this);
        headplate.setRotationPoint(0.0F, -5.5F, -4.25F);
        bipedHead.addChild(headplate);
        setRotateAngle(headplate, -0.2618F, 0.0F, 0.0F);
        headplate.setTextureOffset(48, 34).addBox(-1.5F, -1.5F, -1.0F, 3.0F, 3.0F, 1.0F, 0.0F, false);

        outer_body = new ModelRenderer(this);
        outer_body.setRotationPoint(0.0F, -1.0F, 0.0F);
        bipedBody.addChild(outer_body);
        outer_body.setTextureOffset(30, 47).addBox(-4.5F, 1.0F, -2.5F, 9.0F, 12.0F, 5.0F, 0.4F, false);

        inner_body = new ModelRenderer(this);
        inner_body.setRotationPoint(0.0F, 11.0F, 0.0F);
        bipedBody.addChild(inner_body);
        inner_body.setTextureOffset(0, 51).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 9.0F, 4.0F, 0.5F, false);

        left_shoulderpad = new ModelRenderer(this);
        left_shoulderpad.setRotationPoint(5.0F, 4.0F, 0.0F);
        bipedLeftArm.addChild(left_shoulderpad);
        left_shoulderpad.setTextureOffset(30, 33).addBox(-6.0F, -7.0F, -3.0F, 5.0F, 7.0F, 6.0F, 0.3F, false);

        left_spike = new ModelRenderer(this);
        left_spike.setRotationPoint(-1.0F, -8.5F, 0.0F);
        left_shoulderpad.addChild(left_spike);
        setRotateAngle(left_spike, 0.0F, 0.0F, 0.6109F);
        left_spike.setTextureOffset(21, 43).addBox(-1.0F, -3.5F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, false);

        side_spike = new ModelRenderer(this);
        side_spike.setRotationPoint(2.5F, 3.0F, 0.5F);
        left_spike.addChild(side_spike);
        setRotateAngle(side_spike, 0.0F, 0.0F, 0.829F);
        side_spike.setTextureOffset(30, 47).addBox(0.5F, -3.5F, -0.5F, 2.0F, 4.0F, 0.0F, 0.0F, false);

        right_shoulderpad = new ModelRenderer(this);
        right_shoulderpad.setRotationPoint(-4.0F, 4.0F, 0.0F);
        bipedRightArm.addChild(right_shoulderpad);
        right_shoulderpad.setTextureOffset(30, 33).addBox(0.0F, -7.0F, -3.0F, 5.0F, 7.0F, 6.0F, 0.3F, true);

        right_spike = new ModelRenderer(this);
        right_spike.setRotationPoint(0.0F, -8.5F, 0.0F);
        right_shoulderpad.addChild(right_spike);
        setRotateAngle(right_spike, 0.0F, 0.0F, -0.6109F);
        right_spike.setTextureOffset(21, 43).addBox(-3.0F, -3.5F, 0.0F, 4.0F, 7.0F, 0.0F, 0.0F, true);

        side_spike2 = new ModelRenderer(this);
        side_spike2.setRotationPoint(-2.5F, 3.0F, 0.5F);
        right_spike.addChild(side_spike2);
        setRotateAngle(side_spike2, 0.0F, 0.0F, -0.829F);
        side_spike2.setTextureOffset(30, 47).addBox(-2.5F, -3.5F, -0.5F, 2.0F, 4.0F, 0.0F, 0.0F, true);
    }

    public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.rotateAngleX = x;
        modelRenderer.rotateAngleY = y;
        modelRenderer.rotateAngleZ = z;
    }

    public void setRotationAngles(LivingEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityIn instanceof ArmorStandEntity) {
            ArmorStandEntity entityarmorstand = (ArmorStandEntity) entityIn;
            this.bipedHead.rotateAngleX = 0.017453292F * entityarmorstand.getHeadRotation().getX();
            this.bipedHead.rotateAngleY = 0.017453292F * entityarmorstand.getHeadRotation().getY();
            this.bipedHead.rotateAngleZ = 0.017453292F * entityarmorstand.getHeadRotation().getZ();
            this.bipedHead.setRotationPoint(0.0F, 1.0F, 0.0F);
            this.bipedBody.rotateAngleX = 0.017453292F * entityarmorstand.getBodyRotation().getX();
            this.bipedBody.rotateAngleY = 0.017453292F * entityarmorstand.getBodyRotation().getY();
            this.bipedBody.rotateAngleZ = 0.017453292F * entityarmorstand.getBodyRotation().getZ();
            this.bipedLeftArm.rotateAngleX = 0.017453292F * entityarmorstand.getLeftArmRotation().getX();
            this.bipedLeftArm.rotateAngleY = 0.017453292F * entityarmorstand.getLeftArmRotation().getY();
            this.bipedLeftArm.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftArmRotation().getZ();
            this.bipedRightArm.rotateAngleX = 0.017453292F * entityarmorstand.getRightArmRotation().getX();
            this.bipedRightArm.rotateAngleY = 0.017453292F * entityarmorstand.getRightArmRotation().getY();
            this.bipedRightArm.rotateAngleZ = 0.017453292F * entityarmorstand.getRightArmRotation().getZ();
            this.bipedLeftLeg.rotateAngleX = 0.017453292F * entityarmorstand.getLeftLegRotation().getX();
            this.bipedLeftLeg.rotateAngleY = 0.017453292F * entityarmorstand.getLeftLegRotation().getY();
            this.bipedLeftLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getLeftLegRotation().getZ();
            this.bipedLeftLeg.setRotationPoint(1.9F, 11.0F, 0.0F);
            this.bipedRightLeg.rotateAngleX = 0.017453292F * entityarmorstand.getRightLegRotation().getX();
            this.bipedRightLeg.rotateAngleY = 0.017453292F * entityarmorstand.getRightLegRotation().getY();
            this.bipedRightLeg.rotateAngleZ = 0.017453292F * entityarmorstand.getRightLegRotation().getZ();
            this.bipedRightLeg.setRotationPoint(-1.9F, 11.0F, 0.0F);
            this.bipedHeadwear.copyModelAngles(this.bipedHead);
        } else {
            super.setRotationAngles(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        }
    }
}
