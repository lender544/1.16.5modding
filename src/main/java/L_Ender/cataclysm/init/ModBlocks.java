package L_Ender.cataclysm.init;


import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.blocks.BlockEnderGuardianSpawner;
import L_Ender.cataclysm.blocks.EndStoneTeleportTrapBricks;
import L_Ender.cataclysm.blocks.ObsidianExplosionTrapBricks;
import L_Ender.cataclysm.blocks.PurpurVoidRuneTrapBlock;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.ToIntFunction;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS,
            cataclysm.MODID);

    public static final RegistryObject<Block> WITHERITE_BLOCK = BLOCKS.register("witherite_block",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.BLACK)
                    .hardnessAndResistance(50f, 1200f)
                    .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.NETHERITE)));

    public static final RegistryObject<Block> ENDERRITE_BLOCK = BLOCKS.register("enderite_block",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.BLACK)
                    .hardnessAndResistance(50f, 1200f)
                    .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.NETHERITE)));

    public static final RegistryObject<Block> IGNITIUM_BLOCK = BLOCKS.register("ignitium_block",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.YELLOW)
                    .hardnessAndResistance(50f, 1200f)
                    .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .sound(SoundType.NETHERITE).setLightLevel((state) -> {
                        return 15;
                    })));

    public static final RegistryObject<Block> POLISHED_END_STONE = BLOCKS.register("polished_end_stone",
            () -> new Block(AbstractBlock.Properties.from(Blocks.END_STONE)));

    public static final RegistryObject<Block> POLISHED_END_STONE_SLAB = BLOCKS.register("polished_end_stone_slab",
            () -> new SlabBlock(AbstractBlock.Properties.from(POLISHED_END_STONE.get())));

    public static final RegistryObject<Block> POLISHED_END_STONE_STAIRS = BLOCKS.register("polished_end_stone_stairs",
            () -> new StairsBlock(POLISHED_END_STONE.get().getDefaultState(),AbstractBlock.Properties.from(POLISHED_END_STONE.get())));


    public static final RegistryObject<Block> CHISELED_END_STONE_BRICKS = BLOCKS.register("chiseled_end_stone_bricks",
            () -> new Block(AbstractBlock.Properties.from(Blocks.END_STONE_BRICKS)));

    public static final RegistryObject<Block> VOID_INFUSED_END_STONE_BRICKS = BLOCKS.register("void_infused_end_stone_bricks",
            () -> new Block(AbstractBlock.Properties.from(Blocks.END_STONE_BRICKS)
                    .setLightLevel((state) -> {
                        return 7;
                    })));

    public static final RegistryObject<Block> VOID_STONE = BLOCKS.register("void_stone",
            () -> new Block(AbstractBlock.Properties.create(Material.IRON, MaterialColor.PURPLE)
                    .setRequiresTool()
                    .harvestTool(ToolType.PICKAXE).harvestLevel(3)
                    .hardnessAndResistance(50f, 1200f).setLightLevel((state) -> {
                        return 7;
                    })));

    public static final RegistryObject<Block> VOID_LANTERN_BLOCK = BLOCKS.register("void_lantern_block" ,
            () -> new Block(AbstractBlock.Properties.create(Material.GLASS, MaterialColor.QUARTZ)
            .sound(SoundType.GLASS)
            .setRequiresTool()
            .harvestTool(ToolType.PICKAXE).harvestLevel(3)
            .hardnessAndResistance(50f, 1200f)
            .setLightLevel((state) -> {
        return 15;
    })));

    public static final RegistryObject<Block> END_STONE_PILLAR = BLOCKS.register("end_stone_pillar",
            () -> new RotatedPillarBlock(AbstractBlock.Properties.create(Material.ROCK, MaterialColor.QUARTZ)
                    .setRequiresTool()
                    .hardnessAndResistance(3f, 9f)));

    public static final RegistryObject<Block> CHISELED_PURPUR_BLOCK = BLOCKS.register("chiseled_purpur_block",
            () -> new Block(AbstractBlock.Properties.from(Blocks.PURPUR_BLOCK)));

    public static final RegistryObject<Block> OBSIDIAN_BRICKS = BLOCKS.register("obsidian_bricks",
            () -> new Block(AbstractBlock.Properties.from(Blocks.OBSIDIAN).harvestTool(ToolType.PICKAXE).harvestLevel(3)));

    public static final RegistryObject<Block> CHISELED_OBSIDIAN_BRICKS = BLOCKS.register("chiseled_obsidian_bricks",
            () -> new Block(AbstractBlock.Properties.from(OBSIDIAN_BRICKS.get())));

    public static final RegistryObject<Block> OBSIDIAN_BRICK_SLAB = BLOCKS.register("obsidian_brick_slab",
            () -> new SlabBlock(AbstractBlock.Properties.from(OBSIDIAN_BRICKS.get())));

    public static final RegistryObject<Block> OBSIDIAN_BRICK_STAIRS = BLOCKS.register("obsidian_brick_stairs",
            () -> new StairsBlock(OBSIDIAN_BRICKS.get().getDefaultState(),AbstractBlock.Properties.from(OBSIDIAN_BRICKS.get())));

    public static final RegistryObject<Block> OBSIDIAN_BRICK_WALL = BLOCKS.register("obsidian_brick_wall",
            () -> new WallBlock(AbstractBlock.Properties.from(OBSIDIAN_BRICKS.get())));

    public static final RegistryObject<Block> PURPUR_WALL = BLOCKS.register("purpur_wall",
            () -> new WallBlock(AbstractBlock.Properties.from(Blocks.PURPUR_BLOCK)));

    public static final RegistryObject<Block> PURPUR_VOID_RUNE_TRAP_BLOCK = BLOCKS.register("purpur_void_rune_trap_block",
            () -> new PurpurVoidRuneTrapBlock(AbstractBlock.Properties.from(Blocks.PURPUR_BLOCK).tickRandomly().setLightLevel(getLightValueLit(7))));

    public static final RegistryObject<Block> END_STONE_TELEPORT_TRAP_BRICKS = BLOCKS.register("end_stone_teleport_trap_bricks",
            () -> new EndStoneTeleportTrapBricks(AbstractBlock.Properties.from(Blocks.END_STONE_BRICKS).tickRandomly().setLightLevel(getLightValueLit(7))));

    public static final RegistryObject<Block> OBSIDIAN_EXPLOSION_TRAP_BRICKS = BLOCKS.register("obsidian_explosion_trap_bricks",
            () -> new ObsidianExplosionTrapBricks(AbstractBlock.Properties.from(OBSIDIAN_BRICKS.get()).tickRandomly().setLightLevel(getLightValueLit(7))));

    public static final RegistryObject<Block> ENDER_GUARDIAN_SPAWNER = BLOCKS.register("ender_guardian_spawner",
            () -> new BlockEnderGuardianSpawner(AbstractBlock.Properties.create(Material.ROCK)
                    .hardnessAndResistance(-1.0F, 3600000.0F)
                    .noDrops()
                    .notSolid()
                    .sound(SoundType.METAL)));

    public static final RegistryObject<Block> CHORUS_PLANKS = BLOCKS.register("chorus_planks",
            () -> new Block(AbstractBlock.Properties.create(Material.NETHER_WOOD, MaterialColor.PURPLE).
                    hardnessAndResistance(2.0F, 3.0F)
                    .sound(SoundType.WOOD)));

    public static final RegistryObject<Block> CHORUS_SLAB = BLOCKS.register("chorus_slab",
            () -> new SlabBlock(AbstractBlock.Properties.from(CHORUS_PLANKS.get())));

    public static final RegistryObject<Block> CHORUS_STAIRS = BLOCKS.register("chorus_stairs",
            () -> new StairsBlock(CHORUS_PLANKS.get().getDefaultState(),AbstractBlock.Properties.from(CHORUS_PLANKS.get())));
    
    public static final RegistryObject<Block> CHORUS_FENCE = BLOCKS.register("chorus_fence",
            () -> new FenceBlock(AbstractBlock.Properties.from(CHORUS_PLANKS.get())));


    private static boolean isntSolid(BlockState state, IBlockReader reader, BlockPos pos) {
        return false;
    }

    private static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> {
            return state.get(BlockStateProperties.LIT) ? lightValue : 0;
        };
    }

}
