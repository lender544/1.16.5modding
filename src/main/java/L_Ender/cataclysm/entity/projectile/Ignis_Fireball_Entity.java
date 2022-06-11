package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.init.ModEffect;
import L_Ender.cataclysm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.WitherSkullEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Difficulty;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;


public class Ignis_Fireball_Entity extends DamagingProjectileEntity {

    private static final DataParameter<Boolean> SOUL = EntityDataManager.createKey(Ignis_Fireball_Entity.class, DataSerializers.BOOLEAN);

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


    protected float getMotionFactor() {
        return this.isSoul() ? 1.1F : 0.95F;
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
                if(this.isSoul()) {
                    flag = entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, livingentity), 8.0F);
                }else{
                    flag = entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, livingentity), 6.0F);
                }
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
                EffectInstance effectinstance1 = ((LivingEntity)entity).getActivePotionEffect(ModEffect.EFFECTBLAZING_BRAND.get());
                int i = 1;
                if (effectinstance1 != null) {
                    i += effectinstance1.getAmplifier();
                    ((LivingEntity)entity).removeActivePotionEffect(ModEffect.EFFECTBLAZING_BRAND.get());
                } else {
                    --i;
                }

                i = MathHelper.clamp(i, 0, 4);
                EffectInstance effectinstance = new EffectInstance(ModEffect.EFFECTBLAZING_BRAND.get(), 150, i, false, false, true);
                ((LivingEntity)entity).addPotionEffect(effectinstance);

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
        return false;
    }


    public boolean attackEntityFrom(DamageSource source, float amount) {
        return false;
    }

    protected void registerData() {
        this.dataManager.register(SOUL, false);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_soul", this.isSoul());
    }


    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.setSoul(compound.getBoolean("is_soul"));
    }


    public void setSoul(boolean IsSoul) {
        getDataManager().set(SOUL, IsSoul);
    }

    public boolean isSoul() {
        return getDataManager().get(SOUL);
    }

    protected boolean isFireballFiery() {
        return false;
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
