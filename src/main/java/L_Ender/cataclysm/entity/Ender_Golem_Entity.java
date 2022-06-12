package L_Ender.cataclysm.entity;

import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.AI.CmAttackGoal;
import L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import L_Ender.cataclysm.entity.etc.CMPathNavigateGround;
import L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import L_Ender.cataclysm.entity.projectile.Void_Rune_Entity;
import L_Ender.cataclysm.init.ModSounds;
import L_Ender.cataclysm.init.ModTag;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class Ender_Golem_Entity extends Boss_monster {

    public static final Animation ANIMATION_ATTACK1 = Animation.create(25);
    public static final Animation ANIMATION_ATTACK2 = Animation.create(25);
    public static final Animation ANIMATION_EARTHQUAKE = Animation.create(35);
    public static final Animation VOID_RUNE_ATTACK = Animation.create(83);
    public static final Animation ENDER_GOLEM_DEATH = Animation.create(95);
    public static final int VOID_RUNE_ATTACK_COOLDOWN = 250;
    private static final DataParameter<Boolean> IS_AWAKEN = EntityDataManager.createKey(Ender_Golem_Entity.class, DataSerializers.BOOLEAN);
    private int void_rune_attack_cooldown = 0;
    private int timeWithoutTarget;
    public float deactivateProgress;
    public float prevdeactivateProgress;
    public boolean Breaking = CMConfig.EndergolemBlockBreaking;

    public Ender_Golem_Entity(EntityType entity, World world) {
        super(entity, world);
        this.experienceValue = 15;
        this.stepHeight = 1.5F;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        setConfigattribute(this, CMConfig.EnderGolemHealthMultiplier, CMConfig.EnderGolemDamageMultiplier);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, ANIMATION_ATTACK1, ANIMATION_ATTACK2, ANIMATION_EARTHQUAKE,VOID_RUNE_ATTACK,ENDER_GOLEM_DEATH};
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new CmAttackGoal(this,1.0));
        this.goalSelector.addGoal(1, new AttackGoal());
        this.goalSelector.addGoal(0, new AwakenGoal());
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));

    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MonsterEntity.func_233666_p_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.28F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 10)
                .createMutableAttribute(Attributes.MAX_HEALTH, 150)
                .createMutableAttribute(Attributes.ARMOR, 12)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }


    protected int decreaseAirSupply(int air) {
        return air;
    }


    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    private static Animation getRandomAttack(Random rand) {
        switch (rand.nextInt(3)) {
            case 0:
                return ANIMATION_ATTACK1;
            case 1:
                return ANIMATION_ATTACK2;
            case 2:
                return ANIMATION_EARTHQUAKE;
        }
        return ANIMATION_EARTHQUAKE;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.getAnimation() == VOID_RUNE_ATTACK
                || !this.getIsAwaken()) {
            if(!source.canHarmInCreative()|| !source.isMagicDamage()) {
                damage *= 0.5;
            }
        }
        double range = calculateRange(source);

        if (range > CMConfig.EndergolemLongRangelimit * CMConfig.EndergolemLongRangelimit) {
            return false;
        }

        Entity entity = source.getImmediateSource();
        if (entity instanceof GolemEntity) {
            damage *= 0.5;
        }
        return super.attackEntityFrom(source, damage);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(IS_AWAKEN, false);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_Awaken", getIsAwaken());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setIsAwaken(compound.getBoolean("is_Awaken"));
    }

    public void setIsAwaken(boolean isAwaken) {
        getDataManager().set(IS_AWAKEN, isAwaken);
    }

    public boolean getIsAwaken() {
        return getDataManager().get(IS_AWAKEN);
    }

    public void tick() {
        super.tick();
        rotationYaw = renderYawOffset;
        repelEntities(1.7F, 3.7f, 1.7F, 1.7F);
        AnimationHandler.INSTANCE.updateAnimations(this);
        LivingEntity target = this.getAttackTarget();
        prevdeactivateProgress = deactivateProgress;
        if (!this.getIsAwaken() && deactivateProgress < 30F) {
            deactivateProgress++;
        }
        if (this.getIsAwaken() && deactivateProgress > 0F) {
            deactivateProgress--;

        }

        if(!this.getIsAwaken()) {
            if (this.ticksExisted % 20 == 0) {
                this.heal(2.0F);
            }
        }

        if (deactivateProgress == 0 && this.isAlive()) {
            if (target != null && target.isAlive()) {
                if (void_rune_attack_cooldown <= 0 && !isAIDisabled() && this.getAnimation() == NO_ANIMATION && target.isOnGround() && (this.rand.nextInt(45) == 0 && this.getDistance(target) < 4 || this.rand.nextInt(24) == 0 && this.getDistance(target) < 10)) {
                    void_rune_attack_cooldown = VOID_RUNE_ATTACK_COOLDOWN;
                    this.setAnimation(VOID_RUNE_ATTACK);

                } else if (this.getDistance(target) < 4 && !isAIDisabled() && this.getAnimation() == NO_ANIMATION) {
                    Animation animation = getRandomAttack(rand);
                    this.setAnimation(animation);
                }
            }

            if (this.getAnimation() == ANIMATION_EARTHQUAKE) {
                if (this.getAnimationTick() == 19) {
                    EarthQuake(5,6);
                    EarthQuakeParticle();
                    ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.1f, 0, 10);
                    if (Breaking) {
                        BlockBreaking(4,4,4);
                    } else {
                        if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                            BlockBreaking(4, 4, 4);
                        }
                    }
                }
            }
            if ((this.getAnimation() == ANIMATION_ATTACK1 || this.getAnimation() == ANIMATION_ATTACK2) && this.getAnimationTick() == 13) {
                this.playSound(ModSounds.GOLEMATTACK.get(), 1, 1);
                if (target != null) {
                    if (this.getDistance(target) < 4.75F) {
                        target.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) + this.rand.nextInt(4));
                        target.applyKnockback(1.25F, this.getPosX() - target.getPosX(), this.getPosZ() - target.getPosZ());
                    }
                }
            }
            if (this.getAnimation() == VOID_RUNE_ATTACK) {
                if (this.getAnimationTick() == 22) {
                    EarthQuake(4.25f,4);
                    EarthQuakeParticle();
                    ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.1f, 0, 10);
                    if (!this.world.isRemote) {
                        if (Breaking) {
                            BlockBreaking(4, 4, 4);
                        } else {
                            if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                                BlockBreaking(4, 4, 4);
                            }
                        }
                    }
                }
                if (this.getAnimationTick() == 28) {
                    VoidRuneAttack();
                }
            }
        }
        if (void_rune_attack_cooldown > 0) void_rune_attack_cooldown--;
        if (!world.isRemote) {
            timeWithoutTarget++;
            if (target != null) {
                timeWithoutTarget = 0;
                if(!this.getIsAwaken()) {
                    this.setIsAwaken(true);
                }
            }

            if (timeWithoutTarget > 400 && this.getIsAwaken() && target == null) {
                timeWithoutTarget = 0;
                this.setIsAwaken(false);
            }
        }

    }

    private void BlockBreaking(int x, int y, int z) {
        int MthX = MathHelper.floor(this.getPosX());
        int MthY = MathHelper.floor(this.getPosY());
        int MthZ = MathHelper.floor(this.getPosZ());
        boolean flag = false;
        if (!this.world.isRemote) {
            for (int k2 = -x; k2 <= x; ++k2) {
                for (int l2 = -z; l2 <= z; ++l2) {
                    for (int j = 0; j <= y; ++j) {
                        int i3 = MthX + k2;
                        int k = MthY + j;
                        int l = MthZ + l2;
                        BlockPos blockpos = new BlockPos(i3, k, l);
                        BlockState blockstate = this.world.getBlockState(blockpos);
                        Block block = blockstate.getBlock();
                        ITag<Block> Tag = BlockTags.getCollection().get(ModTag.ENDER_GOLEM_CAN_DESTROY);
                        if (block != Blocks.AIR && Tag.contains(block)) {
                            if (blockstate.canEntityDestroy(this.world, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                flag = this.world.destroyBlock(blockpos, true, this) || flag;
                            }
                        }
                    }
                }
            }
        }
    }


    private void EarthQuakeParticle() {
        if (this.world.isRemote) {
            BlockState block = world.getBlockState(getPosition().down());
            for (int i1 = 0; i1 < 20 + rand.nextInt(12); i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float angle = (0.01745329251F * this.renderYawOffset) + i1;
                double extraX = 4F * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = 4F * MathHelper.cos(angle);
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), this.getPosX() + extraX, this.getPosY() + extraY, this.getPosZ() + extraZ, motionX, motionY, motionZ);
            }
        }
    }

    private void EarthQuake(float grow, int damage) {
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1F + this.getRNG().nextFloat() * 0.1F);
        for (LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(grow))) {
            if (!isOnSameTeam(entity) && !(entity instanceof Ender_Golem_Entity) && entity != this) {
                entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) + this.rand.nextInt(damage));
                launch(entity, true);

            }
        }
    }


    private void VoidRuneAttack() {
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            double d0 = Math.min(target.getPosY(), this.getPosY());
            double d1 = Math.max(target.getPosY(), this.getPosY()) + 1.0D;
            float f = (float) MathHelper.atan2(target.getPosZ() - this.getPosZ(), target.getPosX() - this.getPosX());
            float f2 = MathHelper.cos(this.rotationYaw * ((float) Math.PI / 180F)) * (2.0F);
            float f3 = MathHelper.sin(this.rotationYaw * ((float) Math.PI / 180F)) * (2.0F);
            for(int l = 0; l < 10; ++l) {
                double d2 = 1.5D * (double)(l + 1);
                int j = (int) (1.25f * l);
                this.spawnFangs(this.getPosX() + f2 + (double)MathHelper.cos(f) * d2, this.getPosZ() + f3 + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
                this.spawnFangs(this.getPosX() - f2 + (double)MathHelper.cos(f) * d2, this.getPosZ() - f3 + (double)MathHelper.sin(f) * d2, d0, d1, f, j);
            }
            for (int k = 0; k < 6; ++k) {
                float f4 = f + (float) k * (float) Math.PI * 2.0F / 6.0F + ((float) Math.PI * 2F / 7.5F);
                this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f4) * 2.5D, this.getPosZ() + (double) MathHelper.sin(f4) * 2.5D, d0, d1, f2, 5);
            }
            for (int k = 0; k < 8; ++k) {
                this.spawnFangs(this.getPosX() + this.rand.nextGaussian() * 4.5D, this.getPosZ() + this.rand.nextGaussian() * 4.5D, d0, d1, f3, 15);
            }

        }
    }


    private void spawnFangs(double x, double z, double minY, double maxY, float rotation, int delay) {
        BlockPos blockpos = new BlockPos(x, maxY, z);
        boolean flag = false;
        double d0 = 0.0D;

        do {
            BlockPos blockpos1 = blockpos.down();
            BlockState blockstate = this.world.getBlockState(blockpos1);
            if (blockstate.isSolidSide(this.world, blockpos1, Direction.UP)) {
                if (!this.world.isAirBlock(blockpos)) {
                    BlockState blockstate1 = this.world.getBlockState(blockpos);
                    VoxelShape voxelshape = blockstate1.getCollisionShapeUncached(this.world, blockpos);
                    if (!voxelshape.isEmpty()) {
                        d0 = voxelshape.getEnd(Direction.Axis.Y);
                    }
                }

                flag = true;
                break;
            }

            blockpos = blockpos.down();
        } while(blockpos.getY() >= MathHelper.floor(minY) - 1);

        if (flag) {
            this.world.addEntity(new Void_Rune_Entity(this.world, x, (double)blockpos.getY() + d0, z, rotation, delay, this));
        }
    }

    private void launch(Entity e, boolean huge) {

        double d0 = e.getPosX() - this.getPosX();
        double d1 = e.getPosZ() - this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        float f = huge ? 2F : 0.5F;
        e.addVelocity(d0 / d2 * f, huge ? 0.5D : 0.2F, d1 / d2 * f);
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof Ender_Golem_Entity || entityIn instanceof Ender_Guardian_Entity || entityIn instanceof ShulkerEntity || entityIn instanceof Endermaptera_Entity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }

    @Override
    protected void onDeathAIUpdate() {
        super.onDeathAIUpdate();
        setMotion(0, this.getMotion().y, 0);
        if (this.deathTime == 40) {
            this.playSound(ModSounds.MONSTROSITYLAND.get(), 1, 1);
        }

    }


    @Override
    protected void repelEntities(float x, float y, float z, float radius) {
        super.repelEntities(x, y, z, radius);
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }

    @Nullable
    public Animation getDeathAnimation()
    {
        return ENDER_GOLEM_DEATH;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(SoundEvents.ENTITY_IRON_GOLEM_STEP, 1.0F, 1.0F);
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.GOLEMHURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.GOLEMDEATH.get();
    }

    @Override
    protected BodyController createBodyController() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigator createNavigator(World worldIn) {
        return new CMPathNavigateGround(this, world);
    }

    class AttackGoal extends Goal {


        public AttackGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Golem_Entity.this.getAnimation() == ANIMATION_EARTHQUAKE || Ender_Golem_Entity.this.getAnimation() == VOID_RUNE_ATTACK;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            Ender_Golem_Entity.this.setMotion(0, Ender_Golem_Entity.this.getMotion().y, 0);
            LivingEntity target = Ender_Golem_Entity.this.getAttackTarget();

            if(Ender_Golem_Entity.this.getAnimation() == ANIMATION_EARTHQUAKE) {
                if (Ender_Golem_Entity.this.getAnimationTick() < 19 && target != null) {
                    Ender_Golem_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Golem_Entity.this.rotationYaw = Ender_Golem_Entity.this.prevRotationYaw;
                    //Ender_Golem_Entity.this.renderYawOffset = Ender_Golem_Entity.this.prevRenderYawOffset;
                }
            }
            if(Ender_Golem_Entity.this.getAnimation() == VOID_RUNE_ATTACK) {
                if (Ender_Golem_Entity.this.getAnimationTick() < 22 && target != null) {
                    Ender_Golem_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Golem_Entity.this.rotationYaw = Ender_Golem_Entity.this.prevRotationYaw;
                    //Ender_Golem_Entity.this.renderYawOffset = Ender_Golem_Entity.this.prevRenderYawOffset;
                }
            }
        }
    }

    class AwakenGoal extends Goal {

        public AwakenGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return deactivateProgress > 0F;
        }

        @Override
        public void tick() {
            Ender_Golem_Entity.this.setMotion(0, Ender_Golem_Entity.this.getMotion().y, 0);
        }
    }

}





