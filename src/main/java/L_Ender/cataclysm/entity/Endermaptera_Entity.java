package L_Ender.cataclysm.entity;

import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.etc.DirectPathNavigator;
import L_Ender.cataclysm.entity.etc.FlightMoveController;
import L_Ender.cataclysm.entity.projectile.Void_Shard_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModSounds;
import L_Ender.cataclysm.init.ModTag;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.ClimberPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class Endermaptera_Entity extends MonsterEntity implements IAnimatedEntity {
    public static final ResourceLocation HAS_JAWS_LOOT = new ResourceLocation("cataclysm", "entities/endermaptera_has_jaws");
    private int animationTick;
    private Animation currentAnimation;
    public static final Animation JAW_ATTACK = Animation.create(13);
    public static final Animation HEADBUTT_ATTACK = Animation.create(13);
    public float attachChangeProgress = 0F;
    public float prevAttachChangeProgress = 0F;
    private static final DataParameter<Direction> ATTACHED_FACE = EntityDataManager.createKey(Endermaptera_Entity.class, DataSerializers.DIRECTION);
    private static final DataParameter<Byte> CLIMBING = EntityDataManager.createKey(Endermaptera_Entity.class, DataSerializers.BYTE);
    private static final DataParameter<Boolean> HAS_JAWS = EntityDataManager.createKey(Endermaptera_Entity.class, DataSerializers.BOOLEAN);
    private static final Direction[] HORIZONTALS = new Direction[]{Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private Direction prevAttachDir = Direction.DOWN;
    private boolean isUpsideDownNavigator;

    public Endermaptera_Entity(EntityType entity, World world) {
        super(entity, world);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.experienceValue = 6;
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0f, false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 0.8D));
        this.goalSelector.addGoal(6, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(6, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this).setCallsForHelp());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

    public CreatureAttribute getCreatureAttribute() {
        return CreatureAttribute.ARTHROPOD;
    }

    private void switchNavigator(boolean rightsideUp) {
        if (rightsideUp) {
            this.moveController = new MovementController(this);
            this.navigator = new ClimberPathNavigator(this, world);
            this.isUpsideDownNavigator = false;
        } else {
            this.moveController = new FlightMoveController(this, 0.6F, false);
            this.navigator = new DirectPathNavigator(this, world);
            this.isUpsideDownNavigator = true;
        }
    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.27F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 4.0D)
                .createMutableAttribute(Attributes.MAX_HEALTH, 16)
                .createMutableAttribute(Attributes.ARMOR, 6)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.25);
    }


    protected void registerData() {
        super.registerData();
        this.dataManager.register(CLIMBING, (byte) 0);
        this.dataManager.register(ATTACHED_FACE, Direction.DOWN);
        this.dataManager.register(HAS_JAWS, true);
    }

    public Direction getAttachmentFacing() {
        return this.dataManager.get(ATTACHED_FACE);
    }

    protected PathNavigator createNavigator(World worldIn) {
        return new ClimberPathNavigator(this, worldIn);
    }

    @Nullable
    protected ResourceLocation getLootTable() {
        return this.getHasJaws() ? HAS_JAWS_LOOT : super.getLootTable();
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("Has_Jaws", getHasJaws());

    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        this.dataManager.set(ATTACHED_FACE, Direction.byIndex(compound.getByte("AttachFace")));
        setHasJaw(compound.getBoolean("Has_Jaws"));
    }

    public void setHasJaw(boolean HasJaws) {
        getDataManager().set(HAS_JAWS, HasJaws);

    }

    public boolean getHasJaws() {
        return getDataManager().get(HAS_JAWS);

    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    protected void updateFallState(double y, boolean onGroundIn, BlockState state, BlockPos pos) {
    }

    protected SoundEvent getAmbientSound() {
        this.playSound(ModSounds.ENDERMAPTERA_AMBIENT.get(), 1.0f, 0.6f);
        return null;
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        this.playSound(ModSounds.ENDERMAPTERA_HURT.get(), 1.0f, 0.6f);
        return null;
    }

    protected SoundEvent getDeathSound() {
        this.playSound(ModSounds.ENDERMAPTERA_DEATH.get(), 1.0f, 0.6f);
        return null;
    }

    protected void playStepSound(BlockPos pos, BlockState blockIn) {
        this.playSound(ModSounds.ENDERMAPTERA_STEP.get(), 0.15F, 0.6F);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, JAW_ATTACK, HEADBUTT_ATTACK};
    }

    public boolean isOnSameTeam(Entity entityIn) {
        if (entityIn == this) {
            return true;
        } else if (super.isOnSameTeam(entityIn)) {
            return true;
        } else if (entityIn instanceof Ender_Guardian_Entity || entityIn instanceof Ender_Golem_Entity || entityIn instanceof ShulkerEntity || entityIn instanceof Endermaptera_Entity) {
            return this.getTeam() == null && entityIn.getTeam() == null;
        } else {
            return false;
        }
    }


    public boolean attackEntityAsMob(Entity entityIn) {
        if (this.getHasJaws()) {
            this.setAnimation(JAW_ATTACK);
        } else {
            this.setAnimation(HEADBUTT_ATTACK);
        }
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        this.prevAttachChangeProgress = this.attachChangeProgress;
        AnimationHandler.INSTANCE.updateAnimations(this);
        if (attachChangeProgress > 0F) {
            attachChangeProgress -= 0.25F;
        }
        Vector3d vector3d = this.getMotion();
        if (!this.world.isRemote) {
            this.setBesideClimbableBlock(this.collidedHorizontally || this.collidedVertically && !this.isOnGround());
            if (this.isOnGround() || this.isInWaterOrBubbleColumn() || this.isInLava()) {
                this.dataManager.set(ATTACHED_FACE, Direction.DOWN);
            } else if (this.collidedVertically) {
                this.dataManager.set(ATTACHED_FACE, Direction.UP);
            } else {
                Direction closestDirection = Direction.DOWN;
                double closestDistance = 100;
                for (Direction dir : HORIZONTALS) {
                    BlockPos antPos = new BlockPos(MathHelper.floor(this.getPosX()), MathHelper.floor(this.getPosY()), MathHelper.floor(this.getPosZ()));
                    BlockPos offsetPos = antPos.offset(dir);
                    Vector3d offset = Vector3d.copyCentered(offsetPos);
                    if (closestDistance > this.getPositionVec().distanceTo(offset) && world.isDirectionSolid(offsetPos, this, dir.getOpposite())) {
                        closestDistance = this.getPositionVec().distanceTo(offset);
                        closestDirection = dir;
                    }
                }
                this.dataManager.set(ATTACHED_FACE, closestDirection);
            }
        }
        boolean flag = false;
        if (this.getAttachmentFacing() != Direction.DOWN) {
            if (this.getAttachmentFacing() == Direction.UP) {
                this.setMotion(this.getMotion().add(0, 1, 0));
            } else {
                if (!this.collidedHorizontally && this.getAttachmentFacing() != Direction.UP) {
                    Vector3d vec = Vector3d.copy(this.getAttachmentFacing().getDirectionVec());
                    this.setMotion(this.getMotion().add(vec.normalize().mul(0.1F, 0.1F, 0.1F)));
                }
                if (!this.onGround && vector3d.y < 0.0D) {
                    this.setMotion(this.getMotion().mul(1.0D, 0.5D, 1.0D));
                    flag = true;
                }
            }
        }
        if (this.getAttachmentFacing() == Direction.UP) {
            this.setNoGravity(true);
            this.setMotion(vector3d.mul(0.7D, 1D, 0.7D));
        } else {
            this.setNoGravity(false);
        }
        if (!flag) {
            if (this.isOnLadder()) {
                this.setMotion(vector3d.mul(1.0D, 0.4D, 1.0D));
            }
        }
        if (prevAttachDir != this.getAttachmentFacing()) {
            attachChangeProgress = 1F;
        }
        this.prevAttachDir = this.getAttachmentFacing();
        if (!this.world.isRemote) {
            if (this.getAttachmentFacing() == Direction.UP && !this.isUpsideDownNavigator) {
                switchNavigator(false);
            }
            if (this.getAttachmentFacing() != Direction.UP && this.isUpsideDownNavigator) {
                switchNavigator(true);
            }
            LivingEntity target = this.getAttackTarget();
            if (target != null && getDistance(target) < target.getWidth() + this.getWidth() && this.canEntityBeSeen(target)) {
                float damage = (float) ((int) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                if (this.getAnimation() == JAW_ATTACK && this.getAnimationTick() == 11) {
                    target.attackEntityFrom(DamageSource.causeMobDamage(this), damage);
                    if (this.rand.nextInt(6) == 0) {
                        BrokenJaws();
                    }
                }
                if (this.getAnimation() == HEADBUTT_ATTACK && this.getAnimationTick() == 6) {
                    target.attackEntityFrom(DamageSource.causeMobDamage(this), damage * 0.75f);
                }
            }
        } else {
            for (int i = 0; i < 2; ++i) {
                this.world.addParticle(ParticleTypes.PORTAL, this.getPosXRandom(0.5D), this.getPosYRandom(), this.getPosZRandom(0.5D), (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D);
            }
        }

    }

    private void BrokenJaws() {
        this.playSound(SoundEvents.ENTITY_ITEM_BREAK, 0.5f, 1F + this.getRNG().nextFloat() * 0.1F);
        this.setHasJaw(false);
        int shardCount = 8 + rand.nextInt(4);
        if (!this.world.isRemote) {
            for (int i = 0; i < shardCount; i++) {
                float f = ((i + 1) / (float) shardCount) * 360F;
                Void_Shard_Entity shard = new Void_Shard_Entity(ModEntities.VOID_SHARD.get(), this.world, this);
                shard.shootFromRotation(this, this.rotationPitch - rand.nextInt(40), f, 0.0F, 0.15F + rand.nextFloat() * 0.2F, 1.0F);
                world.addEntity(shard);
            }
        }
    }

    protected void onInsideBlock(BlockState state) {

    }

    public boolean isOnLadder() {
        return this.isBesideClimbableBlock();
    }

    public boolean isBesideClimbableBlock() {
        return (this.dataManager.get(CLIMBING) & 1) != 0;
    }

    public void setBesideClimbableBlock(boolean climbing) {
        byte b0 = this.dataManager.get(CLIMBING);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }

        this.dataManager.set(CLIMBING, b0);
    }

    public static boolean canSpawn(EntityType<Endermaptera_Entity> entity, IWorld worldIn, SpawnReason reason, BlockPos pos, Random randomIn) {
        return canSpawnOn(entity, worldIn, reason, pos, randomIn) && !worldIn.getBlockState(pos.down()).isIn(BlockTags.getCollection().get(ModTag.ENDERMAPTERA_CAN_NOT_SPAWN));
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

}
