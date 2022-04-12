package L_Ender.cataclysm.entity;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.AI.CmAttackGoal;
import L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import L_Ender.cataclysm.entity.etc.CMPathNavigateGround;
import L_Ender.cataclysm.entity.etc.GroundPathNavigatorWide;
import L_Ender.cataclysm.entity.etc.SmartBodyHelper2;
import L_Ender.cataclysm.entity.projectile.Lava_Bomb_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModSounds;
import L_Ender.cataclysm.init.ModTag;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.BodyController;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.BossInfo;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Random;

public class Netherite_Monstrosity_Entity extends Boss_monster {

    private final ServerBossInfo bossInfo = (ServerBossInfo) (new ServerBossInfo(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(false);
    public int frame;
    public static final Animation MONSTROSITY_EARTHQUAKE = Animation.create(75);
    public static final Animation MONSTROSITY_CHARGE = Animation.create(82);
    public static final Animation MONSTROSITY_ERUPTIONATTACK = Animation.create(55);
    public static final Animation MONSTROSITY_EARTHQUAKE2 = Animation.create(65);
    public static final Animation MONSTROSITY_EARTHQUAKE3 = Animation.create(70);
    public static final Animation MONSTROSITY_BERSERK = Animation.create(80);
    public static final Animation MONSTROSITY_DEATH = Animation.create(185);
    private static final DataParameter<Boolean> IS_BERSERK = EntityDataManager.createKey(Netherite_Monstrosity_Entity.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Boolean> IS_AWAKEN = EntityDataManager.createKey(Netherite_Monstrosity_Entity.class, DataSerializers.BOOLEAN);
    private int lavabombmagazine = CMConfig.Lavabombmagazine;
    public boolean Blocking = CMConfig.NetheritemonstrosityBodyBloking;
    public float deactivateProgress;
    private int blockBreakCounter;
    public float prevdeactivateProgress;

    public Netherite_Monstrosity_Entity(EntityType entity, World world) {
        super(entity, world);
        this.experienceValue = 300;
        this.stepHeight = 1.75F;
        this.dropAfterDeathAnim = true;
        this.setPathPriority(PathNodeType.LAVA, 8.0F);
        this.setPathPriority(PathNodeType.WATER, -1.0F);
        this.setPathPriority(PathNodeType.DANGER_FIRE, 0.0F);
        this.setPathPriority(PathNodeType.DAMAGE_FIRE, 0.0F);
    }

    @Override
    public Animation[] getAnimations() {
        return new Animation[]{NO_ANIMATION, MONSTROSITY_BERSERK, MONSTROSITY_EARTHQUAKE, MONSTROSITY_CHARGE, MONSTROSITY_EARTHQUAKE2, MONSTROSITY_EARTHQUAKE3, MONSTROSITY_ERUPTIONATTACK, MONSTROSITY_DEATH};
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(0, new BerserkGoal());
        this.goalSelector.addGoal(0, new AwakenGoal());
        this.goalSelector.addGoal(1, new HealGoal());
        this.goalSelector.addGoal(1, new ShootGoal());
        this.goalSelector.addGoal(1, new EarthQuakeGoal());
        this.goalSelector.addGoal(2, new CmAttackGoal(this,1.0));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillagerEntity.class, true));

    }

    public static AttributeModifierMap.MutableAttribute bakeAttributes() {
        return MonsterEntity.func_234295_eP_()
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 50.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25F)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, CMConfig.MonstrosityDamage)
                .createMutableAttribute(Attributes.MAX_HEALTH, CMConfig.MonstrosityHealth)
                .createMutableAttribute(Attributes.ARMOR, 10)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 1.0);
    }

    @Override
    protected void registerData() {
        super.registerData();
        getDataManager().register(IS_BERSERK, false);
        getDataManager().register(IS_AWAKEN, false);
    }

    private static Animation getRandomAttack(Random rand) {
        switch (rand.nextInt(3)) {

            case 0:
                return MONSTROSITY_EARTHQUAKE;
            case 1:
                return MONSTROSITY_EARTHQUAKE2;
            case 2:
                return MONSTROSITY_EARTHQUAKE3;
        }
        return MONSTROSITY_EARTHQUAKE;
    }

    public boolean func_230285_a_(Fluid p_230285_1_) {
        return p_230285_1_.isIn(FluidTags.LAVA);
    }

    public void writeAdditional(CompoundNBT compound) {
        super.writeAdditional(compound);
        compound.putBoolean("is_Berserk", getIsBerserk());
        compound.putBoolean("is_Awaken", getIsAwaken());
    }

    public void readAdditional(CompoundNBT compound) {
        super.readAdditional(compound);
        setIsBerserk(compound.getBoolean("is_Berserk"));
        setIsAwaken(compound.getBoolean("is_Awaken"));
        if (this.hasCustomName()) {
            this.bossInfo.setName(this.getDisplayName());
        }
    }

    public void setIsBerserk(boolean isBerserk) {
        getDataManager().set(IS_BERSERK, isBerserk);
    }

    public boolean getIsBerserk() {
        return getDataManager().get(IS_BERSERK);
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
        if (this.getAnimation() == MONSTROSITY_BERSERK && !source.canHarmInCreative()) {
            return false;
        }

        double range = calculateRange(source);

        if (range > CMConfig.MonstrosityLongRangelimit * CMConfig.MonstrosityLongRangelimit) {
            return false;
        }

        if (!source.canHarmInCreative()) {
            damage = Math.min(CMConfig.MonstrosityDamageCap, damage);
        }
        Entity entity = source.getImmediateSource();
        if (entity instanceof GolemEntity) {
            damage *= 0.5;
        }
        boolean attack = super.attackEntityFrom(source, damage);

        if(attack &&!this.getIsAwaken() ){
            this.setIsAwaken(true);
        }

        return attack;
    }

    public boolean func_241845_aY() {
        return this.isAlive() && Blocking;
    }

    public boolean canBePushed() {
        return false;
    }

    public boolean onLivingFall(float distance, float damageMultiplier) {
        return false;
    }

    private void func_234318_eL() {
        if (this.isInLava()) {
            ISelectionContext lvt_1_1_ = ISelectionContext.forEntity(this);
            if (lvt_1_1_.func_216378_a(FlowingFluidBlock.LAVA_COLLISION_SHAPE, this.getPosition().down(), true) && !this.world.getFluidState(this.getPosition().up()).isTagged(FluidTags.LAVA)) {
                this.onGround = true;
            } else {
                this.setMotion(this.getMotion().scale(0.5D).add(0.0D, rand.nextFloat() * 0.5, 0.0D));
            }
        }

    }

    public void tick() {
        super.tick();
        this.func_234318_eL();
        rotationYaw = renderYawOffset;
        if (!this.isSilent() && !world.isRemote && this.getIsAwaken()) {
            this.world.setEntityState(this, (byte) 67);
        }

        frame++;
        float moveX = (float) (getPosX() - prevPosX);
        float moveZ = (float) (getPosZ() - prevPosZ);
        float speed = MathHelper.sqrt(moveX * moveX + moveZ * moveZ);
        if (!this.isSilent() && frame % 25 == 1 && speed > 0.05 && this.getIsAwaken()) {
            playSound(ModSounds.MONSTROSITYSTEP.get(), 1F, 1.0f);
        }
        this.bossInfo.setPercent(this.getHealth() / this.getMaxHealth());
        prevdeactivateProgress = deactivateProgress;
        if (!this.getIsAwaken() && deactivateProgress < 40F) {
            deactivateProgress = 40;
        }
        if (this.getIsAwaken() && deactivateProgress > 0F) {
            deactivateProgress--;
            if(deactivateProgress == 20 && this.getHealth() > 0){
                this.playSound(ModSounds.MONSTROSITYAWAKEN.get(), 10, 1);
            }
        }
        AnimationHandler.INSTANCE.updateAnimations(this);
        LivingEntity target = this.getAttackTarget();

        if (this.getAnimation() == MONSTROSITY_EARTHQUAKE && this.getAnimationTick() == 34
                || this.getAnimation() == MONSTROSITY_EARTHQUAKE2 && this.getAnimationTick() == 24
                || this.getAnimation() == MONSTROSITY_EARTHQUAKE3 && this.getAnimationTick() == 29 ){
            EarthQuake();
            ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 20, 0.3f, 0, 20);
            Makeparticle(4.75f,2.5f);
            Makeparticle(4.75f,-2.5f);
        }
        BlockBreaking();
        if(deactivateProgress == 0 && this.isAlive()) {
            if(!isAIDisabled() && this.getAnimation() == NO_ANIMATION && this.isBerserk() && !this.getIsBerserk()){
                this.setAnimation(MONSTROSITY_BERSERK);
            }else if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && target != null && target.isAlive()) {
                if (this.isInLava() && this.lavabombmagazine == 0) {
                    this.setAnimation(MONSTROSITY_CHARGE);
                }
                else if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && this.getDistance(target) >= 18F && this.getDistance(target) < 40F && this.lavabombmagazine > 0 && this.rand.nextInt(48) == 0 || this.getDistance(target) > 4.75F && rand.nextFloat() * 100.0F < 0.3F && this.getDistance(target) < 18F && this.lavabombmagazine > 0) {
                    this.setAnimation(MONSTROSITY_ERUPTIONATTACK);

                }
                else if (!isAIDisabled() && this.getAnimation() == NO_ANIMATION && this.getDistance(target) < 6) {
                    Animation animation = getRandomAttack(rand);
                    if (this.isBerserk()) {
                        this.setAnimation(MONSTROSITY_EARTHQUAKE2);
                    } else {
                        this.setAnimation(animation);
                    }
                }
            }
        }

        if (this.getAnimation() == MONSTROSITY_CHARGE) {
                if (this.getAnimationTick() == 34) {
                    this.lavabombmagazine = CMConfig.Lavabombmagazine;
                    this.doAbsorptionEffects(4,1,4);
                    this.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 6f, 0.5F);
                    this.heal(15F);
                }
                if (this.getAnimationTick() == 44) {
                    this.doAbsorptionEffects(8,2,8);
                    this.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 6f, 0.5F);
                    this.heal(15F);
                }
                if (this.getAnimationTick() == 54) {
                    this.doAbsorptionEffects(16,4,16);
                    this.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 6f, 0.5F);
                    this.heal(15F);
            }
        }
        if (this.getAnimation() == MONSTROSITY_BERSERK) {
            Netherite_Monstrosity_Entity.this.setIsBerserk(true);
            if (this.getAnimationTick() == 20) {
                this.playSound(ModSounds.MONSTROSITYGROWL.get(), 3, 1);
            }
            if (this.getAnimationTick() == 29) {
                berserkBlockBreaking(8,8,8);
                ScreenShake_Entity.ScreenShake(world, this.getPositionVec(), 20, 0.3f, 0, 20);
                EarthQuake();
                Makeparticle(4.0f,3.5f);
                Makeparticle(4.0f,-3.5f);
            }
        }
        if (!world.isRemote) {
            if (!this.getIsAwaken() && target != null) {
                this.setIsAwaken(true);
            }
        }
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
        setMotion(0, Netherite_Monstrosity_Entity.this.getMotion().y, 0);
        if (this.deathTime == 68) {
            this.playSound(ModSounds.MONSTROSITYLAND.get(), 1, 1);
        }

    }

    private void doAbsorptionEffects(int x, int y, int z) {

        int MthX = MathHelper.floor(this.getPosX());
        int MthY = MathHelper.floor(this.getPosY());
        int MthZ = MathHelper.floor(this.getPosZ());
        if (!world.isRemote) {
            for (int k2 = -x; k2 <= x; ++k2) {
            for (int l2 = -z; l2 <= z; ++l2) {
                for (int j = -y; j <= y; ++j) {
                    int i3 = MthX + k2;
                    int k = MthY + j;
                    int l = MthZ + l2;
                    BlockPos blockpos = new BlockPos(i3, k, l);
                    this.doAbsorptionEffect(blockpos);
                }
            }
        }
        }
    }

    private void doAbsorptionEffect(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
       // if (state.getFluidState().isTagged(FluidTags.LAVA) && state.getFluidState().isSource()) {
      //      this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
      //  }
        if (state.getMaterial() == Material.LAVA) {
            this.world.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    private void EarthQuake() {
        this.playSound(SoundEvents.ENTITY_GENERIC_EXPLODE, 1.5f, 1F + this.getRNG().nextFloat() * 0.1F);
        for (LivingEntity entity : this.world.getEntitiesWithinAABB(LivingEntity.class, this.getBoundingBox().grow(7.0D))) {
            if (!isOnSameTeam(entity) && !(entity instanceof Netherite_Monstrosity_Entity) && entity != this) {
                boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
                if (entity instanceof PlayerEntity && entity.isActiveItemStackBlocking()) {
                    disableShield(entity, 120);
                }
                if (flag) {
                    launch(entity, true);
                    if (getIsBerserk()) {
                        entity.setFire(6);
                    }
                }
            }
        }
    }

    private void Makeparticle(float vec, float math) {
        if (this.world.isRemote) {
            for (int i1 = 0; i1 < 80 + rand.nextInt(12); i1++) {
                double motionX = getRNG().nextGaussian() * 0.07D;
                double motionY = getRNG().nextGaussian() * 0.07D;
                double motionZ = getRNG().nextGaussian() * 0.07D;
                float f = MathHelper.cos(this.rotationYaw * ((float)Math.PI / 180F)) ;
                float f1 = MathHelper.sin(this.rotationYaw * ((float)Math.PI / 180F)) ;
                float angle = (0.01745329251F * this.renderYawOffset) + i1;
                double extraX = 2F * MathHelper.sin((float) (Math.PI + angle));
                double extraY = 0.3F;
                double extraZ = 2F * MathHelper.cos(angle);
                double theta = (renderYawOffset) * (Math.PI / 180);
                theta += Math.PI / 2;
                double vecX = Math.cos(theta);
                double vecZ = Math.sin(theta);
                int hitX = MathHelper.floor(getPosX() + vec * vecX+ extraX);
                int hitY = MathHelper.floor(getPosY());
                int hitZ = MathHelper.floor(getPosZ() + vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = world.getBlockState(hit.down());
                if (getIsBerserk()) {
                    this.world.addParticle(ParticleTypes.FLAME, getPosX() + vec * vecX + extraX + f * math, this.getPosY() + extraY, getPosZ() + vec * vecZ + extraZ + f1 * math, motionX, motionY, motionZ);
                } else {
                    this.world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), getPosX() + vec * vecX + extraX + f * math, this.getPosY() + extraY, getPosZ() + vec * vecZ + extraZ + f1 * math, motionX, motionY, motionZ);
                }
            }
        }
    }


    private void launch(Entity e, boolean huge) {
        double d0 = e.getPosX() - this.getPosX();
        double d1 = e.getPosZ() - this.getPosZ();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
        float f = huge ? 2F : 0.5F;
        e.addVelocity(d0 / d2 * f, huge ? 0.75D : 0.2F, d1 / d2 * f);
    }


    private void berserkBlockBreaking(int x, int y, int z) {
        int MthX = MathHelper.floor(this.getPosX());
        int MthY = MathHelper.floor(this.getPosY());
        int MthZ = MathHelper.floor(this.getPosZ());
        if (!this.world.isRemote) {
            if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
            for (int k2 = -x; k2 <= x; ++k2) {
                for (int l2 = -z; l2 <= z; ++l2) {
                    for (int j = 0; j <= y; ++j) {
                        int i3 = MthX + k2;
                        int k = MthY + j;
                        int l = MthZ + l2;
                        Block block = world.getBlockState(new BlockPos(i3, k, l)).getBlock();
                        TileEntity tileEntity = world.getTileEntity(new BlockPos(i3, k, l));
                        if (block != Blocks.AIR && !BlockTags.getCollection().get(ModTag.NETHERITE_MONSTROSITY_IMMUNE).contains(block)) {
                            if (tileEntity == null && rand.nextInt(4) + 1 == 4) {
                                FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(world, i3 + 0.5D, k + 0.5D, l + 0.5D, block.getDefaultState());
                                fallingBlockEntity.setMotion(fallingBlockEntity.getMotion().add(this.getPositionVec().subtract(fallingBlockEntity.getPositionVec()).mul((-1.2D + rand.nextDouble()) / 3, (-1.1D + rand.nextDouble()) / 3, (-1.2D + rand.nextDouble()) / 3)));
                                world.addEntity(fallingBlockEntity);
                            } else {
                                world.destroyBlock(new BlockPos(i3, k, l), shouldDropItem(tileEntity));
                            }
                        }
                    }
                }
            }
            }
        }
    }

    private void BlockBreaking() {
        if (this.blockBreakCounter > 0) {
            --this.blockBreakCounter;
            return;
        }

        if (!this.world.isRemote && this.blockBreakCounter == 0) {
            if (ForgeEventFactory.getMobGriefingEvent(this.world, this)) {
                for (int a = (int) Math.round(this.getBoundingBox().minX); a <= (int) Math.round(this.getBoundingBox().maxX); a++) {
                    for (int b = (int) Math.round(this.getBoundingBox().minY); (b <= (int) Math.round(this.getBoundingBox().maxY) + 1) && (b <= 127); b++) {
                        for (int c = (int) Math.round(this.getBoundingBox().minZ); c <= (int) Math.round(this.getBoundingBox().maxZ); c++) {
                            BlockPos blockpos = new BlockPos(a, b, c);
                            Block block = world.getBlockState(new BlockPos(blockpos)).getBlock();
                            TileEntity tileEntity = world.getTileEntity(blockpos);
                            if (block != Blocks.AIR && BlockTags.getCollection().get(ModTag.NETHERITE_MONSTROSITY_BREAK).contains(block)) {
                                boolean flag = world.destroyBlock(new BlockPos(a, b, c), shouldDropItem(tileEntity));
                                if (flag) {
                                    blockBreakCounter = 10;
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private boolean shouldDropItem(TileEntity tileEntity) {
        if (tileEntity == null) {
            return rand.nextInt(3) + 1 == 3;
        }
        return true;
    }

    public boolean isBerserk() {
        return this.getHealth() <= this.getMaxHealth() / 3.0F;
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

    @Nullable
    @Override
    public ILivingEntityData onInitialSpawn(IServerWorld world, DifficultyInstance difficulty, SpawnReason reason, @Nullable ILivingEntityData livingData, @Nullable CompoundNBT compound) {
        setIsBerserk(false);
        this.setMaxHealth(CMConfig.MonstrosityHealth, true);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(CMConfig.MonstrosityDamage);
        return super.onInitialSpawn(world, difficulty, reason, livingData, compound);
    }


    public void travel(Vector3d travelVector) {
        this.setAIMoveSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED) * (isInLava() ? 0.2F : 1F));
        if (this.isServerWorld() && this.isInLava()) {
            this.moveRelative(this.getAIMoveSpeed(), travelVector);
            this.move(MoverType.SELF, this.getMotion());
            this.setMotion(this.getMotion().scale(0.9D));
        }else{
            super.travel(travelVector);
        }
    }

    protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
        return ModSounds.MONSTROSITYHURT.get();
    }

    protected SoundEvent getDeathSound() {
        return ModSounds.MONSTROSITYDEATH.get();
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

    @Nullable
    public Animation getDeathAnimation()
    {
        return MONSTROSITY_DEATH;
    }

    class EarthQuakeGoal extends Goal {

        public EarthQuakeGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        public boolean shouldExecute() {
            return Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_EARTHQUAKE || Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_EARTHQUAKE2 || Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_EARTHQUAKE3;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        public void tick() {
            LivingEntity target = Netherite_Monstrosity_Entity.this.getAttackTarget();
            Netherite_Monstrosity_Entity.this.setMotion(0, Netherite_Monstrosity_Entity.this.getMotion().y, 0);
                //I wanted to clear this code, but I didn't because I was too lazy.
            if (Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_EARTHQUAKE) {
                if (Netherite_Monstrosity_Entity.this.getAnimationTick() < 34 && target !=null || Netherite_Monstrosity_Entity.this.getAnimationTick() > 54 && target !=null) {
                    Netherite_Monstrosity_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Netherite_Monstrosity_Entity.this.rotationYaw = Netherite_Monstrosity_Entity.this.prevRotationYaw;
                  //  Netherite_Monstrosity_Entity.this.renderYawOffset = Netherite_Monstrosity_Entity.this.prevRenderYawOffset;
                }

            }
            if (Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_EARTHQUAKE2) {
                if (Netherite_Monstrosity_Entity.this.getAnimationTick() < 24  && target !=null || Netherite_Monstrosity_Entity.this.getAnimationTick() > 44  && target !=null) {
                    Netherite_Monstrosity_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Netherite_Monstrosity_Entity.this.rotationYaw = Netherite_Monstrosity_Entity.this.prevRotationYaw;
                    //Netherite_Monstrosity_Entity.this.renderYawOffset = Netherite_Monstrosity_Entity.this.prevRenderYawOffset;
                }

            }

            if (Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_EARTHQUAKE3) {
                if (Netherite_Monstrosity_Entity.this.getAnimationTick() < 29 && target !=null || Netherite_Monstrosity_Entity.this.getAnimationTick() > 49 && target !=null) {
                    Netherite_Monstrosity_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Netherite_Monstrosity_Entity.this.rotationYaw = Netherite_Monstrosity_Entity.this.prevRotationYaw;
                    //Netherite_Monstrosity_Entity.this.renderYawOffset = Netherite_Monstrosity_Entity.this.prevRenderYawOffset;
                }
            }
        }
    }



    class ShootGoal extends Goal {


        public ShootGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_ERUPTIONATTACK;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        @Override
        public void tick() {
            LivingEntity target = Netherite_Monstrosity_Entity.this.getAttackTarget();
            Netherite_Monstrosity_Entity.this.setMotion(0, Netherite_Monstrosity_Entity.this.getMotion().y, 0);
            double lavabombcount = CMConfig.Lavabombamount;

            if(target !=null) {
                Netherite_Monstrosity_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                if (Netherite_Monstrosity_Entity.this.getAnimationTick() == 30) {
                    Netherite_Monstrosity_Entity.this.playSound(ModSounds.MONSTROSITYSHOOT.get(), 3, 0.75f);
                    Netherite_Monstrosity_Entity.this.lavabombmagazine--;
                    for (int i = 0; i < lavabombcount; ++i) {
                        Lava_Bomb_Entity lava = new Lava_Bomb_Entity(ModEntities.LAVA_BOMB.get(), Netherite_Monstrosity_Entity.this.world, Netherite_Monstrosity_Entity.this);
                        double d0 = target.getPosX() - Netherite_Monstrosity_Entity.this.getPosX();
                        double d1 = target.getBoundingBox().minY + target.getHeight() / 3.0F - lava.getPosY();
                        double d2 = target.getPosZ() - Netherite_Monstrosity_Entity.this.getPosZ();
                        double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
                        lava.shoot(d0, d1 + d3 * 0.20000000298023224D, d2, 1.0F, 24 - Netherite_Monstrosity_Entity.this.world.getDifficulty().getId() * 4);
                        Netherite_Monstrosity_Entity.this.world.addEntity(lava);
                    }
                }
            }
        }
    }

    class BerserkGoal extends Goal {

        public BerserkGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_BERSERK;
        }

        @Override
        public void resetTask() {
            super.resetTask();
        }

        @Override
        public void tick() {
            Netherite_Monstrosity_Entity.this.setMotion(0, Netherite_Monstrosity_Entity.this.getMotion().y, 0);
            LivingEntity target = Netherite_Monstrosity_Entity.this.getAttackTarget();
            if (target!= null) {
                Netherite_Monstrosity_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);

            }
        }
    }

    class HealGoal extends Goal {

        public HealGoal() {
            this.setMutexFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.LOOK, Goal.Flag.MOVE));
        }

        @Override
        public boolean shouldExecute() {
            return Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_CHARGE;
        }

        @Override
        public void tick() {
            LivingEntity target = Netherite_Monstrosity_Entity.this.getAttackTarget();
            Netherite_Monstrosity_Entity.this.setMotion(0, Netherite_Monstrosity_Entity.this.getMotion().y, 0);
            if (Netherite_Monstrosity_Entity.this.getAnimation() == MONSTROSITY_CHARGE && target!=null){
                if (Netherite_Monstrosity_Entity.this.getAnimationTick() < 34 || Netherite_Monstrosity_Entity.this.getAnimationTick() > 72) {
                    Netherite_Monstrosity_Entity.this.getLookController().setLookPositionWithEntity(target, 30.0F, 30.0F);
                } else {
                    Netherite_Monstrosity_Entity.this.rotationYaw = Netherite_Monstrosity_Entity.this.prevRotationYaw;
                   // Netherite_Monstrosity_Entity.this.renderYawOffset = Netherite_Monstrosity_Entity.this.prevRenderYawOffset;
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
            Netherite_Monstrosity_Entity.this.setMotion(0, Netherite_Monstrosity_Entity.this.getMotion().y, 0);
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





