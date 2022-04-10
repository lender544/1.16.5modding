package L_Ender.cataclysm.tileentities;

import L_Ender.cataclysm.blocks.ObsidianExplosionTrapBricks;
import L_Ender.cataclysm.init.ModTag;
import L_Ender.cataclysm.init.ModTileentites;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;

public class TileEntityObsidianExplosionTrapBricks extends TileEntity implements ITickableTileEntity {

    public int ticksExisted;


    public TileEntityObsidianExplosionTrapBricks() {
        super(ModTileentites.OBSIDIAN_EXPLOSION_TRAP_BRICKS.get());
    }

    @Override
    public void tick() {
        boolean LIT = false;
        if(getBlockState().getBlock() instanceof ObsidianExplosionTrapBricks){
            LIT = getBlockState().get(ObsidianExplosionTrapBricks.LIT);
        }
        if(LIT){
            ticksExisted++;
            float x = this.getPos().getX();
            float y = this.getPos().getY();
            float z = this.getPos().getZ();
            float f = 5F;
            if (ticksExisted < 80) {
                for (LivingEntity inRange : world.getEntitiesWithinAABB(LivingEntity.class, new AxisAlignedBB((double) x - f, (double) y - f, (double) z - f, (double) x + f, (double) y + f, (double) z + f))) {
                    if (inRange instanceof PlayerEntity && ((PlayerEntity) inRange).abilities.disableDamage) continue;
                    if (EntityTypeTags.getCollection().get(ModTag.TRAP_BLOCK_NOT_DETECTED).contains(inRange.getType())) continue;
                    Vector3d diff = inRange.getPositionVec().subtract(Vector3d.copyCentered(getPos().add(0, 0, 0)));
                    diff = diff.normalize().scale(0.06);
                    inRange.setMotion(inRange.getMotion().subtract(diff));
                }
                if (world.isRemote) {
                    for (int i = 0; i < 3; ++i) {
                        int j = world.rand.nextInt(2) * 2 - 1;
                        int k = world.rand.nextInt(2) * 2 - 1;
                        double d0 = (double) pos.getX() + 0.5D + 0.25D * (double) j;
                        double d1 = (float) pos.getY() + world.rand.nextFloat();
                        double d2 = (double) pos.getZ() + 0.5D + 0.25D * (double) k;
                        double d3 = world.rand.nextFloat() * (float) j;
                        double d4 = ((double) world.rand.nextFloat() - 0.5D) * 0.125D;
                        double d5 = world.rand.nextFloat() * (float) k;
                        world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
                    }
                }
            }
            if(ticksExisted == 80){
                world.createExplosion(null, x, y+1, z, 3.0F, Explosion.Mode.NONE);
            }

        }else{
            ticksExisted=0;
        }
    }
}


