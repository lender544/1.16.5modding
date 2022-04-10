package L_Ender.cataclysm.init;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.tileentities.TileEntityEnderGuardianSpawner;
import L_Ender.cataclysm.tileentities.TileEntityObsidianExplosionTrapBricks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class ModTileentites {

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES,
            cataclysm.MODID);

    public static final RegistryObject<TileEntityType<TileEntityObsidianExplosionTrapBricks>> OBSIDIAN_EXPLOSION_TRAP_BRICKS = TILE_ENTITY_TYPES.register("obsidian_explosion_trap_bricks", () ->
            TileEntityType.Builder.create(TileEntityObsidianExplosionTrapBricks::new, ModBlocks.OBSIDIAN_EXPLOSION_TRAP_BRICKS.get()).build(null));

    public static final RegistryObject<TileEntityType<TileEntityEnderGuardianSpawner>> ENDER_GUARDIAN_SPAWNER = TILE_ENTITY_TYPES.register("ender_guardian_spawner", () ->
            TileEntityType.Builder.create(TileEntityEnderGuardianSpawner::new, ModBlocks.ENDER_GUARDIAN_SPAWNER.get()).build(null));

}
