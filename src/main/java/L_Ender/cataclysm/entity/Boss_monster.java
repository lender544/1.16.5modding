package L_Ender.cataclysm.entity;

import L_Ender.cataclysm.init.ModEffect;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.AnimationHandler;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class Boss_monster extends MonsterEntity implements IAnimatedEntity {
    private int animationTick;
    private Animation currentAnimation;
    protected boolean dropAfterDeathAnim = true;
    private int killDataRecentlyHit;
    private DamageSource killDataCause;
    private PlayerEntity killDataAttackingPlayer;

    @OnlyIn(Dist.CLIENT)
    public Vector3d[] socketPosArray;

    public Boss_monster(EntityType entity, World world) {
        super(entity, world);
        if (world.isRemote) {
            socketPosArray = new Vector3d[]{};
        }
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float damage) {
        boolean attack = super.attackEntityFrom(source, damage);
        if (attack) {
            if (getHealth() <= 0.0F) {
                AnimationHandler.INSTANCE.sendAnimationMessage(this, getDeathAnimation());
            }
        }
        return attack;
    }

    double calculateRange(DamageSource damagesource) {
        return damagesource.getTrueSource() != null ? getDistanceSq(damagesource.getTrueSource()) : -1;
    }

    public double getAngleBetweenEntities(Entity first, Entity second) {
        return Math.atan2(second.getPosZ() - first.getPosZ(), second.getPosX() - first.getPosX()) * (180 / Math.PI) + 90;
    }

    public  List<LivingEntity> getEntityLivingBaseNearby(double distanceX, double distanceY, double distanceZ, double radius) {
        return getEntitiesNearby(LivingEntity.class, distanceX, distanceY, distanceZ, radius);
    }

    public <T extends Entity> List<T> getEntitiesNearby(Class<T> entityClass, double dX, double dY, double dZ, double r) {
        return world.getEntitiesWithinAABB(entityClass, getBoundingBox().grow(dX, dY, dZ), e -> e != this && getDistance(e) <= r + e.getWidth() / 2f && e.getPosY() <= getPosY() + dY);
    }

    public static void disableShield(LivingEntity livingEntity, int ticks) {
        ((PlayerEntity)livingEntity).getCooldownTracker().setCooldown(livingEntity.getActiveItemStack().getItem(), ticks);
        livingEntity.resetActiveHand();
        livingEntity.world.setEntityState(livingEntity, (byte)30);
    }

    public static void entityHitAngle(LivingEntity livingEntity, int arc, int range) {

    }

    protected void onAnimationFinish(Animation animation) {}

    @Override
    public void baseTick() {
        super.baseTick();
        if (getHealth() <= 0.0F) {
            Animation death;
            if ((death = getDeathAnimation()) != null) {
                onDeathUpdate(death.getDuration() - 20);
            } else {
                onDeathUpdate(20);
            }
        }
    }

    protected void onDeathAIUpdate() {}

    @Override
    protected final void onDeathUpdate() {}

    private void onDeathUpdate(int deathDuration) { // TODO copy from entityLiving
        onDeathAIUpdate();

        ++this.deathTime;
        if (this.deathTime == deathDuration) {
            attackingPlayer = killDataAttackingPlayer;
            recentlyHit = killDataRecentlyHit;
            if (!world.isRemote() && dropAfterDeathAnim && killDataCause != null) {
                spawnDrops(killDataCause);
            }

            this.remove(false);

            for(int i = 0; i < 20; ++i) {
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                double d2 = this.rand.nextGaussian() * 0.02D;
                this.world.addParticle(ParticleTypes.POOF, this.getPosXRandom(1.0D), this.getPosYRandom(), this.getPosZRandom(1.0D), d0, d1, d2);
            }
        }
    }

    @Override
    public void onDeath(DamageSource cause) // TODO copy from entityLiving
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.dead) {
            Entity entity = cause.getTrueSource();
            LivingEntity livingentity = this.getAttackingEntity();
            if (this.scoreValue >= 0 && livingentity != null) {
                livingentity.awardKillScore(this, this.scoreValue, cause);
            }

            if (this.isSleeping()) {
                this.wakeUp();
            }

            this.dead = true;
            this.getCombatTracker().reset();
            if (this.world instanceof ServerWorld) {
                if (entity != null) {
                    entity.onKillEntity((ServerWorld)this.world, this);
                }

                if (!dropAfterDeathAnim)
                    this.spawnDrops(cause);
                this.createWitherRose(livingentity);
            }
            killDataCause = cause;
            killDataRecentlyHit = this.recentlyHit;
            killDataAttackingPlayer = attackingPlayer;

            this.world.setEntityState(this, (byte)3);
            this.setPose(Pose.DYING);
        }
    }

    public void circleEntity(Entity target, float radius, float speed, boolean direction, int circleFrame, float offset, float moveSpeedMultiplier) {
        int directionInt = direction ? 1 : -1;
        double t = directionInt * circleFrame * 0.5 * speed / radius + offset;
        Vector3d movePos = target.getPositionVec().add(radius * Math.cos(t), 0, radius * Math.sin(t));
        this.getNavigator().tryMoveToXYZ(movePos.getX(), movePos.getY(), movePos.getZ(), speed * moveSpeedMultiplier);
    }

    protected void repelEntities(float x, float y, float z, float radius) {
        List<LivingEntity> nearbyEntities = getEntityLivingBaseNearby(x, y, z, radius);
        for (Entity entity : nearbyEntities) {
            if (entity.canBeCollidedWith() && !entity.noClip) {
                double angle = (getAngleBetweenEntities(this, entity) + 90) * Math.PI / 180;
                entity.setMotion(-0.1 * Math.cos(angle), entity.getMotion().y, -0.1 * Math.sin(angle));
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void setSocketPosArray(int index, Vector3d pos) {
        if (socketPosArray != null && socketPosArray.length > index) {
            socketPosArray[index] = pos;
        }
    }

    public boolean canBePushedByEntity(Entity entity) {
        return true;
    }

    // TODO: Copied from parent classes
    @Override
    public void applyEntityCollision(Entity entityIn) {
        if (!this.isSleeping()) {
            if (!this.isRidingSameEntity(entityIn)) {
                if (!entityIn.noClip && !this.noClip) {
                    double d0 = entityIn.getPosX() - this.getPosX();
                    double d1 = entityIn.getPosZ() - this.getPosZ();
                    double d2 = MathHelper.absMax(d0, d1);
                    if (d2 >= (double)0.01F) {
                        d2 = (double)MathHelper.sqrt(d2);
                        d0 = d0 / d2;
                        d1 = d1 / d2;
                        double d3 = 1.0D / d2;
                        if (d3 > 1.0D) {
                            d3 = 1.0D;
                        }

                        d0 = d0 * d3;
                        d1 = d1 * d3;
                        d0 = d0 * (double)0.05F;
                        d1 = d1 * (double)0.05F;
                        d0 = d0 * (double)(1.0F - this.entityCollisionReduction);
                        d1 = d1 * (double)(1.0F - this.entityCollisionReduction);
                        if (!this.isBeingRidden()) {
                            if (canBePushedByEntity(entityIn)) {
                                this.addVelocity(-d0, 0.0D, -d1);
                            }
                        }

                        if (!entityIn.isBeingRidden()) {
                            entityIn.addVelocity(d0, 0.0D, d1);
                        }
                    }

                }
            }
        }
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
        if (animation == NO_ANIMATION) {
            onAnimationFinish(this.currentAnimation);
        }
        this.currentAnimation = animation;
        setAnimationTick(0);
    }

    public boolean isPotionApplicable(EffectInstance potioneffectIn) {
        return potioneffectIn.getPotion() == ModEffect.EFFECTSTUN.get() ? false : super.isPotionApplicable(potioneffectIn);
    }

    @Nullable
    public Animation getDeathAnimation()
    {
        return null;
    }

    public boolean canDespawn(double distanceToClosestPlayer) {
        return false;
    }

    protected boolean isDespawnPeaceful() {
        return false;
    }

    protected boolean canBeRidden(Entity entityIn) {
        return false;
    }
}
