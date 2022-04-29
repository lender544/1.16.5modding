package L_Ender.cataclysm.event;

import L_Ender.cataclysm.entity.Ignis_Entity;
import L_Ender.cataclysm.init.ModEffect;
import L_Ender.cataclysm.init.ModItems;
import L_Ender.cataclysm.items.final_fractal;
import L_Ender.cataclysm.items.zweiender;
import L_Ender.cataclysm.cataclysm;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = cataclysm.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler {

    @SubscribeEvent
    public void onJoinWorld(EntityJoinWorldEvent event) {
        if (event.getWorld().isRemote) {
            return;
        }

    }

    @SubscribeEvent
    public void onLivingDamage(LivingHurtEvent event) {
        World world = event.getEntityLiving().getEntityWorld();
        if (event.getSource() instanceof EntityDamageSource && event.getSource().getTrueSource() instanceof LivingEntity) {
            LivingEntity attacker = (LivingEntity) event.getSource().getTrueSource();
            LivingEntity target = event.getEntityLiving();
            ItemStack weapon = attacker.getHeldItemMainhand();
            if (weapon != null && weapon.getItem() instanceof zweiender) {
                Vector3d lookDir = new Vector3d(target.getLookVec().x, 0, target.getLookVec().z).normalize();
                Vector3d vecBetween = new Vector3d(target.getPosX() - attacker.getPosX(), 0, target.getPosZ() - attacker.getPosZ()).normalize();
                double dot = lookDir.dotProduct(vecBetween);
                if (dot > 0.05) {
                    event.setAmount(event.getAmount() * 2);
                    target.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 0.75F, 0.5F);
                }
            }
            if (weapon != null && weapon.getItem() instanceof final_fractal) {
                event.setAmount(event.getAmount() + target.getMaxHealth() * 0.03f);
            }


        }
    }
    @SubscribeEvent
    public void onLivingAttack(LivingAttackEvent event) {
        World world = event.getEntityLiving().getEntityWorld();
        if (!event.getEntityLiving().getActiveItemStack().isEmpty() && event.getSource() != null && event.getSource().getTrueSource() != null) {
            if (event.getEntityLiving().getActiveItemStack().getItem() == ModItems.BULWARK_OF_THE_FLAME.get()) {
                Entity attacker = event.getSource().getTrueSource();
                if (attacker instanceof LivingEntity) {
                    if (attacker.getDistance(event.getEntityLiving()) <= 4 && !attacker.isBurning()) {
                        attacker.setFire(5);

                    }
                }

            }
        }
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingSetTargetEvent(LivingSetAttackTargetEvent event) {
        if (event.getTarget() != null && event.getEntityLiving() instanceof MobEntity) {
            if (event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
                ((MobEntity) event.getEntityLiving()).setAttackTarget(null);
            }
        }
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getActivePotionEffect(ModEffect.EFFECTSTUN.get()) != null){
            entity.setMotion(entity.getMotion().x, 0.0D, entity.getMotion().z);
        }
    }

    @SubscribeEvent
    public void onPlayerLeftClick(PlayerInteractEvent.LeftClickBlock event) {
        PlayerEntity player = event.getPlayer();
        if (event.isCancelable() && player.isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onUseItem(LivingEntityUseItemEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (event.isCancelable() && living.isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if (event.isCancelable() && living.isPotionActive(ModEffect.EFFECTSTUN.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        LivingEntity living = event.getEntityLiving();
        if (living != null) {
            if (event.isCancelable() && living.isPotionActive(ModEffect.EFFECTSTUN.get())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (event.isCancelable() && event.getPlayer().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickEmpty event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity.getHealth() <= event.getAmount() && entity.isPotionActive(ModEffect.EFFECTSTUN.get())) {
            entity.removeActivePotionEffect(ModEffect.EFFECTSTUN.get());
        }
    }

    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.isCancelable() && event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get())) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (event.getEntityLiving().world.isRemote && (event.getEntityLiving().isPotionActive(ModEffect.EFFECTSTUN.get()))) {
            for (int i = 0; i < 5; i++) {
                float innerAngle = (0.01745329251F * (event.getEntityLiving().renderYawOffset + event.getEntityLiving().ticksExisted * 5) * (i + 1));
                double extraX = 0.5F * MathHelper.sin((float) (Math.PI + innerAngle));
                double extraZ = 0.5F * MathHelper.cos(innerAngle);
                event.getEntityLiving().world.addParticle(ParticleTypes.CRIT, true, event.getEntityLiving().getPosX() + extraX, event.getEntityLiving().getPosYEye() + 0.5F, event.getEntityLiving().getPosZ() + extraZ, 0, 0, 0);
            }
        }
        if (event.getEntityLiving() instanceof Ignis_Entity) {
            if (!event.getEntityLiving().getPassengers().isEmpty() && event.getEntityLiving().getPassengers().get(0).isSneaking()) {
                event.getEntityLiving().getPassengers().get(0).setSneaking(false);
            }
        }
    }
}


