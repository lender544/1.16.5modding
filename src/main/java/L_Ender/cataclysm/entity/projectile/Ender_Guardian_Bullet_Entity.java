package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.init.ModEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.DamagingProjectileEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nonnull;

public class Ender_Guardian_Bullet_Entity extends DamagingProjectileEntity {
    //Projectile goes to a point over a set duration, then activates and accelerates in a given straight line
    private double dirX, dirY, dirZ;
    private double startX, startY, startZ;
    private int timer;
    private boolean fired;

    public Ender_Guardian_Bullet_Entity(EntityType<? extends Ender_Guardian_Bullet_Entity> type, World world) {
        super(type, world);
    }

    public Ender_Guardian_Bullet_Entity(World worldIn, LivingEntity shooter, double accelX, double accelY, double accelZ) {
        super(ModEntities.ENDER_GUARDIAN_BULLET.get(), shooter, accelX, accelY, accelZ, worldIn);
    }

    @Override
    protected void registerData() {
        super.registerData();
    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        if (!world.isRemote && fired) {
            Entity entity = result.getEntity();
            Entity Shooter = this.getShooter();
            LivingEntity livingentity = Shooter instanceof LivingEntity ? (LivingEntity)Shooter : null;
            boolean flag = entity.attackEntityFrom(DamageSource.causeIndirectDamage(this, livingentity).setProjectile(), 6.0F);
            if (flag) {
                this.applyEnchantments(livingentity, entity);
                if (entity instanceof LivingEntity) {
                    ((LivingEntity) entity).addPotionEffect(new EffectInstance(Effects.LEVITATION, 100));
                }
            }
        }
    }

    protected void func_230299_a_(BlockRayTraceResult result) {
        super.func_230299_a_(result);
        if(fired) {
            ((ServerWorld) this.world).spawnParticle(ParticleTypes.EXPLOSION, this.getPosX(), this.getPosY(), this.getPosZ(), 2, 0.2D, 0.2D, 0.2D, 0.0D);
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HIT, 1.0F, 1.0F);
        }
    }


    protected void onImpact(RayTraceResult result) {
        super.onImpact(result);
        if(fired) {
            this.remove();
        }
    }

    public void setUp(int delay, double dirX, double dirY, double dirZ, double startX, double startY, double startZ) {
        fired = false;
        timer = delay;
        this.dirX = dirX;
        this.dirY = dirY;
        this.dirZ = dirZ;
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
    }

    public void setUpTowards(int delay, double startX, double startY, double startZ, double endX, double endY, double endZ, double speed) {
        Vector3d vec = new Vector3d(endX - startX, endY - startY, endZ - startZ).normalize().scale(speed);
        setUp(delay, vec.x, vec.y, vec.z, startX, startY, startZ);
    }

    public void tick() {
        if (!this.world.isRemote) {
            timer--;
            if (timer <= 0) {
                if (fired) remove();
                else {
                    fired = true;
                    setMotion(new Vector3d(0, 0, 0));
                    timer = 30;
                }
            }
            Vector3d motion = getMotion();
            double d0 = getPosX();
            double d1 = getPosY();
            double d2 = getPosZ();

            if (fired) {
                if (motion.lengthSquared() <= 16) setMotion(motion.add(dirX * 0.1, dirY * 0.1, dirZ * 0.1));
            } else {
                setMotion(new Vector3d(startX - d0, startY - d1, startZ - d2).scale(1.0 / timer));
            }
        }

        // Started from copy of the above tick
        Entity shooter = this.getShooter();
        if (world.isRemote || (shooter == null || !shooter.removed) && world.isBlockLoaded(getPosition())) {
            RayTraceResult raytraceresult = ProjectileHelper.func_234618_a_(this, this::func_230298_a_);
            if (raytraceresult.getType() != RayTraceResult.Type.MISS && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult)) {
                onImpact(raytraceresult);
            }

            doBlockCollisions();
            Vector3d vector3d = getMotion();
            double d0 = getPosX() + vector3d.x;
            double d1 = getPosY() + vector3d.y;
            double d2 = getPosZ() + vector3d.z;
            ProjectileHelper.rotateTowardsMovement(this, 0.2F);
            this.world.addParticle(ParticleTypes.END_ROD, this.getPosX() - vector3d.x, this.getPosY() - vector3d.y + 0.15D, this.getPosZ() - vector3d.z, 0.0D, 0.0D, 0.0D);
            setPosition(d0, d1, d2);
        } else {
            remove();
        }
    }

    @Override
    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putDouble("DX", dirX);
        compound.putDouble("DY", dirY);
        compound.putDouble("DZ", dirZ);
        compound.putDouble("SX", startX);
        compound.putDouble("SY", startY);
        compound.putDouble("SZ", startZ);
        compound.putInt("Timer", timer);
        compound.putBoolean("Fired", fired);
    }

    @Override
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        dirX = compound.getDouble("DX");
        dirY = compound.getDouble("DY");
        dirZ = compound.getDouble("DZ");
        startX = compound.getDouble("SX");
        startY = compound.getDouble("SY");
        startZ = compound.getDouble("SZ");
        timer = compound.getInt("Timer");
        fired = compound.getBoolean("Fired");
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if(!this.world.isRemote && fired)
        {
            this.playSound(SoundEvents.ENTITY_SHULKER_BULLET_HURT, 1.0F, 1.0F);
            ((ServerWorld)this.world).spawnParticle(ParticleTypes.CRIT, this.getPosX(), this.getPosY(), this.getPosZ(), 15, 0.2D, 0.2D, 0.2D, 0.0D);
            this.remove();
        }

        return true;
    }


    @Nonnull
    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

}
