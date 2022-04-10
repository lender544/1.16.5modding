package L_Ender.cataclysm.entity;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.AI.CmAttackGoal;
import L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import L_Ender.cataclysm.entity.etc.CMPathNavigateGround;
import L_Ender.cataclysm.entity.etc.GroundPathNavigatorWide;
import L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import L_Ender.cataclysm.entity.etc.SmartBodyHelper3;
import L_Ender.cataclysm.entity.projectile.Ender_Guardian_Bullet_Entity;
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
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ShulkerBulletEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.*;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import static java.lang.Math.*;

public class Ender_Guardian_Entity extends Boss_monster {

    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    private static final DataParameter<Boolean> IS_HELMETLESS = EntityDataManager.createKey(Ender_Guardian_Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_AWAKEN = EntityDataManager.createKey(Ender_Guardian_Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> USED_MASS_DESTRUCTION = EntityDataManager.createKey(Ender_Guardian_Entity.class, DataSerializers.BOOLEAN);
    public static final Animation GUARDIAN_RIGHT_STRONG_ATTACK = Animation.create(60);
    public static final Animation GUARDIAN_LEFT_STRONG_ATTACK = Animation.create(60);
    public static final Animation GUARDIAN_RIGHT_ATTACK = Animation.create(40);
    public static final Animation GUARDIAN_LEFT_ATTACK = Animation.create(40);
    public static final Animation GUARDIAN_BURST_ATTACK = Animation.create(53);
    public static final Animation GUARDIAN_UPPERCUT_AND_BULLET = Animation.create(100);
    public static final Animation GUARDIAN_RAGE_UPPERCUT = Animation.create(120);
    public static final Animation GUARDIAN_STOMP = Animation.create(48);
    public static final Animation GUARDIAN_RAGE_STOMP = Animation.create(83);
    public static final Animation GUARDIAN_MASS_DESTRUCTION = Animation.create(75);
    public static final Animation GUARDIAN_FALLEN = Animation.create(196);
    public static final int STOMP_COOLDOWN = 400;
    public float deactivateProgress;
    public float prevdeactivateProgress;
    public boolean Breaking = CMConfig.EnderguardianBlockBreaking;

    private int stomp_cooldown = 0;

    public Ender_Guardian_Entity(EntityType entity, World world) {
        super(entity, world);
        this.experienceValue = 300;
        this.stepHeight = 1.5F;
        this.dropAfterDeathAnim = true;
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{
                NO_ANIMATION,
                GUARDIAN_RIGHT_STRONG_ATTACK,
                GUARDIAN_LEFT_STRONG_ATTACK,
                GUARDIAN_BURST_ATTACK,
                GUARDIAN_UPPERCUT_AND_BULLET,
                GUARDIAN_STOMP,
                GUARDIAN_RIGHT_ATTACK,
                GUARDIAN_LEFT_ATTACK,
                GUARDIAN_MASS_DESTRUCTION,
                GUARDIAN_RAGE_STOMP,
                GUARDIAN_RAGE_UPPERCUT,
                GUARDIAN_FALLEN};
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(2, new CmAttackGoal(this,1.0));
        this.goalSelector.addGoal(1, new PunchAttackGoal());
        this.goalSelector.addGoal(1, new BurstAttackGoal());
        this.goalSelector.addGoal(1, new StompAttackGoal());
        this.goalSelector.addGoal(1, new UppercutAndBulletGoal());
        this.goalSelector.addGoal(1, new RageUppercut());
        this.goalSelector.addGoal(1, new MassDestruction());
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));

    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 50.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.27F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, CMConfig.EnderguardianDamage)
                .createMutableAttribute(Attributes.MAX_HEALTH, CMConfig.EnderguardianHealth)
                .createMutableAttribute(Attributes.ARMOR, 20)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(IS_HELMETLESS, false);
        getDataManager().register(IS_AWAKEN, true);
        getDataManager().register(USED_MASS_DESTRUCTION, false);
    }

    private static Animation getRandomAttack(Random rand) {
        switch (rand.nextInt(4)) {
            case 0:
                return GUARDIAN_RIGHT_STRONG_ATTACK;
            case 1:
                return GUARDIAN_LEFT_STRONG_ATTACK;
            case 2:
                return GUARDIAN_RIGHT_ATTACK;
            case 3:
                return GUARDIAN_LEFT_ATTACK;
        }
        return GUARDIAN_RIGHT_STRONG_ATTACK;
    }


    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_Helmetless", getIsHelmetless());
        compound.putBoolean("is_Awaken", getIsAwaken());
        compound.putBoolean("used_mass_destruction", getUsedMassDestruction());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setIsHelmetless(compound.getBoolean("is_Helmetless"));
        setIsAwaken(compound.getBoolean("is_Awaken"));
        setUsedMassDestruction(compound.getBoolean("used_mass_destruction"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setIsHelmetless(boolean isHelmetless) {
        getDataManager().set(IS_HELMETLESS, isHelmetless);

    }

    public boolean getIsHelmetless() {
        return getDataManager().get(IS_HELMETLESS);

    }

    public void setUsedMassDestruction(boolean usedMassDestruction) {
        getDataManager().set(USED_MASS_DESTRUCTION, usedMassDestruction);
    }

    public boolean getUsedMassDestruction() {
        return getDataManager().get(USED_MASS_DESTRUCTION);
    }

    public void setIsAwaken(boolean isAwaken) {
        getDataManager().set(IS_AWAKEN, isAwaken);
    }

    public boolean getIsAwaken() {
        return getDataManager().get(IS_AWAKEN);
    }


    public void setCustomName(@Nullable ITextComponent name) {
        super.setCustomName(name);
        this.bossInfo.setName(this.getDisplayName());
    }

    protected int decreaseAirSupply(int air) {
        return air;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        if (this.getAnimation() == GUARDIAN_MASS_DESTRUCTION && !source.canHarmInCreative()) {
            return false;
        }
        if (!source.canHarmInCreative()) {
            damage = Math.min(CMConfig.EnderguardianDamageCap, damage);
        }
        double range = calculateRange(source);

        if (range > CMConfig.EnderguardianLongRangelimit * CMConfig.EnderguardianLongRangelimit) {
            return false;
        }

        if (!this.getIsAwaken()) {
            this.setIsAwaken(true);
        }
        Entity entity = source.getImmediateSource();
        if (!this.getIsHelmetless()) {
            if (entity instanceof AbstractArrowEntity) {
                return false;
            }
        }
        if (entity instanceof ShulkerBulletEntity || entity instanceof Ender_Guardian_Bullet_Entity) {
            return false;
        }
        if (entity instanceof GolemEntity) {
            damage *= 0.5;
        }

        return super.attackEntityFrom(source, damage);
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }


    public void tick() {
        super.tick();
        rotationYaw = renderYawOffset;
        //prevRotationYaw = rotationYaw;
        if (!this.isSilent() && !world.isRemote) {
            this.world.setEntityState(this, (byte) 67);
        }
        repelEntities(1.8F, 4.0f, 1.8F, 1.8F);
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        prevdeactivateProgress = deactivateProgress;
        if (!this.getIsAwaken() && deactivateProgress < 40F) {
            deactivateProgress = 40;
        }
        LivingEntity target = this.getAttackTarget();
        Animation animation = getRandomAttack(rand);
        if (this.isAlive()) {
            if (!this.getIsHelmetless() && this.isHelmetless()) {
                this.setIsHelmetless(true);
                BrokenHelmet();
            }
            if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && !this.getUsedMassDestruction() && this.isHelmetless()) {
                this.setAnimation(GUARDIAN_MASS_DESTRUCTION);
            }
            else if (target != null && target.isAlive()) {
                if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && this.getDistance(target) < 2.75F) {
                    if (this.rand.nextInt(2) == 0) {
                        this.setAnimation(GUARDIAN_BURST_ATTACK);
                    } else {
                        this.setAnimation(animation);
                    }
                } else if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && this.getDistance(target) > 2.75F && this.getDistance(target) < 4.3F && target.isPotionActive(Effects.LEVITATION)) {
                    if (this.rand.nextInt(3) == 0) {
                        if (this.getIsHelmetless()) {
                            this.setAnimation(GUARDIAN_RAGE_UPPERCUT);
                        } else {
                            this.setAnimation(GUARDIAN_UPPERCUT_AND_BULLET);
                        }
                    } else {
                        this.setAnimation(GUARDIAN_BURST_ATTACK);
                    }
                } else if (stomp_cooldown <= 0 && !isAIDisabled() && this.getAnimation() == NO_ANIMATION && target.isOnGround()&& (this.getDistance(target) > 6F && this.getDistance(target) < 13F || this.getDistance(target) > 2.75F && this.getDistance(target) < 4.3F && this.rand.nextInt(12) == 0)) {
                    stomp_cooldown = STOMP_COOLDOWN;
                    if (this.getIsHelmetless()) {
                        this.setAnimation(GUARDIAN_RAGE_STOMP);
                    } else {
                        this.setAnimation(GUARDIAN_STOMP);

                    }
                } else if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && this.getDistance(target) > 2.75F && this.getDistance(target) < 4.3F) {
                    if (this.rand.nextInt(4) == 0) {
                        if (this.getIsHelmetless()) {
                            this.setAnimation(GUARDIAN_RAGE_UPPERCUT);
                        } else {
                            this.setAnimation(GUARDIAN_UPPERCUT_AND_BULLET);

                        }
                    } else {
                        this.setAnimation(animation);
                    }
                }
            }
        }

        AnimationHandler.INSTANCE.updateAnimations(this);

        if (this.getIsHelmetless()) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(10F);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.29F);
        }

        if (this.getAnimation() == GUARDIAN_LEFT_STRONG_ATTACK) {
            if (this.getAnimationTick() < 2) {
                GravityPullparticle();
            }
            if (this.getAnimationTick() < 29) {
                GravityPull();
            }
            if (this.getAnimationTick() == 34) {
                this.playSound(ModSounds.ENDER_GUARDIAN_FIST.get(), 0.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                AreaAttack(5.15f,5,70,1.1f,100);
                Attackparticle(2.2f,0);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 20, 0.2f, 0, 10);
            }

        }
        if (this.getAnimation() == GUARDIAN_RIGHT_STRONG_ATTACK) {
            if (this.getAnimationTick() < 2) {
                GravityPullparticle();
            }
            if (this.getAnimationTick() < 24) {
                GravityPull();
            }
            if (this.getAnimationTick() == 29) {
                AreaAttack(5.15f,5,70,1.1f,100);
                this.playSound(ModSounds.ENDER_GUARDIAN_FIST.get(), 0.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                Attackparticle(2.2f,0);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 20, 0.2f, 0, 10);
            }

        }

        if (this.getAnimation() == GUARDIAN_RIGHT_ATTACK) {
            if (this.getAnimationTick() == 22) {
                AreaAttack(5.85f,5,80,1,80);
                this.playSound(ModSounds.ENDER_GUARDIAN_FIST.get(), 0.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                Attackparticle(2.75f,0.5f);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.1f, 0, 10);
            }
        }
        if (this.getAnimation() == GUARDIAN_LEFT_ATTACK) {
            if (this.getAnimationTick() == 19) {
                AreaAttack(5.85f,5,80,1,80);
                this.playSound(ModSounds.ENDER_GUARDIAN_FIST.get(), 0.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                Attackparticle(2.75f,-0.5f);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.1f, 0, 10);
            }
        }

        if (this.getAnimation() == GUARDIAN_BURST_ATTACK) {
            if (this.getAnimationTick() == 15) {
                Burstparticle();
            }
            if (this.getAnimationTick() == 27) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                AreaAttack(7.5f,6,100,1,0);
            }
        }
        if (this.getAnimation() == GUARDIAN_UPPERCUT_AND_BULLET || this.getAnimation() == GUARDIAN_RAGE_UPPERCUT) {
            if (this.getAnimationTick() == 27) {
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                AreaAttack(6.25f,6,60,1.5f,150);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.3f, 0, 10);
            }
        }
        if (this.getAnimation() == GUARDIAN_STOMP) {
            if (this.getAnimationTick() == 32) {
                StompAttack();
                Attackparticle(0.4f,0.8f);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 10, 0.1f, 0, 5);
            }
        }
        if (this.getAnimation() == GUARDIAN_RAGE_STOMP) {
            if (this.getAnimationTick() == 32 || this.getAnimationTick() == 53 || this.getAnimationTick() == 62) {
                StompAttack();
                Attackparticle(0.4f,0.8f);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 10, 0.1f, 0, 5);
            }
        }
        if (this.getAnimation() == GUARDIAN_RAGE_UPPERCUT) {
            if (this.getAnimationTick() == 84) {
                RageAttack();
                AreaAttack(5.5f,5,120,1.2f,100);
                this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1F + this.getRNG().nextFloat() * 0.1F);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.2f, 0, 10);
            }
        }

        if (this.getAnimation() == GUARDIAN_MASS_DESTRUCTION) {
            this.setUsedMassDestruction(true);
            if (this.getAnimationTick() == 39) {
                Attackparticle(2.75f,2.25f);
                Attackparticle(2.75f,-2.25f);
                MassDestruction(5.0f, 1.1f,150);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 15, 0.3f, 0, 10);
                if (Breaking) {
                    BlockBreaking(CMConfig.EnderguardianBlockBreakingX, CMConfig.EnderguardianBlockBreakingY, CMConfig.EnderguardianBlockBreakingZ);
                } else {
                    if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                        BlockBreaking(CMConfig.EnderguardianBlockBreakingX, CMConfig.EnderguardianBlockBreakingY, CMConfig.EnderguardianBlockBreakingZ);
                    }
                }
            }
        }

        if (stomp_cooldown > 0) stomp_cooldown--;

    }

    public boolean isHelmetless() {
        return this.getHealth() <= this.getMaxHealth() / 2.0F;
    }

    public void setMaxHealth(double maxHealth, boolean heal){
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(maxHealth);
        if(heal){
            this.heal((float)maxHealth);
        }
    }

    @Override
    protected void onDeathAIUpdate() {
        super.onDeathAIUpdate();
        setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
        if (this.deathTime == 50) {
            this.playSound(ModSounds.MONSTROSITYLAND.get(), 1, 1);
        }
        if (this.deathTime == 100) {
            this.playSound(SoundEvents.ENTITY_SHULKER_TELEPORT, 1, 1);
        }

    }

    @Nullable
    public Animation getDeathAnimation()
    {
        return GUARDIAN_FALLEN;
    }


    private void AreaAttack(float range,float height,float arc ,float damage,int ticks) {
        List<LivingEntity> entitiesHit = this.getEntityLivingBaseNearby(range, height, range, range);
        for (LivingEntity entityHit : entitiesHit) {
            float entityHitAngle = (float) ((Math.atan2(entityHit.getPosZ() - this.getPosZ(), entityHit.getPosX() - this.getPosX()) * (180 / Math.PI) - 90) % 360);
            float entityAttackingAngle = this.renderYawOffset % 360;
            if (entityHitAngle < 0) {
                entityHitAngle += 360;
            }
            if (entityAttackingAngle < 0) {
                entityAttackingAngle += 360;
            }
            float entityRelativeAngle = entityHitAngle - entityAttackingAngle;
            float entityHitDistance = (float) Math.sqrt((entityHit.getPosZ() - this.getPosZ()) * (entityHit.getPosZ() - this.getPosZ()) + (entityHit.getPosX() - this.getPosX()) * (entityHit.getPosX() - this.getPosX()));
            if (entityHitDistance <= range && (entityRelativeAngle <= arc / 2 && entityRelativeAngle >= -arc / 2) || (entityRelativeAngle >= 360 - arc / 2 || entityRelativeAngle <= -360 + arc / 2)) {
                if (!(entityHit instanceof Ender_Guardian_Entity)) {
                    entityHit.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);
                    if (entityHit instanceof PlayerEntity && entityHit.isActiveItemStackBlocking() && ticks > 0) {
                        disableShield(entityHit, ticks);
                    }
                    if(this.getAnimation() == GUARDIAN_UPPERCUT_AND_BULLET || this.getAnimation() == GUARDIAN_RAGE_UPPERCUT){
                        entityHit.setMotion(entityHit.getMotion().add(0.0D, 0.5F, 0.0D));
                    }
                    if(this.getAnimation() == GUARDIAN_BURST_ATTACK){
                        launch(entityHit);
                    }
                }
            }
        }
    }

    private void MassDestruction(float grow, float damage, int ticks) {
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1F + this.getRNG().nextFloat() * 0.1F);
        for (LivingEntity entityHit : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(grow))) {
            if (!isOnSameTeam(entityHit) && !(entityHit instanceof Ender_Guardian_Entity) && entityHit != this) {
                entityHit.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * damage);
                if (entityHit instanceof PlayerEntity && entityHit.isActiveItemStackBlocking()) {
                    disableShield(entityHit, ticks);
                }
                launch(entityHit);
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
                    for (int j = -y; j <= -1; ++j) {
                        int i3 = MthX + k2;
                        int k = MthY + j;
                        int l = MthZ + l2;
                        BlockPos blockpos = new BlockPos(i3, k, l);
                        BlockState blockstate = this.world.getBlockState(blockpos);
                        Block block = blockstate.getBlock();
                        ITag<Block> Tag = BlockTags.getCollection().get(ModTag.ENDER_GUARDIAN_CAN_DESTROY);
                        if (block != Blocks.AIR && Tag.contains(block)) {
                            if (blockstate.canEntityDestroy(this.world, blockpos, this) && net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this, blockpos, blockstate)) {
                                flag = this.world.destroyBlock(blockpos, false, this) || flag;
                            }
                        }
                    }
                }
            }
        }
    }


    private void Burstparticle() {
        if (this.world.isRemote) {
            double d0 = this.getPosX();
            double d1 = this.getPosY() + 2.0;
            double d2 = this.getPosZ();
            int size = (int) 5f;
            for (int i = -size; i <= size; ++i) {
                for (int j = -size; j <= size; ++j) {
                    for (int k = -size; k <= size; ++k) {
                        double d3 = (double) j + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                        double d4 = (double) i + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                        double d5 = (double) k + (this.rand.nextDouble() - this.rand.nextDouble()) * 0.5D;
                        double d6 = (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5) / 0.5 + this.rand.nextGaussian() * 0.05D;
                        this.world.addParticle(ParticleTypes.REVERSE_PORTAL, d0, d1, d2, d3 / d6, d4 / d6, d5 / d6);
                        if (i != -size && i != size && j != -size && j != size) {
                            k += size * 2 - 1;
                        }
                    }
                }
            }
        }
    }

    private void launch(Entity entityHit) {
        double d0 = entityHit.getPosX() - this.getPosX();
        double d1 = entityHit.getPosZ() - this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        entityHit.addVelocity(d0 / d2 * 4.0D, 0.2D, d1 / d2 * 4.0D);
    }

    private void GravityPull() {
        List<LivingEntity> entities = getEntityLivingBaseNearby(12, 12, 12, 12);
        for (LivingEntity inRange : entities) {
            if (inRange instanceof PlayerEntity && ((PlayerEntity) inRange).abilities.disableDamage) continue;
            if (isOnSameTeam(inRange)) continue;
            float angle = (0.01745329251F * this.renderYawOffset);
            double extraX = MathHelper.sin((float) (Math.PI + angle));
            double extraZ = MathHelper.cos(angle);
            double theta = (renderYawOffset) * (Math.PI / 180);
            theta += Math.PI / 2;
            double vecX = Math.cos(theta);
            double vecZ = Math.sin(theta);
            Vector3d diff = inRange.getPositionVec().subtract(getPositionVec().add(2.0 * vecX + extraX * 0.25, 0, 2.0 * vecZ + extraZ * 0.25));
            diff = diff.normalize().scale(0.085);
            inRange.setMotion(inRange.getMotion().subtract(diff));

        }
    }

    private void GravityPullparticle() {
        if (this.world.isRemote) {
            for (int i1 = 0; i1 < 80 + rand.nextInt(12); i1++) {
                float angle = (0.01745329251F * this.renderYawOffset) + i1;
                double extraX = MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = MathHelper.cos(angle);
                double theta = (renderYawOffset) * (Math.PI / 180);
                theta += Math.PI / 2;
                double vecX = Math.cos(theta);
                double vecZ = Math.sin(theta);
                this.world.addParticle(ParticleTypes.PORTAL, getPosX() + 2.2 * vecX + extraX * 0.75, this.getPosY() + extraY, getPosZ() + 2.2 * vecZ + extraZ * 0.75, (this.rand.nextDouble() - 0.5D) * 12.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 12.0D);
            }
        }
    }

    private void Attackparticle(float vec, float math) {
        if (this.world.isRemote) {
            for (int i1 = 0; i1 < 80 + rand.nextInt(12); i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float angle = (0.01745329251F * this.renderYawOffset) + i1;
                float f = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F)) ;
                float f1 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)) ;
                double extraX = 1.2 * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = 1.2 * MathHelper.cos(angle);
                double theta = (renderYawOffset) * (Math.PI / 180);
                theta += Math.PI / 2;
                double vecX = Math.cos(theta);
                double vecZ = Math.sin(theta);
                int hitX = MathHelper.floor(getPosX() + vec * vecX+ extraX);
                int hitY = MathHelper.floor(getPosY());
                int hitZ = MathHelper.floor(getPosZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = world.getBlockState(hit.down());
                this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), getPosX() + vec * vecX + extraX + f * math, this.getPosY() + extraY, getPosZ() + vec * vecZ + extraZ + f1 * math, motionX, motionY, motionZ);

            }
        }
    }

    private void StompAttack() {
        this.playSound(ModSounds.ENDER_GUARDIAN_FIST.get(), 0.3f, 1F + this.getRNG().nextFloat() * 0.1F);
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            double d0 = Math.min(target.getPosY(), this.getPosY());
            double d1 = Math.max(target.getPosY(), this.getPosY()) + 1.0D;
            Vector3d looking = this.getLookVec();
            Vector3d[] all = new Vector3d[]{looking, looking.rotateYaw(0.40f), looking.rotateYaw(-0.40f)};
            Vector3d[] all2 = new Vector3d[]{looking.rotateYaw(0.10f), looking.rotateYaw(-0.10f)};
            float f = (float) MathHelper.atan2(target.getPosZ() - this.getPosZ(), target.getPosX() - this.getPosX());

            for (int k = 0; k < 6; ++k) {
                float f2 = f + (float) k * (float) Math.PI * 2.0F / 6.0F + ((float) Math.PI * 2F / 5F);
                this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f2) * 2.5D, this.getPosZ() + (double) MathHelper.sin(f2) * 2.5D, d0, d1, f2, 3);
            }
            for (int k = 0; k < 11; ++k) {
                float f3 = f + (float) k * (float) Math.PI * 2.0F / 11.0F + ((float) Math.PI * 2F / 10F);
                this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f3) * 3.5D, this.getPosZ() + (double) MathHelper.sin(f3) * 3.5D, d0, d1, f3, 10);
            }
            for (int k = 0; k < 14; ++k) {
                float f4 = f + (float) k * (float) Math.PI * 2.0F / 14.0F + ((float) Math.PI * 2F / 20F);
                this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f4) * 4.5D, this.getPosZ() + (double) MathHelper.sin(f4) * 4.5D, d0, d1, f4, 15);
            }
            switch (rand.nextInt(3)) {
                case 0:
                    for (Vector3d vector3d : all) {
                        float f0 = (float) MathHelper.atan2(vector3d.z, vector3d.x);
                        for (int l = 0; l < 13; ++l) {
                            double d2 = 1.25D * (double) (l + 1);
                            int j = (int) (0.75f * l);
                            this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f0) * d2, this.getPosZ() + (double) MathHelper.sin(f0) * d2, d0, d1, f0, j);
                        }
                    }
                    break;
                case 1:
                    for (Vector3d vector3d : all2) {
                        float f0 = (float) MathHelper.atan2(vector3d.z, vector3d.x);
                        for (int l = 0; l < 13; ++l) {
                            double d2 = 1.25D * (double) (l + 1);
                            int j = (int) (0.25f * l);
                            this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f0) * d2, this.getPosZ() + (double) MathHelper.sin(f0) * d2, d0, d1, f0, j);
                        }
                    }
                    break;
                case 2:
                    for (int l = 0; l < 13; ++l) {
                        double d2 = 1.25D * (double) (l + 1);
                        int j = (int) (0.5f * l);
                        this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f) * d2, this.getPosZ() + (double) MathHelper.sin(f) * d2, d0, d1, f, j);
                    }
                    break;
                default:
                    break;
            }
        }
    }

    private void RageAttack() {
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            double d0 = Math.min(target.getPosY(), this.getPosY());
            double d1 = Math.max(target.getPosY(), this.getPosY()) + 1.0D;
            Vector3d looking = this.getLookVec();
            Vector3d[] all = new Vector3d[]{looking, looking.rotateYaw(0.3f), looking.rotateYaw(-0.3f), looking.rotateYaw(0.6f), looking.rotateYaw(-0.6f), looking.rotateYaw(0.9f), looking.rotateYaw(-0.9f)};
            for (Vector3d vector3d : all) {
                float f0 = (float) MathHelper.atan2(vector3d.z, vector3d.x);
                for (int l = 0; l < 10; ++l) {
                    double d2 = 1.25D * (double) (l + 1);
                    int j = (int) (0.75f * l);
                    this.spawnFangs(this.getPosX() + (double) MathHelper.cos(f0) * d2, this.getPosZ() + (double) MathHelper.sin(f0) * d2, d0, d1, f0, j);
                }
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

    private void BrokenHelmet() {
        double xx = MathHelper.cos(this.rotationYaw % 360.0F / 180.0F * 3.1415927F) * 0.75F;
        double zz = MathHelper.sin(this.rotationYaw % 360.0F / 180.0F * 3.1415927F) * 0.75F;
        this.world.createExplosion(this, this.getPosX() + xx, this.getPosY() + (double) this.getEyeHeight(), getPosZ() + zz, 2.0F, Explosion.Mode.NONE);
    }

    private void Bulletpattern() {
        LivingEntity target = this.getAttackTarget();
        if (target != null) {
            BlockPos tgt = target.getPosition();
            double tx = tgt.getX();
            double tz = tgt.getZ();
            double ty = target.getPosY() + 0.1;
            if (this.getAnimationTick() == 54) {
                if (!target.isOnGround() && !target.isInWater() && !this.world.getBlockState(tgt.down()).getMaterial().blocksMovement())
                    ty -= 1;
                {
                    BlockPos Pos = this.getPosition();
                    double sx = Pos.getX();
                    double sz = Pos.getZ();
                    Direction dir = Direction.getFacingFromVector(tx - sx, 0, tz - sz);
                    double cx = dir.getXOffset();
                    double cz = dir.getZOffset();
                    double offsetangle = toRadians(6.0);

                    for (int i = -4; i <= 4; i++) {
                        double angle = (i - (4 / 2)) * offsetangle;
                        double x = cx * cos(angle) + cz * sin(angle);
                        double z = -cx * sin(angle) + cz * cos(angle);
                        Ender_Guardian_Bullet_Entity bullet = new Ender_Guardian_Bullet_Entity(world, this, x, this.getPosY() + 2, z);
                        bullet.setShooter(this);
                        bullet.setPosition(getPosX(), getPosY() - 2 + rand.nextDouble() * 4, getPosZ());
                        bullet.setUp(30, cx, 0, cz, tx - 7 * cx + i * cz, ty, tz - 7 * cz + i * cx);
                       this.world.addEntity(bullet);
                    }
                }
            }
        }
    }


    @Override
    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public ItemEntity entityDropItem(ItemStack stack) {
        ItemEntity itementity = this.entityDropItem(stack, 0.0F);
        if (itementity != null) {
            itementity.setMotion(itementity.getMotion().mul(0.0, 3.5, 0.0));
            itementity.setGlowing(true);
            itementity.setNoDespawn();
        }
        return itementity;
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

    @Override
    protected void repelEntities(float x, float y, float z, float radius) {
        super.repelEntities(x, y, z, radius);
    }

    @Override
    public boolean canBePushedByEntity(Entity entity) {
        return false;
    }



    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
        setIsHelmetless(false);
        setUsedMassDestruction(false);
        this.setMaxHealth(CMConfig.EnderguardianHealth, true);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(CMConfig.EnderguardianDamage);
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }


    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        this.playSound(ModSounds.ENDERGUARDIANHURT.get(), 1.0f, 1.0f);
        if (this.getIsHelmetless()) {
            this.playSound(SoundEvents.ENTITY_SHULKER_HURT, 1.0f, 0.8f);
        }
        return null;
    }

    protected SoundEvent getAmbientSound() {
        return this.getIsHelmetless() ? SoundEvents.ENTITY_SHULKER_AMBIENT : null;
    }

    protected SoundEvent getDeathSound() {
        this.playSound(ModSounds.ENDERGUARDIANDEATH.get(), 1.0f, 1.0f);
        if (this.getIsHelmetless()) {
            this.playSound(SoundEvents.ENTITY_SHULKER_DEATH, 2.0f, 0.8f);
        }
        return null;
    }

    @Override
    protected BodyController createBodyController() {
        return new SmartBodyHelper2(this);
    }

    protected PathNavigator createNavigator(World worldIn) {
        return new CMPathNavigateGround(this, world);
    }


    @Override
    public void addTrackingPlayer(ServerPlayerEntity player) {
        super.addTrackingPlayer(player);
        this.bossInfo.addPlayer(player);
    }

    @Override
    public void removeTrackingPlayer(ServerPlayerEntity player) {
        super.removeTrackingPlayer(player);
        this.bossInfo.removePlayer(player);
    }

    class PunchAttackGoal extends Goal {

        public PunchAttackGoal() {
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_LEFT_ATTACK
                    || Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RIGHT_ATTACK
                    || Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_LEFT_STRONG_ATTACK
                    || Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RIGHT_STRONG_ATTACK;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            Ender_Guardian_Entity.this.setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
            LivingEntity target = Ender_Guardian_Entity.this.getAttackTarget();
            if (Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_LEFT_ATTACK) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 17 && target != null || Ender_Guardian_Entity.this.getAnimationTick() > 27 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                    //Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
            if (Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RIGHT_ATTACK) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 22 && target != null || Ender_Guardian_Entity.this.getAnimationTick() > 32 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                   // Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
            if (Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_LEFT_STRONG_ATTACK) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 34 && target != null || Ender_Guardian_Entity.this.getAnimationTick() > 44 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                   // Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
            if (Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RIGHT_STRONG_ATTACK) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 29 && target != null || Ender_Guardian_Entity.this.getAnimationTick() > 39 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                  //  Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
        }
    }

    class StompAttackGoal extends Goal {


        public StompAttackGoal() {
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_STOMP
                    || Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RAGE_STOMP;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            Ender_Guardian_Entity.this.setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
            LivingEntity target = Ender_Guardian_Entity.this.getAttackTarget();
            if(Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_STOMP) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 32 && target != null || Ender_Guardian_Entity.this.getAnimationTick() > 42 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                 //   Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
            if(Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RAGE_STOMP) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 32 && target != null
                        || Ender_Guardian_Entity.this.getAnimationTick() > 42 && Ender_Guardian_Entity.this.getAnimationTick() < 53 && target != null
                        || Ender_Guardian_Entity.this.getAnimationTick() > 58 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                  //  Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
        }
    }

    class BurstAttackGoal extends Goal {


        public BurstAttackGoal() {
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_BURST_ATTACK;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            Ender_Guardian_Entity.this.setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
            LivingEntity target = Ender_Guardian_Entity.this.getAttackTarget();
            if(Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_BURST_ATTACK) {
                if (Ender_Guardian_Entity.this.getAnimationTick() < 27 && target != null || Ender_Guardian_Entity.this.getAnimationTick() > 47 && target != null) {
                    Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
                   // Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
                }
            }
        }
    }

    class UppercutAndBulletGoal extends Goal {

        public UppercutAndBulletGoal() {
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_UPPERCUT_AND_BULLET;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            LivingEntity target = Ender_Guardian_Entity.this.getAttackTarget();
            if (Ender_Guardian_Entity.this.getAnimationTick() < 29 && target != null
                    || Ender_Guardian_Entity.this.getAnimationTick() > 54 && target !=null) {
                Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
            } else {
                Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
              //  Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
            }
            if (Ender_Guardian_Entity.this.getAnimationTick() == 26) {
                float f1 = (float) Math.cos(Math.toRadians(Ender_Guardian_Entity.this.rotationYaw + 90));
                float f2 = (float) Math.sin(Math.toRadians(Ender_Guardian_Entity.this.rotationYaw + 90));
                Ender_Guardian_Entity.this.addVelocity(f1 * 1.5, 0, f2 * 1.5);
            }
            if(Ender_Guardian_Entity.this.getAnimationTick() > 32 || Ender_Guardian_Entity.this.getAnimationTick() < 26){
                Ender_Guardian_Entity.this.setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
            }

            Bulletpattern();

        }
    }

    class RageUppercut extends Goal {


        public RageUppercut() {
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_RAGE_UPPERCUT;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            LivingEntity target = Ender_Guardian_Entity.this.getAttackTarget();
            if (Ender_Guardian_Entity.this.getAnimationTick() < 29 && target != null
                    || Ender_Guardian_Entity.this.getAnimationTick() > 54 && Ender_Guardian_Entity.this.getAnimationTick() < 84 && target != null
                    ||Ender_Guardian_Entity.this.getAnimationTick() > 104 && target !=null) {
                Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
            } else {
                Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
              //  Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
            }
            if (Ender_Guardian_Entity.this.getAnimationTick() == 26) {
                float f1 = (float) Math.cos(Math.toRadians(Ender_Guardian_Entity.this.rotationYaw + 90));
                float f2 = (float) Math.sin(Math.toRadians(Ender_Guardian_Entity.this.rotationYaw + 90));
                Ender_Guardian_Entity.this.addVelocity(f1 * 1.5, 0, f2 * 1.5);
            }
            if(Ender_Guardian_Entity.this.getAnimationTick() > 32 || Ender_Guardian_Entity.this.getAnimationTick() < 26){
                Ender_Guardian_Entity.this.setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
            }

            Bulletpattern();

        }
    }


    class MassDestruction extends Goal {


        public MassDestruction() {
            this.setMutexFlags(EnumSet.of(Flag.JUMP, Flag.LOOK, Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Ender_Guardian_Entity.this.getAnimation() == GUARDIAN_MASS_DESTRUCTION;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            Ender_Guardian_Entity.this.setMotion(0, Ender_Guardian_Entity.this.getMotion().y, 0);
            LivingEntity target = Ender_Guardian_Entity.this.getAttackTarget();
            if (Ender_Guardian_Entity.this.getAnimationTick() < 39 && target!= null || Ender_Guardian_Entity.this.getAnimationTick() > 50 && target!= null) {
                Ender_Guardian_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
            }else{
                Ender_Guardian_Entity.this.rotationYaw = Ender_Guardian_Entity.this.prevRotationYaw;
              //  Ender_Guardian_Entity.this.renderYawOffset = Ender_Guardian_Entity.this.prevRenderYawOffset;
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleStatusUpdate(byte id) {
        if (id == 67) {
            cataclysm.PROXY.onEntityStatus(this, id);
        } else {
            super.handleStatusUpdate(id);
        }
    }
}





