package L_Ender.cataclysm.client.model.item;// Made with Blockbench 4.0.4
// Exported for Minecraft version 1.15 - 1.16 with MCP mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelGauntlet_of_Guard extends EntityModel<Entity> {
	private final ModelRenderer root;
	private final ModelRenderer gauntlet_fist;
	private final ModelRenderer void_stone_knuckle;
	private final ModelRenderer gauntlet_arm;
	private final ModelRenderer gauntlet_arm2;
	private final ModelRenderer gauntlet_shoulder;
	private final ModelRenderer big_void_stone;

	public ModelGauntlet_of_Guard() {
		textureWidth = 64;
		textureHeight = 64;

		root = new ModelRenderer(this);
		root.setRotationPoint(0.0F, 9.0F, 0.0F);
		

		gauntlet_fist = new ModelRenderer(this);
		gauntlet_fist.setRotationPoint(0.0F, 0.0F, -3.5F);
		root.addChild(gauntlet_fist);
		gauntlet_fist.setTextureOffset(24, 30).addBox(-4.0F, -4.0F, -8.0F, 8.0F, 8.0F, 8.0F, 0.25F, false);

		void_stone_knuckle = new ModelRenderer(this);
		void_stone_knuckle.setRotationPoint(-5.25F, -2.5F, -6.0F);
		gauntlet_fist.addChild(void_stone_knuckle);
		void_stone_knuckle.setTextureOffset(0, 0).addBox(0.0F, 0.5F, 0.0F, 1.0F, 4.0F, 4.0F, 0.0F, false);

		gauntlet_arm = new ModelRenderer(this);
		gauntlet_arm.setRotationPoint(0.0F, 0.0F, 2.5F);
		root.addChild(gauntlet_arm);
		gauntlet_arm.setTextureOffset(0, 22).addBox(-4.0F, -4.0F, 0.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		gauntlet_arm2 = new ModelRenderer(this);
		gauntlet_arm2.setRotationPoint(0.0F, 0.0F, -3.5F);
		root.addChild(gauntlet_arm2);
		gauntlet_arm2.setTextureOffset(34, 16).addBox(-4.0F, -4.0F, 0.0F, 6.0F, 8.0F, 6.0F, 0.0F, false);

		gauntlet_shoulder = new ModelRenderer(this);
		gauntlet_shoulder.setRotationPoint(2.0F, 0.0F, 9.0F);
		gauntlet_arm2.addChild(gauntlet_shoulder);
		setRotationAngle(gauntlet_shoulder, 0.0F, 0.6109F, 0.0F);
		gauntlet_shoulder.setTextureOffset(0, 0).addBox(-8.0F, -5.0F, -6.0F, 8.0F, 10.0F, 12.0F, 0.0F, false);

		big_void_stone = new ModelRenderer(this);
		big_void_stone.setRotationPoint(-8.0F, 0.0F, -2.0F);
		gauntlet_shoulder.addChild(big_void_stone);
		setRotationAngle(big_void_stone, 0.0F, 0.6109F, 0.0F);
		big_void_stone.setTextureOffset(28, 0).addBox(-8.0F, -2.0F, 0.0F, 8.0F, 4.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setRotationAngles(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}

	@Override
	public void render(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		root.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}