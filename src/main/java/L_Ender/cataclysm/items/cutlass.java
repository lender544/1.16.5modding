package L_Ender.cataclysm.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class cutlass extends SwordItem {
    public cutlass(IItemTier toolMaterial, Properties props) {
        super(toolMaterial, 4, -2f, props);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int itemSlot, boolean isSelected) {
        if (entity instanceof LivingEntity) {
            LivingEntity living = (LivingEntity) entity;
            if (living.getHeldItemMainhand() == stack || living.getHeldItemOffhand() == stack) {
                living.addPotionEffect(new EffectInstance(Effects.SPEED, 1, 0));
            }
        }
    }
}






