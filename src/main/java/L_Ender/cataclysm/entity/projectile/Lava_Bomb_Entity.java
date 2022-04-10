package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.config.CMConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ThrowableEntity;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class Lava_Bomb_Entity extends ThrowableEntity {


    private boolean hasHit;
    public double prevMotionX, prevMotionY, prevMotionZ;

    public Lava_Bomb_Entity(EntityType type, World world) {
        super(type, world);
    }

    public Lava_Bomb_Entity(EntityType type, World world, LivingEntity thrower) {
        super(type, thrower, world);
    }


    @Override
    protected void registerData() {

    }

    @Override
    protected void onImpact(RayTraceResult ray) {
        this.setMotion(0.0D, 0.0D, 0.0D);
        this.hasHit = true;
        if (!world.isRemote) {
            this.doTerrainEffects();
        }
    }

    private void doTerrainEffects() {

        final int range = 0;

        int ix = MathHelper.floor(this.lastTickPosX);
        int iy = MathHelper.floor(this.lastTickPosY);
        int iz = MathHelper.floor(this.lastTickPosZ);

        for (int x = -range; x <= range; x++) {
            for (int y = -range; y <= range; y++) {
                for (int z = -range; z <= range; z++) {
                    BlockPos pos = new BlockPos(ix + x, iy + y, iz + z);
                    this.doTerrainEffect(pos);
                }
            }
        }
    }

    private void doTerrainEffect(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getMaterial() == Material.WATER) {
            this.world.setBlockState(pos, Blocks.STONE.getDefaultState());
        }
        if (this.world.isAirBlock(pos) && Blocks.LAVA.getDefaultState().isValidPosition(this.world, pos)) {
            this.world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        }
    }

    @Override
    public void tick() {
        super.tick();
        prevMotionX = getMotion().x;
        prevMotionY = getMotion().y;
        prevMotionZ = getMotion().z;

        rotationYaw = -((float) MathHelper.atan2(getMotion().x, getMotion().z)) * (180F / (float)Math.PI);

        if (this.hasHit) {
            this.getMotion().mul(0.1D, 0.1D, 0.1D);


            if (!world.isRemote) {
                this.playSound(SoundEvents.ENTITY_GENERIC_BURN, 1.5f, 0.75f);
                this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), CMConfig.Lavabombradius, Explosion.Mode.NONE);
                remove();
            }
        } else {
            makeTrail();
        }
    }

    public void makeTrail() {
        for (int i = 0; i < 5; i++) {
            double dx = getPosX() + 1.5F * (rand.nextFloat() - 0.5F);
            double dy = getPosY() + 1.5F * (rand.nextFloat() - 0.5F);
            double dz = getPosZ() + 1.5F * (rand.nextFloat() - 0.5F);

            world.addParticle(ParticleTypes.FLAME, dx, dy, dz, -getMotion().getX(), -getMotion().getY(), -getMotion().getZ());
        }
    }

    public float getBrightness() {
        return 1.0F;
    }

    @Override
    protected float getGravityVelocity() {
        return this.hasHit ? 0F : 0.025F;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}