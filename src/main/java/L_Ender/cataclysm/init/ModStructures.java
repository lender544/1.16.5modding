package L_Ender.cataclysm.init;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.structures.RuinedCitadelPieces;
import L_Ender.cataclysm.structures.RuinedCitadelStructure;
import L_Ender.cataclysm.structures.SoulBlackSmithPieces;
import L_Ender.cataclysm.structures.SoulBlackSmithStructure;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModStructures {
    public static final DeferredRegister<Structure<?>> STRUCTURE_FEATURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, cataclysm.MODID);

    public static final RegistryObject<Structure<NoFeatureConfig>> SOUL_BLACK_SMITH = registerStructure("soul_black_smith", () -> (new SoulBlackSmithStructure(NoFeatureConfig.CODEC)));

    public static final RegistryObject<Structure<NoFeatureConfig>> RUINED_CITADEL = registerStructure("ruined_citadel", () -> (new RuinedCitadelStructure(NoFeatureConfig.CODEC)));

    public static IStructurePieceType SBSP = IStructurePieceType.register(SoulBlackSmithPieces.Piece::new, cataclysm.MODID + "soul_black_smith");

    public static IStructurePieceType RCP = IStructurePieceType.register(RuinedCitadelPieces.Piece::new, cataclysm.MODID + "ruined_citadel");

    private static <T extends Structure<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structure) {
        return STRUCTURE_FEATURES.register(name, structure);
    }

    public static void setupStructures() {
        setupMapSpacingAndLand(
                SOUL_BLACK_SMITH.get(), /* The instance of the structure */
                new StructureSeparationSettings(112 /* maximum distance apart in chunks between spawn attempts */,
                        -1 /* minimum distance apart in chunks between spawn attempts */,
                        1234567890 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);

        setupMapSpacingAndLand(
                RUINED_CITADEL.get(), /* The instance of the structure */
                new StructureSeparationSettings(30 /* maximum distance apart in chunks between spawn attempts */,
                        -1 /* minimum distance apart in chunks between spawn attempts */,
                        1234567890 /* this modifies the seed of the structure so no two structures always spawn over each-other. Make this large and unique. */),
                false);
    }


    public static <F extends Structure<?>> void setupMapSpacingAndLand(
            F structure,
            StructureSeparationSettings structureSeparationSettings,
            boolean transformSurroundingLand) {

        Structure.NAME_STRUCTURE_BIMAP.put(structure.getRegistryName().toString(), structure);

        if (transformSurroundingLand) {
            Structure.field_236384_t_ =
                    ImmutableList.<Structure<?>>builder()
                            .addAll(Structure.field_236384_t_)
                            .add(structure)
                            .build();
        }

        DimensionStructuresSettings.field_236191_b_ =
                ImmutableMap.<Structure<?>, StructureSeparationSettings>builder()
                        .putAll(DimensionStructuresSettings.field_236191_b_)
                        .put(structure, structureSeparationSettings)
                        .build();
    }


    private static Method GETCODEC_METHOD;

    public static void addDimensionalSpacing(final WorldEvent.Load event) {
        if (event.getWorld() instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) event.getWorld();
            try {
                if (GETCODEC_METHOD == null)
                    GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
                ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
                if (cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
            } catch (Exception e) {
                cataclysm.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
            }

            // Prevent spawning our structure in Vanilla's superflat world as
            // people seem to want their superflat worlds free of modded structures.
            // Also that vanilla superflat is really tricky and buggy to work with in my experience.
            if (serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator &&
                    serverWorld.getDimensionKey().equals(World.OVERWORLD)) {
                return;
            }


            Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
            // putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
            // NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_SETTINGS in FMLCommonSetupEvent
            //       already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
            tempMap.putIfAbsent(ModStructures.SOUL_BLACK_SMITH.get(), DimensionStructuresSettings.field_236191_b_.get(ModStructures.SOUL_BLACK_SMITH.get()));
            tempMap.putIfAbsent(ModStructures.RUINED_CITADEL.get(), DimensionStructuresSettings.field_236191_b_.get(ModStructures.RUINED_CITADEL.get()));
            serverWorld.getChunkProvider().generator.func_235957_b_().field_236193_d_ = tempMap;
        }
    }
}
