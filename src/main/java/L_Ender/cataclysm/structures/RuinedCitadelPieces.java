package L_Ender.cataclysm.structures;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.entity.Ender_Golem_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModStructures;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.ShulkerEntity;
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

public class RuinedCitadelPieces {

    private static final ResourceLocation CITADEL1 = new ResourceLocation(cataclysm.MODID, "ruined_citadel1");
    private static final ResourceLocation CITADEL2 = new ResourceLocation(cataclysm.MODID, "ruined_citadel2");
    private static final ResourceLocation CITADEL3 = new ResourceLocation(cataclysm.MODID, "ruined_citadel3");
    private static final ResourceLocation CITADEL4 = new ResourceLocation(cataclysm.MODID, "ruined_citadel4");
    private static final ResourceLocation CITADEL5 = new ResourceLocation(cataclysm.MODID, "ruined_citadel5");
    private static final ResourceLocation CITADEL6 = new ResourceLocation(cataclysm.MODID, "ruined_citadel6");
    private static final ResourceLocation CITADEL7 = new ResourceLocation(cataclysm.MODID, "ruined_citadel7");
    private static final ResourceLocation CITADEL8 = new ResourceLocation(cataclysm.MODID, "ruined_citadel8");
    private static final ResourceLocation CITADEL9 = new ResourceLocation(cataclysm.MODID, "ruined_citadel9");
    private static final ResourceLocation CITADEL10 = new ResourceLocation(cataclysm.MODID, "ruined_citadel10");
    private static final ResourceLocation CITADEL11 = new ResourceLocation(cataclysm.MODID, "ruined_citadel11");
    private static final ResourceLocation CITADEL12 = new ResourceLocation(cataclysm.MODID, "ruined_citadel12");
    private static final ResourceLocation CITADEL13 = new ResourceLocation(cataclysm.MODID, "ruined_citadel13");
    private static final ResourceLocation CITADEL14 = new ResourceLocation(cataclysm.MODID, "ruined_citadel14");
    private static final ResourceLocation CITADEL15 = new ResourceLocation(cataclysm.MODID, "ruined_citadel15");
    private static final ResourceLocation CITADEL16 = new ResourceLocation(cataclysm.MODID, "ruined_citadel16");
    private static final ResourceLocation CITADEL17 = new ResourceLocation(cataclysm.MODID, "ruined_citadel17");
    private static final ResourceLocation CITADEL18 = new ResourceLocation(cataclysm.MODID, "ruined_citadel18");

    private static final Map<ResourceLocation, BlockPos> OFFSET = ImmutableMap.<ResourceLocation, BlockPos>builder()
            .put(CITADEL1, new BlockPos(0, 1, 0))
            .put(CITADEL2, new BlockPos(0, 1, 0))
            .put(CITADEL3, new BlockPos(0, 1, 0))
            .put(CITADEL4, new BlockPos(0, 1, 0))
            .put(CITADEL5, new BlockPos(0, 1, 0))
            .put(CITADEL6, new BlockPos(0, 1, 0))
            .put(CITADEL7, new BlockPos(0, 1, 0))
            .put(CITADEL8, new BlockPos(0, 1, 0))
            .put(CITADEL9, new BlockPos(0, 1, 0))
            .put(CITADEL10, new BlockPos(0, 1, 0))
            .put(CITADEL11, new BlockPos(0, 1, 0))
            .put(CITADEL12, new BlockPos(0, 1, 0))
            .put(CITADEL13, new BlockPos(0, 1, 0))
            .put(CITADEL14, new BlockPos(0, 1, 0))
            .put(CITADEL15, new BlockPos(0, 1, 0))
            .put(CITADEL16, new BlockPos(0, 1, 0))
            .put(CITADEL17, new BlockPos(0, 1, 0))
            .put(CITADEL18, new BlockPos(0, 1, 0))
            .build();

    /*
     * Begins assembling your structure and where the pieces needs to go.
     */
    public static void start(TemplateManager templateManager, BlockPos pos, Rotation rotation, List<StructurePiece> pieceList, Random random) {
        int x = pos.getX();
        int z = pos.getZ();


        BlockPos rotationOffSet = new BlockPos(0, -45, 0).rotate(rotation);
        BlockPos blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL5, blockpos, rotation));

        rotationOffSet = new BlockPos(0, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL14, blockpos, rotation));


        rotationOffSet = new BlockPos(0, -45, 37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL6, blockpos, rotation));

        rotationOffSet = new BlockPos(0, 0, 37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL15, blockpos, rotation));


        rotationOffSet = new BlockPos(0, -45, -37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL4, blockpos, rotation));

        rotationOffSet = new BlockPos(0, 0, -37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL13, blockpos, rotation));


        rotationOffSet = new BlockPos(-36, -45, 0).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL2, blockpos, rotation));

        rotationOffSet = new BlockPos(-36, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL11, blockpos, rotation));


        rotationOffSet = new BlockPos(36, -45, 0).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL8, blockpos, rotation));

        rotationOffSet = new BlockPos(36, 0, 0).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL17, blockpos, rotation));


        rotationOffSet = new BlockPos(-36, -45, -37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL1, blockpos, rotation));

        rotationOffSet = new BlockPos(-36, 0, -37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL10, blockpos, rotation));


        rotationOffSet = new BlockPos(-36, -45, 37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL3, blockpos, rotation));

        rotationOffSet = new BlockPos(-36, 0, 37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL12, blockpos, rotation));


        rotationOffSet = new BlockPos(36, -45, 37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL9, blockpos, rotation));

        rotationOffSet = new BlockPos(36, 0, 37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL18, blockpos, rotation));


        rotationOffSet = new BlockPos(36, -45, -37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL7, blockpos, rotation));

        rotationOffSet = new BlockPos(36, 0, -37).rotate(rotation);
        blockpos = rotationOffSet.add(x, pos.getY(), z);
        pieceList.add(new RuinedCitadelPieces.Piece(templateManager, CITADEL16, blockpos, rotation));


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
            super(ModStructures.RCP, 0);
            this.resourceLocation = resourceLocationIn;
            BlockPos blockpos = RuinedCitadelPieces.OFFSET.get(resourceLocation);
            this.templatePosition = pos.add(blockpos.getX(), blockpos.getY(), blockpos.getZ());
            this.rotation = rotationIn;
            this.setupPiece(templateManagerIn);
        }

        public Piece(TemplateManager templateManagerIn, CompoundNBT tagCompound) {
            super(ModStructures.RCP, tagCompound);
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
            if ("sentry".equals(function)) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                ShulkerEntity shulkerentity = EntityType.SHULKER.create(worldIn.getWorld());
                shulkerentity.setPosition((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D);
                shulkerentity.setAttachmentPos(pos);
                worldIn.addEntity(shulkerentity);
            }else if ("mimic".equals(function)) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                ShulkerEntity Silentshulkerentity = EntityType.SHULKER.create(worldIn.getWorld());
                Silentshulkerentity.setPosition((double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D);
                Silentshulkerentity.setAttachmentPos(pos);
                Silentshulkerentity.setSilent(true);
                worldIn.addEntity(Silentshulkerentity);
            }else if ("golem".equals(function)) {
                worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
                Ender_Golem_Entity Golem = ModEntities.ENDER_GOLEM.get().create(worldIn.getWorld());
                Golem.moveToBlockPosAndAngles(pos, 180.0F, 180.0F);
                worldIn.addEntity(Golem);
            }

        }
    }
}
