package L_Ender.cataclysm.init;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.config.CMConfig;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class ModConfiguredStructures {
    public static StructureFeature<?, ?> CONFIGURED_SOUL_BLACK_SMITH = ModStructures.SOUL_BLACK_SMITH.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);

    public static StructureFeature<?, ?> CONFIGURED_RUINED_CITADEL = ModStructures.RUINED_CITADEL.get().withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG);
    /**
     * Registers the configured structure which is what gets added to the biomes.
     * Noticed we are not using a forge registry because there is none for configured structures.
     * <p>
     * We can register configured structures at any time before a world is clicked on and made.
     * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
     */
    public static void registerConfiguredStructures() {
        Registry<StructureFeature<?, ?>> registry = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE;
        Registry.register(registry, new ResourceLocation(cataclysm.MODID, "configured_soul_black_smith"), CONFIGURED_SOUL_BLACK_SMITH);
        Registry.register(registry, new ResourceLocation(cataclysm.MODID, "configured_ruined_citadel"), CONFIGURED_RUINED_CITADEL);
        // Ok so, this part may be hard to grasp but basically, just add your structure to this to
        // prevent any sort of crash or issue with other mod's custom ChunkGenerators. If they use
        // FlatGenerationSettings.STRUCTURES in it and you don't add your structure to it, the game
        // could crash later when you attempt to add the StructureSeparationSettings to the dimension.
        //
        // (It would also crash with superflat worldtype if you omit the below line
        //  and attempt to add the structure's StructureSeparationSettings to the world)
        //
        // Note: If you want your structure to spawn in superflat, remove the FlatChunkGenerator check
        // in StructureTutorialMain.addDimensionalSpacing and then create a superflat world, exit it,
        // and re-enter it and your structures will be spawning. I could not figure out why it needs
        // the restart but honestly, superflat is really buggy and shouldn't be your main focus in my opinion.
        FlatGenerationSettings.STRUCTURES.put(ModStructures.SOUL_BLACK_SMITH.get(), CONFIGURED_SOUL_BLACK_SMITH);
        FlatGenerationSettings.STRUCTURES.put(ModStructures.RUINED_CITADEL.get(), CONFIGURED_RUINED_CITADEL);
    }

    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (event.getName() == null) {
            return;
        }
        ResourceLocation name = event.getName();
        RegistryKey<Biome> biome = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, name);
        if (CMConfig.SoulblacksmithSpawn && event.getCategory().equals(Biome.Category.NETHER)) {
            if (!(biome == Biomes.BASALT_DELTAS)) {
                event.getGeneration().getStructures().add(() -> CONFIGURED_SOUL_BLACK_SMITH);
            }
        }

        if (CMConfig.RuinedcitadelSpawn && event.getCategory().equals(Biome.Category.THEEND)) {
            event.getGeneration().getStructures().add(() -> CONFIGURED_RUINED_CITADEL);
        }
    }
}


