package L_Ender.cataclysm.entity.projectile;

import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class Void_Rune_Entity extends Entity {
    private int warmupDelayTicks;
    private boolean sentSpikeEvent;
    private int lifeTicks = 34;
    private boolean clientSideAttackStarted;
    private LivingEntity caster;
    private UUID casterUuid;
    private static final DataParameter<Boolean> ACTIVATE = EntityDataManager.createKey(Void_Rune_Entity.class, DataSerializers.BOOLEAN);
    public float activateProgress;
    public float prevactivateProgress;

    public Void_Rune_Entity(EntityType<? extends Void_Rune_Entity> p_i50170_1_, World p_i50170_2_) {
        super(p_i50170_1_, p_i50170_2_);
    }

    public Void_Rune_Entity(World worldIn, double x, double y, double z, float p_i47276_8_, int p_i47276_9_, LivingEntity casterIn) {
        this(ModEntities.VOID_RUNE.get(), worldIn);
        this.warmupDelayTicks = p_i47276_9_;
        this.setCaster(casterIn);
        this.rotationYaw = p_i47276_8_ * (180F / (float)Math.PI);
        this.setPosition(x, y, z);
    }

    protected void registerData() {
        this.dataManager.register(ACTIVATE, Boolean.valueOf(false));
    }

    public void setCaster(@Nullable LivingEntity p_190549_1_) {
        this.caster = p_190549_1_;
        this.casterUuid = p_190549_1_ == null ? null : p_190549_1_.getUniqueID();
    }

    @Nullable
    public LivingEntity getCaster() {
        if (this.caster == null && this.casterUuid != null && this.world instanceof ServerWorld) {
            Entity entity = ((ServerWorld)this.world).getEntityByUuid(this.casterUuid);
            if (entity instanceof LivingEntity) {
                this.caster = (LivingEntity)entity;
            }
        }

        return this.caster;
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    protected void readAdditional(CompoundNBT compound) {
        this.warmupDelayTicks = compound.getInt("Warmup");
        if (compound.hasUniqueId("Owner")) {
            this.casterUuid = compound.getUniqueId("Owner");
        }

    }

    protected void writeAdditional(CompoundNBT compound) {
        compound.putInt("Warmup", this.warmupDelayTicks);
        if (this.casterUuid != null) {
            compound.putUniqueId("Owner", this.casterUuid);
        }

    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        prevactivateProgress = activateProgress;

        if (isActivate() && this.activateProgress > 0F) {
            this.activateProgress--;
        }

        if (this.world.isRemote) {
            if (this.clientSideAttackStarted) {
                --this.lifeTicks;
                if (!isActivate() && this.activateProgress < 10F) {
                    this.activateProgress++;
                }
                if (this.lifeTicks == 33) {
                    for(int i = 0; i < 80; ++i) {
                        BlockState block = world.getBlockState(getPosition().down());
                        double d0 = this.getPosX() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
                        double d1 = this.getPosY() + 0.03D;
                        double d2 = this.getPosZ() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double) this.getWidth() * 0.5D;
                        double d3 = (this.rand.nextGaussian() * 0.07D);
                        double d4 = (this.rand.nextGaussian() * 0.07D);
                        double d5 = (this.rand.nextGaussian() * 0.07D);
                        this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), d0, d1, d2, d3, d4, d5);
                    }
                }

                if (this.lifeTicks == 14) {
                    this.setActivate(true);
                    for(int i = 0; i < 12; ++i) {
                        double d0 = this.getPosX() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.getWidth() * 0.5D;
                        double d1 = this.getPosY() + 0.05D + this.rand.nextDouble();
                        double d2 = this.getPosZ() + (this.rand.nextDouble() * 2.0D - 1.0D) * (double)this.getWidth() * 0.5D;
                        double d3 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        double d4 = 0.3D + this.rand.nextDouble() * 0.3D;
                        double d5 = (this.rand.nextDouble() * 2.0D - 1.0D) * 0.3D;
                        this.world.addParticle(ParticleTypes.REVERSE_PORTAL, d0, d1, d2, d3, d4, d5);
                    }
                }
            }
        } else if (--this.warmupDelayTicks < 0) {
            if (this.warmupDelayTicks == -10) {
                if(isActivate()) {
                    this.setActivate(false);
                }
            }
            if (this.warmupDelayTicks < -10 && this.warmupDelayTicks > -30) {
                for(LivingEntity livingentity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(0.2D, 0.0D, 0.2D))) {
                    this.damage(livingentity);
                }
            }


            if (!this.sentSpikeEvent) {
                this.world.setEntityState(this, (byte)4);
                this.sentSpikeEvent = true;
            }

            if (--this.lifeTicks < 0) {
                this.remove();
            }
        }

    }

    public boolean isActivate() {
        return this.dataManager.get(ACTIVATE);
    }

    public void setActivate(boolean Activate) {
        this.dataManager.set(ACTIVATE, Activate);
    }

    private void damage(LivingEntity Hitentity) {
        LivingEntity livingentity = this.getCaster();
        if (Hitentity.isAlive() && !Hitentity.isInvulnerable() && Hitentity != livingentity) {
            if (this.ticksExisted % 5 == 0) {
                if (livingentity == null) {
                    Hitentity.attackEntityFrom(DamageSource.MAGIC, CMConfig.Voidrunedamage);
                } else {
                    if (livingentity.isOnSameTeam(Hitentity)) {
                        return;
                    }
                    Hitentity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this, livingentity), CMConfig.Voidrunedamage);
                }
            }
        }
    }


    /**
     * Handler for {@link World#setEntityState}
     */
    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        super.handleStatusUpdate(id);
        if (id == 4) {
            this.clientSideAttackStarted = true;
            if (!this.isSilent()) {
                this.world.playSound(this.getPosX(), this.getPosY(), this.getPosZ(), ModSounds.VOID_RUNE_RISING.get(), this.getSoundCategory(), 0.5F, this.rand.nextFloat() * 0.2F + 0.85F, false);
            }
        }

    }

    public float getBrightness() {
        return 1.0F;
    }



    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
