package L_Ender.cataclysm.tileentities;

import L_Ender.cataclysm.entity.Ender_Guardian_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModTileentites;

public class TileEntityEnderGuardianSpawner extends TileEntityBossSpawner<Ender_Guardian_Entity> {

	public TileEntityEnderGuardianSpawner() {
		super(ModTileentites.ENDER_GUARDIAN_SPAWNER.get(), ModEntities.ENDER_GUARDIAN.get());
	}

	@Override
	protected int getRange() {
		return SHORT_RANGE;
	}
}
