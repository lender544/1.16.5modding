package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;


public class Ignis_Fireball_Entity extends DamagingProjectileEntity {

    private static final DataParameter<Integer> VARIANT = EntityDataManager.createKey(Ignis_Fireball_Entity.class, DataSerializers.VARINT);

    public Ignis_Fireball_Entity(EntityType<? extends Ignis_Fireball_Entity> type, World world) {
        super(type, world);
    }

    public Ignis_Fireball_Entity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntities.IGNIS_FIREBALL.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    @OnlyIn(Dist.CLIENT)
    public Ignis_Fireball_Entity(World worldIn, double x, double y, double z, double accelX, double accelY, double accelZ) {
        super(ModEntities.IGNIS_FIREBALL.get(), x, y, z, accelX, accelY, accelZ, worldIn);
    }

    /**
     * Called when the fireball hits an entity
     */
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if (!this.world.isRemote) {
            Entity entity = result.getEntity();
            Entity shooter = this.getShooter();
            boolean flag;
            if (shooter instanceof LivingEntity) {
                LivingEntity livingentity = (LivingEntity)shooter;
                flag = entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, livingentity), 8.0F);
                if (flag) {
                    if (entity.isAlive()) {
                        this.applyEnchantments(livingentity, entity);
                    } else {
                        livingentity.heal(5.0F);
                    }
                }
            } else {
                flag = entity.attackEntityFrom(DamageSource.MAGIC, 5.0F);
            }

            if (flag && entity instanceof LivingEntity) {
                int i = 0;
                if (this.world.getDifficulty() == Difficulty.NORMAL) {
                    i = 10;
                } else if (this.world.getDifficulty() == Difficulty.HARD) {
                    i = 40;
                }

                if (i > 0) {
                    ((LivingEntity)entity).addPotionEffect(new EffectInstance(Effects.WITHER, 20 * i, 1));
                }
            }

        }
    }

    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if (!this.world.isRemote) {
            Explosion.Mode explosion$mode = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.world, this.getShooter()) ? Explosion.Mode.DESTROY : Explosion.Mode.NONE;
            this.world.createExplosion(this, this.getPosX(), this.getPosY(), this.getPosZ(), 1.0F, false, explosion$mode);
            this.remove();
        }

    }

    public boolean canBeCollidedWith() {
        return this.getVariant() == 2;
    }

    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (this.getVariant() == 2) {
            if (this.isInvulnerableTo(source)) {
                return false;
            } else {
                this.markVelocityChanged();
                Entity entity = source.getTrueSource();
                if (entity != null) {
                    Vector3d vector3d = entity.getLookVec();
                    this.setMotion(vector3d);
                    this.accelerationX = vector3d.x * 0.1D;
                    this.accelerationY = vector3d.y * 0.1D;
                    this.accelerationZ = vector3d.z * 0.1D;
                    this.setShooter(entity);
                    return true;
                } else {
                    return false;
                }
            }
        }
        return super.attackEntityFrom(source, amount);
    }

    protected void registerData() {
        this.dataManager.register(VARIANT, 0);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("Variant", this.getVariant());
    }


    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setVariant(compound.getInt("Variant"));
    }


    public void setVariant(int variant) {
        getDataManager().set(VARIANT, Integer.valueOf(variant));
    }

    public int getVariant() {
        return getDataManager().get(VARIANT).intValue();
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
