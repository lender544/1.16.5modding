package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.FMLPlayMessages;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;


public class Void_Shard_Entity extends ProjectileItemEntity {

    private BlockState lastState;

    private Entity ignoreEntity = null;

    public Void_Shard_Entity(EntityType<? extends Void_Shard_Entity> type, World world) {
        super(type, world);
    }

    public Void_Shard_Entity(EntityType type,World worldIn, LivingEntity throwerIn) {
        super(type, throwerIn, worldIn);

    }

    public Void_Shard_Entity(World worldIn, LivingEntity throwerIn, double x, double y, double z, Vector3d movement, @Nullable Entity ignore) {
        super(ModEntities.VOID_SHARD.get(), x, y, z, worldIn);
        this.setShooter(throwerIn);
        this.setMotion(movement);
        this.ignoreEntity = ignore;
    }

    public Void_Shard_Entity(FMLPlayMessages.SpawnEntity spawnEntity, World world) {
        this(ModEntities.VOID_SHARD.get(), world);
    }



    @Override
    public void writeAdditional(CompoundNBT tag) {
        super.writeAdditional(tag);
        if (this.lastState != null) {
            tag.put("inBlockState", NBTUtil.writeBlockState(this.lastState));
        }
    }

    @Override
    public void readAdditional(CompoundNBT tag) {
        super.readAdditional(tag);
        if (tag.contains("inBlockState", 10)) {
            this.lastState = NBTUtil.readBlockState(tag.getCompound("inBlockState"));
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.VOID_SHARD.get();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void func_230299_a_(BlockRayTraceResult hit) {
        this.lastState = this.world.getBlockState(hit.getPos());
        super.func_230299_a_(hit);
        Vector3d vector3d = hit.getHitVec().subtract(this.getPosX(), this.getPosY(), this.getPosZ());
        this.setMotion(vector3d);
        Vector3d vector3d1 = vector3d.normalize().scale(getGravityVelocity());
        this.setRawPosition(this.getPosX() - vector3d1.x, this.getPosY() - vector3d1.y, this.getPosZ() - vector3d1.z);
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        Entity shooter = this.getShooter();
        Entity entity = result.getEntity();
        float i = 1.5f;
        if (shooter == null) {
            entity.attackEntityFrom(DamageSource.MAGIC, i);
            entity.hurtResistantTime = 0;
        }else {
            if (!((entity == shooter) ||(shooter.isOnSameTeam(entity)))) {
                entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, this.getShooter()), i);
                entity.hurtResistantTime = 0;
            }
        }

    }

    public void shootFromRotation(Entity p_234612_1_, float p_234612_2_, float p_234612_3_, float p_234612_4_, float p_234612_5_, float p_234612_6_) {
        float f = (float) (-Math.sin(p_234612_3_ * ((float) Math.PI / 180F)) * Math.cos(p_234612_2_ * ((float) Math.PI / 180F)));
        float f1 = (float) -Math.sin((p_234612_2_ + p_234612_4_) * ((float) Math.PI / 180F));
        float f2 = (float) (Math.cos(p_234612_3_ * ((float) Math.PI / 180F)) * Math.cos(p_234612_2_ * ((float) Math.PI / 180F)));
        this.shoot(f, f1, f2, p_234612_5_, p_234612_6_);
        Vector3d vector3d = p_234612_1_.getMotion();
        this.setMotion(this.getMotion().add(vector3d.x, p_234612_1_.isOnGround() ? 0.0D : vector3d.y, vector3d.z));
    }

    @Override
    protected boolean func_230298_a_(Entity entity) {
        if(entity == ignoreEntity) return false;
        return super.func_230298_a_(entity);
    }

    @Override
    public boolean hasNoGravity() {
        return false;
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            this.world.setEntityState(this, (byte)3);
            this.remove();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(new ItemParticleData(ParticleTypes.ITEM, new ItemStack(ModItems.VOID_SHARD.get())), this.getPosX(), this.getPosY(), this.getPosZ(), rand.nextGaussian() * 0.1D, rand.nextGaussian() * 0.1D, rand.nextGaussian() * 0.1D);
            }
        }
    }
}
