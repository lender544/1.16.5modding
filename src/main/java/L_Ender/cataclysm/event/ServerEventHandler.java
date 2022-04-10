package L_Ender.cataclysm.event;

import L_Ender.cataclysm.init.ModItems;
import L_Ender.cataclysm.items.final_fractal;
import L_Ender.cataclysm.items.zweiender;
import L_Ender.cataclysm.cataclysm;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
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
}


