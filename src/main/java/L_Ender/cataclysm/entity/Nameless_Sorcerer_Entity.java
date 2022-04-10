package L_Ender.cataclysm.entity;

import L_Ender.cataclysm.init.ModEntities;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.EvokerFangsEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Nameless_Sorcerer_Entity extends AbstractIllagerEntity implements IAnimatedEntity {
    private static final DataParameter<Byte> SPELL = EntityDataManager.createKey(Nameless_Sorcerer_Entity.class, DataSerializers.BYTE);
    protected int spellTicks;
    private Nameless_Sorcerer_Entity.SpellType activeSpell = Nameless_Sorcerer_Entity.SpellType.NONE;
    private static final DataParameter<Boolean> IS_ILLUSION = EntityDataManager.createKey(Nameless_Sorcerer_Entity.class, DataSerializers.BOOLEAN);
    private int animationTick;
    private Animation currentAnimation;

    public Nameless_Sorcerer_Entity(EntityType entity, World world) {
        super(entity, world);
        this.experienceValue = 300;
    }

    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new Nameless_Sorcerer_Entity.CastingSpellGoal());
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, PlayerEntity.class, 8.0F, 0.6D, 1.0D));
        this.goalSelector.addGoal(5, new Nameless_Sorcerer_Entity.AttackSpellGoal());
        this.goalSelector.addGoal(6, new Nameless_Sorcerer_Entity.TeleportSpellGoal());
        this.goalSelector.addGoal(7, new Nameless_Sorcerer_Entity.IllusionSpellGoal());
        this.goalSelector.addGoal(8, new RandomWalkingGoal(this, 0.6D));
        this.goalSelector.addGoal(9, new LookAtGoal(this, PlayerEntity.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtGoal(this, MobEntity.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this, AbstractRaiderEntity.class)).setCallsForHelp());
        this.targetSelector.addGoal(2, (new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, (new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, false)).setUnseenMemoryTicks(300));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, false));
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION};
    }

    @Override
    public int getAnimationTick() {
        return animationTick;
    }


    @Override
    public void setAnimationTick(int tick) {
        animationTick = tick;
    }

    @Override
    public Animation getAnimation() {
        return currentAnimation;
    }


    @Override
    public void setAnimation(Animation animation) {
        currentAnimation = animation;
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MonsterEntity.func_234295_eP_().createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.5D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 24.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 50.0D);
    }

    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }

    public boolean canBeLeader() {
        return false;
    }

    protected void registerData() {
        super.registerData();
        this.dataManager.register(SPELL, (byte)0);
        this.dataManager.register(IS_ILLUSION, false);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.spellTicks = compound.getInt("SpellTicks");
        this.setIsIllusion(compound.getBoolean("is_Illusion"));
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putInt("SpellTicks", this.spellTicks);
        compound.putBoolean("is_Illusion", getIsIllusion());
    }

    public void setIsIllusion(boolean isIllusion) {
        getDataManager().set(IS_ILLUSION, isIllusion);
    }

    public boolean getIsIllusion() {
        return getDataManager().get(IS_ILLUSION);
    }


    @OnlyIn(Dist.CLIENT)
    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (this.isSpellcasting()) {
            return AbstractIllagerEntity.ArmPose.SPELLCASTING;
        } else {
            return this.getCelebrating() ? AbstractIllagerEntity.ArmPose.CELEBRATING : AbstractIllagerEntity.ArmPose.CROSSED;
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.getIsIllusion()) {
            this.playSound(SoundEvents.ENTITY_ILLUSIONER_CAST_SPELL, 1.0f ,0.9f);
            for(int i = 0; i < 20; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
            }
            this.remove();
            return false;
        }
        return super.attackEntityFrom(source, damage);
    }


    public boolean isSpellcasting() {
        if (this.world.isRemote) {
            return this.dataManager.get(SPELL) > 0;
        } else {
            return this.spellTicks > 0;
        }
    }

    public void setSpellType(Nameless_Sorcerer_Entity.SpellType spellType) {
        this.activeSpell = spellType;
        this.dataManager.set(SPELL, (byte)spellType.id);
    }

    protected Nameless_Sorcerer_Entity.SpellType getSpellType() {
        return !this.world.isRemote ? this.activeSpell : Nameless_Sorcerer_Entity.SpellType.getFromId(this.dataManager.get(SPELL));
    }

    protected void updateAITasks() {
        super.updateAITasks();
        if (this.spellTicks > 0) {
            --this.spellTicks;
        }

    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof LivingEntity && ((LivingEntity)entityIn).getCreatureAttribute() == CreatureAttribute.ILLAGER) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }


    protected SoundEvent getAmbientSound() {
        this.playSound(SoundEvents.ENTITY_EVOKER_AMBIENT, 1.0f ,0.9f);
        return null;
    }

    protected SoundEvent getDeathSound() {
        this.playSound(SoundEvents.ENTITY_EVOKER_DEATH, 1.0f ,0.9f);
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {

        this.playSound(SoundEvents.ENTITY_EVOKER_HURT, 1.0f ,0.9f);
        return null;
    }

    public void applyWaveBonus(int wave, boolean p_213660_2_) {
    }

    public SoundEvent getRaidLossSound() {
        return SoundEvents.ENTITY_EVOKER_CELEBRATE;
    }

    protected SoundEvent getSpellSound() {
        return SoundEvents.ENTITY_EVOKER_CAST_SPELL;
    }

    /**
     * Called to update the entity's position/logic.
     */
    public void tick() {
        super.tick();
        if (this.world.isRemote && this.isSpellcasting()) {
            Nameless_Sorcerer_Entity.SpellType Nameless_Sorcerer_Entity$spelltype = this.getSpellType();
            double d0 = getRNG().nextGaussian() * 0.07D;
            double d1 = getRNG().nextGaussian() * 0.07D;
            double d2 = getRNG().nextGaussian() * 0.07D;
            float f = this.renderYawOffset * ((float) Math.PI / 180F) + MathHelper.cos((float) this.ticksExisted * 0.6662F) * 0.25F;
            float f1 = MathHelper.cos(f);
            float f2 = MathHelper.sin(f);
            if (Nameless_Sorcerer_Entity$spelltype == SpellType.TELEPORTSPELL) {
                this.world.addParticle(ParticleTypes.PORTAL, this.getPosX() + (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double) f2 * 0.6D, d0, d1, d2);
                this.world.addParticle(ParticleTypes.PORTAL, this.getPosX() - (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() - (double) f2 * 0.6D, d0, d1, d2);
            }
            if (Nameless_Sorcerer_Entity$spelltype == SpellType.FANGS) {
                this.world.addParticle(ParticleTypes.CRIT, this.getPosX() + (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double) f2 * 0.6D, d0, d1, d2);
                this.world.addParticle(ParticleTypes.CRIT, this.getPosX() - (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() - (double) f2 * 0.6D, d0, d1, d2);
            }

            if (Nameless_Sorcerer_Entity$spelltype == SpellType.ILLUSION) {
                this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() + (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() + (double) f2 * 0.6D, d0, d1, d2);
                this.world.addParticle(ParticleTypes.SMOKE, this.getPosX() - (double) f1 * 0.6D, this.getPosY() + 1.8D, this.getPosZ() - (double) f2 * 0.6D, d0, d1, d2);
            }
        }

    }

    protected int getSpellTicks() {
        return this.spellTicks;
    }


    public class CastingASpellGoal extends Goal {
        public CastingASpellGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return Nameless_Sorcerer_Entity.this.getSpellTicks() > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            super.startExecuting();
            Nameless_Sorcerer_Entity.this.navigator.clearPath();
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            super.resetTask();
            Nameless_Sorcerer_Entity.this.setSpellType(Nameless_Sorcerer_Entity.SpellType.NONE);
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (Nameless_Sorcerer_Entity.this.getAttackTarget() != null) {
                Nameless_Sorcerer_Entity.this.getLookController().setLookPositionWithEntity(Nameless_Sorcerer_Entity.this.getAttackTarget(), (float)Nameless_Sorcerer_Entity.this.getHorizontalFaceSpeed(), (float)Nameless_Sorcerer_Entity.this.getVerticalFaceSpeed());
            }

        }
    }

    public enum SpellType {
        NONE(0),
        TELEPORTSPELL(1),
        FANGS(2),
        WOLOLO(3),
        ILLUSION(4);

        private final int id;


        SpellType(int idIn) {
            this.id = idIn;

        }

        public static SpellType getFromId(int idIn) {
            for(Nameless_Sorcerer_Entity.SpellType Nameless_Sorcerer_Entity$spelltype : values()) {
                if (idIn == Nameless_Sorcerer_Entity$spelltype.id) {
                    return Nameless_Sorcerer_Entity$spelltype;
                }
            }

            return NONE;
        }
    }

    public abstract class UseSpellGoal extends Goal {
        protected int spellWarmup;
        protected int spellCooldown;

        protected UseSpellGoal() {
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            LivingEntity livingentity = Nameless_Sorcerer_Entity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (Nameless_Sorcerer_Entity.this.isSpellcasting()) {
                    return false;
                } else {
                    return Nameless_Sorcerer_Entity.this.ticksExisted >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            LivingEntity livingentity = Nameless_Sorcerer_Entity.this.getAttackTarget();
            return livingentity != null && livingentity.isAlive() && this.spellWarmup > 0;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.spellWarmup = this.getCastWarmupTime();
            Nameless_Sorcerer_Entity.this.spellTicks = this.getCastingTime();
            this.spellCooldown = Nameless_Sorcerer_Entity.this.ticksExisted + this.getCastingInterval();
            SoundEvent soundevent = this.getSpellPrepareSound();
            if (soundevent != null) {
                Nameless_Sorcerer_Entity.this.playSound(soundevent, 1.0F, 1.0F);
            }

            Nameless_Sorcerer_Entity.this.setSpellType(this.getSpellType());
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            --this.spellWarmup;
            if (this.spellWarmup == 0) {
                this.castSpell();
                Nameless_Sorcerer_Entity.this.playSound(Nameless_Sorcerer_Entity.this.getSpellSound(), 1.0F, 1.0F);
            }

        }

        protected abstract void castSpell();

        protected int getCastWarmupTime() {
            return 20;
        }

        protected abstract int getCastingTime();

        protected abstract int getCastingInterval();

        @Nullable
        protected abstract SoundEvent getSpellPrepareSound();

        protected abstract Nameless_Sorcerer_Entity.SpellType getSpellType();
    }

    class AttackSpellGoal extends Nameless_Sorcerer_Entity.UseSpellGoal {
        private AttackSpellGoal() {
        }

        protected int getCastingTime() {
            return 20;
        }

        protected int getCastingInterval() {
            return 45;
        }

        protected void castSpell() {
            LivingEntity target = Nameless_Sorcerer_Entity.this.getAttackTarget();
            double d0 = Math.min(target.getPosY(), Nameless_Sorcerer_Entity.this.getPosY());
            double d1 = Math.max(target.getPosY(), Nameless_Sorcerer_Entity.this.getPosY()) + 1.0D;
            float f = (float) MathHelper.atan2(target.getPosZ() - Nameless_Sorcerer_Entity.this.getPosZ(), target.getPosX() - Nameless_Sorcerer_Entity.this.getPosX());
            if (Nameless_Sorcerer_Entity.this.getDistanceSq(target) < 12.0D) {
                for(int i = 0; i < 5; ++i) {
                    float f1 = f + (float)i * (float)Math.PI * 0.4F;
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double)MathHelper.cos(f1) * 1.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 0);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double)MathHelper.cos(f1) * 1.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double)MathHelper.sin(f1) * 1.5D, d0, d1, f1, 40);
                }

                for(int k = 0; k < 8; ++k) {
                    float f2 = f + (float)k * (float)Math.PI * 2.0F / 8.0F + ((float) Math.PI * 2F / 5F);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double)MathHelper.cos(f2) * 2.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double)MathHelper.cos(f2) * 2.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double)MathHelper.sin(f2) * 2.5D, d0, d1, f2, 37);
                }
                for (int k = 0; k < 13; ++k) {
                    float f3 = f + (float) k * (float) Math.PI * 2.0F / 13.0F + ((float) Math.PI * 2F / 10F);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double) MathHelper.cos(f3) * 3.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double) MathHelper.sin(f3) * 3.5D, d0, d1, f3, 10);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double) MathHelper.cos(f3) * 3.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double) MathHelper.sin(f3) * 3.5D, d0, d1, f3, 30);
                }
                for (int k = 0; k < 16; ++k) {
                    float f4 = f + (float) k * (float) Math.PI * 2.0F / 16.0F + ((float) Math.PI * 2F / 20F);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double) MathHelper.cos(f4) * 4.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double) MathHelper.sin(f4) * 4.5D, d0, d1, f4, 15);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double) MathHelper.cos(f4) * 4.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double) MathHelper.sin(f4) * 4.5D, d0, d1, f4, 25);
                }
                for (int k = 0; k < 19; ++k) {
                    float f5 = f + (float) k * (float) Math.PI * 2.0F / 19.0F + ((float) Math.PI * 2F / 40F);
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double) MathHelper.cos(f5) * 5.5D, Nameless_Sorcerer_Entity.this.getPosZ() + (double) MathHelper.sin(f5) * 5.5D, d0, d1, f5, 20);
                }

            } else {
                for(int l = 0; l < 16; ++l) {
                    double d2 = 1.25D * (double)(l + 1);
                    int j = 1 * l;
                    this.spawnFangs(Nameless_Sorcerer_Entity.this.getPosX() + (double)MathHelper.cos(f) * d2, Nameless_Sorcerer_Entity.this.getPosZ() + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                }
            }
        }

        private void spawnFangs(double p_190876_1_, double p_190876_3_, double p_190876_5_, double p_190876_7_, float p_190876_9_, int p_190876_10_) {
            BlockPos blockpos = new BlockPos(p_190876_1_, p_190876_7_, p_190876_3_);
            boolean flag = false;
            double d0 = 0.0D;

            do {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate = Nameless_Sorcerer_Entity.this.world.getBlockState(blockpos1);
                if (blockstate.isSolidSide(Nameless_Sorcerer_Entity.this.world, blockpos1, Direction.UP)) {
                    if (!Nameless_Sorcerer_Entity.this.world.isAirBlock(blockpos)) {
                        BlockState blockstate1 = Nameless_Sorcerer_Entity.this.world.getBlockState(blockpos);
                        VoxelShape voxelshape = blockstate1.getCollisionShapeUncached(Nameless_Sorcerer_Entity.this.world, blockpos);
                        if (!voxelshape.isEmpty()) {
                            d0 = voxelshape.getEnd(Direction.Axis.Y);
                        }
                    }

                    flag = true;
                    break;
                }

                blockpos = blockpos.down();
            } while(blockpos.getY() >= MathHelper.floor(p_190876_5_) - 1);

            if (flag) {
                Nameless_Sorcerer_Entity.this.world.addEntity(new EvokerFangsEntity(Nameless_Sorcerer_Entity.this.world, p_190876_1_, (double)blockpos.getY() + d0, p_190876_3_, p_190876_9_, p_190876_10_, Nameless_Sorcerer_Entity.this));
            }

        }

        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_ATTACK;
        }

        protected Nameless_Sorcerer_Entity.SpellType getSpellType() {
            return Nameless_Sorcerer_Entity.SpellType.FANGS;
        }
    }

    class CastingSpellGoal extends Nameless_Sorcerer_Entity.CastingASpellGoal {
        private CastingSpellGoal() {
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void tick() {
            if (Nameless_Sorcerer_Entity.this.getAttackTarget() != null) {
                Nameless_Sorcerer_Entity.this.getLookController().setLookPositionWithEntity(Nameless_Sorcerer_Entity.this.getAttackTarget(), (float) Nameless_Sorcerer_Entity.this.getHorizontalFaceSpeed(), (float) Nameless_Sorcerer_Entity.this.getVerticalFaceSpeed());
            }
        }
    }

    class TeleportSpellGoal extends Nameless_Sorcerer_Entity.UseSpellGoal {

        private TeleportSpellGoal()
        {
            super();
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = Nameless_Sorcerer_Entity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (Nameless_Sorcerer_Entity.this.getIsIllusion()) {
                    return false;
                }
                if (Nameless_Sorcerer_Entity.this.getDistance(livingentity) < 6F){
                    return false;
                }
                if (Nameless_Sorcerer_Entity.this.isSpellcasting()) {
                    return false;
                } else {
                    return Nameless_Sorcerer_Entity.this.ticksExisted >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        @Override
        protected int getCastingTime()
        {
            return 60;
        }

        @Override
        protected int getCastingInterval()
        {
            return 300;
        }

        @Override
        protected void castSpell() {

            LivingEntity target = Nameless_Sorcerer_Entity.this.getAttackTarget();

            this.teleportEntity(target);


        }

        public void teleportEntity(LivingEntity target) {
            if (target.isPassenger()) {
                target.stopRiding();
            }

            double d0 = target.getPosX();
            double d1 = target.getPosY();
            double d2 = target.getPosZ();


            double d3 = Nameless_Sorcerer_Entity.this.getPosX();
            double d4 = Nameless_Sorcerer_Entity.this.getPosY();
            double d5 = Nameless_Sorcerer_Entity.this.getPosZ();
            target.setPositionAndUpdate(d3, d4, d5);
            target.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
            target.rotationYaw = Nameless_Sorcerer_Entity.this.rotationYaw;
            target.rotationPitch = Nameless_Sorcerer_Entity.this.rotationPitch;

            Nameless_Sorcerer_Entity.this.setPositionAndUpdate(d0, d1, d2);
            Nameless_Sorcerer_Entity.this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            Nameless_Sorcerer_Entity.this.rotationYaw = target.rotationYaw;
            Nameless_Sorcerer_Entity.this.rotationPitch = target.rotationPitch;
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_ILLUSIONER_PREPARE_MIRROR;
        }

        @Override
        protected Nameless_Sorcerer_Entity.SpellType getSpellType()
        {
            return SpellType.TELEPORTSPELL;
        }
    }

    class IllusionSpellGoal extends Nameless_Sorcerer_Entity.UseSpellGoal {

        private IllusionSpellGoal() {
            super();
        }

        public boolean shouldExecute() {
            LivingEntity livingentity = Nameless_Sorcerer_Entity.this.getAttackTarget();
            if (livingentity != null && livingentity.isAlive()) {
                if (Nameless_Sorcerer_Entity.this.getIsIllusion()) {
                    return false;
                }
                if (Nameless_Sorcerer_Entity.this.isSpellcasting()) {
                    return false;
                } else {
                    return Nameless_Sorcerer_Entity.this.ticksExisted >= this.spellCooldown;
                }
            } else {
                return false;
            }
        }

        @Override
        protected int getCastingTime()
        {
            return 80;
        }

        @Override
        protected int getCastingInterval()
        {
            return 300;
        }

        @Override
        protected void castSpell() {
            ServerWorld serverworld = (ServerWorld)Nameless_Sorcerer_Entity.this.world;

            for (int i = 0; i < 2; ++i) {
                LivingEntity target = Nameless_Sorcerer_Entity.this.getAttackTarget();
                BlockPos blockpos = Nameless_Sorcerer_Entity.this.getPosition().add(-2 + Nameless_Sorcerer_Entity.this.rand.nextInt(5), 0, -2 + Nameless_Sorcerer_Entity.this.rand.nextInt(5));
                Nameless_Sorcerer_Entity illusion = ModEntities.NAMELESS_SORCERER.get().create(Nameless_Sorcerer_Entity.this.world);
                illusion.moveToBlockPosAndAngles(blockpos, 0.0F, 0.0F);
                if(target != null ) {
                    illusion.setAttackTarget(target);
                }
                illusion.onInitialSpawn(serverworld, Nameless_Sorcerer_Entity.this.world.getDifficultyForLocation(blockpos), SpawnReason.MOB_SUMMONED, (ILivingEntityData)null, (CompoundNBT)null);
                illusion.setIsIllusion(true);
                serverworld.func_242417_l(illusion);

            }
        }


        protected SoundEvent getSpellPrepareSound() {
            return SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO;
        }

        @Override
        protected Nameless_Sorcerer_Entity.SpellType getSpellType()
        {
            return SpellType.ILLUSION;
        }
    }


}

