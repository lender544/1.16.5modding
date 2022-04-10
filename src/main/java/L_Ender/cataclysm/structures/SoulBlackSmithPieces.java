package L_Ender.cataclysm.structures;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.entity.Netherite_Monstrosity_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModStructures;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class SoulBlackSmithPieces {

    private static final ResourceLocation UPPER_SIDE = new ResourceLocation(cataclysm.MODID, "soulblacksmith2");
    private static final ResourceLocation BOTTOM_SIDE = new ResourceLocation(cataclysm.MODID, "soulblacksmith");
    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.of(UPPER_SIDE, new BlockPos(0, 1, 0), BOTTOM_SIDE, new BlockPos(0, 1, 0));

    /*
     * Begins assembling your structure and where the pieces needs to go.
     */
    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
        int x = pos.getX();
        int z = pos.getZ();

        BlockPos rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new SoulBlackSmithPieces.Piece(templateManager, UPPER_SIDE, blockpos, rotation));

        rotationOffSet = new BlockPos(0, 0, -34).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new SoulBlackSmithPieces.Piece(templateManager, BOTTOM_SIDE, blockpos, rotation));
    }

    /*
     * Here's where some voodoo happens. Most of this doesn't need to be touched but you do
     * have to pass in the IStructurePieceType you registered into the super constructors.
     *
     * The method you will most likely want to touch is the handleDataMarker method.
     */
    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation resourceLocation;
        private Rotation rotation;

        public Piece(TemplateManager templateManagerIn, ResourceLocation resourceLocationIn, BlockPos pos, Rotation rotationIn) {
            super(ModStructures.SBSP, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = SoulBlackSmithPieces.OFFSET.get(resourceLocation);
            this.templatePosition = pos.add(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound) {
            super(ModStructures.SBSP, tagCompound);
            this.resourceLocation = new ResourceLocation(tagCompound.getString("Template"));
            this.rotation = Rotation.valueOf(tagCompound.getString("Rot"));
            this.setupPiece(templateManagerIn);
        }

        private void setupPiece(TemplateManager templateManager) {
            Template template = templateManager.getTemplateDefaulted(this.resourceLocation);
            PlacementSettings placementsettings = (new PlacementSettings()).setRotation(this.rotation).setMirror(Mirror.NONE);
            this.setup(template, this.templatePosition, placementsettings);
        }

        /**
         * (abstract) Helper method to read subclass data from NBT
         */
        @Override
        protected void readAdditional(CompoundNBT tagCompound) {
            super.readAdditional(tagCompound);
            tagCompound.putString("Template", this.resourceLocation.toString());
            tagCompound.putString("Rot", this.rotation.name());
        }

        /*
         * If you added any data marker structure blocks to your structure, you can access and modify them here.
         * In this case, our structure has a data maker with the string "chest" put into it. So we check to see
         * if the incoming function is "chest" and if it is, we now have that exact position.
         *
         * So what is done here is we replace the structure block with
         * a chest and we can then set the loottable for it.
         *
         * You can set other data markers to do other behaviors such as spawn a random mob in a certain spot,
         * randomize what rare block spawns under the floor, or what item an Item Frame will have.
         */
        @Override
        protected void handleDataMarker(String function, BlockPos pos, IServerWorld worldIn, Random rand, MutableBoundingBox sbb) {
            if ("monstrosity".equals(function)) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                Netherite_Monstrosity_Entity monstrosity = ModEntities.NETHERITE_MONSTROSITY.get().create(worldIn.getWorld());
                monstrosity.moveToBlockPosAndAngles(pos, 180.0F, 180.0F);
                worldIn.addEntity(monstrosity);
            }
        }
    }
}
