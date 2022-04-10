package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.*;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Void_Scatter_Arrow_Entity extends ArrowEntity {

    public Void_Scatter_Arrow_Entity(EntityType type, World worldIn) {
        super(type, worldIn);
    }

    public Void_Scatter_Arrow_Entity(EntityType type, double x, double y, double z, World worldIn) {
        this(type, worldIn);
        this.setPosition(x, y, z);
    }

    public Void_Scatter_Arrow_Entity(World worldIn, LivingEntity shooter) {
        this(ModEntities.VOID_SCATTER_ARROW.get(), shooter.getPosX(), shooter.getPosYEye() - (double)0.1F, shooter.getPosZ(), worldIn);
        this.setShooter(shooter);
        if (shooter instanceof PlayerEntity) {
            this.pickupStatus = AbstractArrowEntity.PickupStatus.ALLOWED;
        }
    }

    public Void_Scatter_Arrow_Entity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(ModEntities.VOID_SCATTER_ARROW.get(), world);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected ItemStack getArrowStack() {
        return new ItemStack(Items.ARROW);
    }


    @Override
    protected void onImpact(RayTraceResult hit) {
        super.onImpact(hit);
        double x = this.getPosX();
        double y = this.getPosY();
        double z = this.getPosZ();
        if (this.world.isRemote){
            for (int l2 = 0; l2 < 8; ++l2) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ModItems.VOID_SCATTER_ARROW.get())), x, y, z, rand.nextGaussian() * 0.1D, rand.nextDouble() * 0.15D, rand.nextGaussian() * 0.1D);
            }
        }
        else {
            List<Vector3d> directions = getShootVectors(this.rand,0);
            for (Vector3d vec : directions) {
                Entity target = null;
                Direction dir = Direction.UP;
                if (hit.getType() == RayTraceResult.Type.ENTITY) {
                    target = ((EntityRayTraceResult) hit).getEntity();
                } else if (hit.getType() == RayTraceResult.Type.BLOCK) {
                    dir = ((BlockRayTraceResult)hit).getFace();
                }
                vec = vec.scale(0.35f);
                vec = this.rotateVector(vec,dir);
                Void_Shard_Entity shard = new Void_Shard_Entity(world, (LivingEntity) this.getShooter(),
                        x+vec.x,y+vec.y+0.25, vec.z+z, vec, target);
                world.addEntity(shard);
            }

            this.playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.1F, 0.8F);

        }
        this.remove();
    }

    public List<Vector3d> getShootVectors(Random random, float uncertainty){
        List<Vector3d> vectors = new ArrayList<>();
        float turnFraction = (1 + MathHelper.sqrt(5))/2;
        int numPoints = 17;
        double fullness = 0.8;
        for (int i = 1; i <= numPoints; i++){
            float dst = i / ((float)numPoints);
            //in degrees cause MathHelper sin are in deg
            float inclination = (random.nextFloat() - 0.5f) * uncertainty
                    + (float) (  Math.acos(1 - fullness * dst));
            float azimuth = (float) ((random.nextFloat() - 0.5f) * uncertainty
                    + (2f*Math.PI) * (random.nextFloat() + (turnFraction * i)));

            double x = Math.sin(inclination) * Math.cos(azimuth);
            double z = Math.sin(inclination) * Math.sin(azimuth);
            double y = Math.cos(inclination);

            Vector3d vec = new Vector3d(x, y, z);

            if(i==1){
                vec = vec.add(0, 1,0);
                vec = vec.scale(0.5);
            }

            vectors.add(vec);
        }
        return vectors;
    }

    private Vector3d rotateVector(Vector3d v, Direction dir){
        switch (dir){
            default:
            case UP:
                return v;
            case DOWN:
                return v.mul(0d,-1d,0d);
            case NORTH:
                return new Vector3d(v.z,v.x,-v.y);
            case SOUTH:
                return new Vector3d(v.z,v.x,v.y);
            case WEST:
                return new Vector3d(-v.y,v.z,v.x);
            case EAST:
                return new Vector3d(v.y,v.z,v.x);

        }
    }
}
