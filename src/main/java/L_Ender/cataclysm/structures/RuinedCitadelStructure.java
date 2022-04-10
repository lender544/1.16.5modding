package L_Ender.cataclysm.structures;

import L_Ender.cataclysm.init.ModEntities;
import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.gen.settings.StructureSeparationSettings;

import java.util.List;

public class RuinedCitadelStructure extends Structure<NoFeatureConfig> {
    public RuinedCitadelStructure(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    /**
     * This is how the worldgen code knows what to call when it
     * is time to create the pieces of the structure for generation.
     */
    @Override
    public IStartFactory<NoFeatureConfig> getStartFactory() {
        return RuinedCitadelStructure.Start::new;
    }


    /**
     * Generation stage for when to generate the structure. there are 10 stages you can pick from!
     * This surface structure stage places the structure before plants and ores are generated.
     */
    @Override
    public GenerationStage.Decoration getDecorationStage() {
        return GenerationStage.Decoration.SURFACE_STRUCTURES;
    }


    private static final List<MobSpawnInfo.Spawners> STRUCTURE_MONSTERS = ImmutableList.of(
            new MobSpawnInfo.Spawners(ModEntities.ENDERMAPTERA.get(), 3, 1, 1)
    );
    @Override
    public List<MobSpawnInfo.Spawners> getDefaultSpawnList() {
        return STRUCTURE_MONSTERS;
    }


    @Override
    protected boolean func_230363_a_(ChunkGenerator generator, BiomeProvider provider, long seed, SharedSeedRandom random, int x, int z, Biome biome, ChunkPos chunkPos, NoFeatureConfig config)
    {
        int i = x >> 4;
        int j = z >> 4;
        random.setSeed((long) (i ^ j << 4) ^ seed);
        random.nextInt();
        return !this.isEndCityWithin(generator, seed, random, x, z, 10);
    }

    private boolean isEndCityWithin(ChunkGenerator generator, long seed, SharedSeedRandom random, int x, int z, int radius)
    {
        StructureSeparationSettings structureseparationsettings = generator.func_235957_b_().func_236197_a_(Structure.END_CITY);
        if(structureseparationsettings != null)
        {
            for(int surroundingX = x - radius; surroundingX <= x + radius; ++surroundingX)
            {
                for(int surroundingZ = z - radius; surroundingZ <= z + radius; ++surroundingZ)
                {
                    ChunkPos chunkpos = Structure.END_CITY.getChunkPosForStructure(structureseparationsettings, seed, random, surroundingX, surroundingZ);
                    if(surroundingX == chunkpos.x && surroundingZ == chunkpos.z)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static class Start extends StructureStart<NoFeatureConfig> {
        public Start(Structure<NoFeatureConfig> structureIn, int chunkX, int chunkZ, MutableBoundingBox mutableBoundingBox, int referenceIn, long seedIn) {
            super(structureIn, chunkX, chunkZ, mutableBoundingBox, referenceIn, seedIn);
        }

        @Override
        public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn, NoFeatureConfig config) {
            Rotation rotation = Rotation.values()[this.rand.nextInt(Rotation.values().length)];

            int x = (chunkX << 4) + 7;
            int z = (chunkZ << 4) + 7;

            int surfaceY = generator.getNoiseHeightMinusOne(x, z, Heightmap.Type.WORLD_SURFACE_WG);
            if (surfaceY < 48) {
                return;
            }
            BlockPos blockpos = new BlockPos(x, surfaceY + 5, z);


            RuinedCitadelPieces.start(templateManagerIn, blockpos, rotation, this.components, this.rand);

            this.recalculateStructureSize();

        }
    }
}