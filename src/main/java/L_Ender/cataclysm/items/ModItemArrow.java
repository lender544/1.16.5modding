package L_Ender.cataclysm.items;

import L_Ender.cataclysm.entity.projectile.Void_Scatter_Arrow_Entity;
import L_Ender.cataclysm.init.ModItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModItemArrow extends ArrowItem {
    public ModItemArrow(Properties group) {
        super(group);
    }

    public AbstractArrowEntity createArrow(World worldIn, ItemStack stack, LivingEntity shooter) {
        if(this == ModItems.VOID_SCATTER_ARROW.get()){
            ArrowEntity arrowentity = new Void_Scatter_Arrow_Entity(worldIn, shooter);
            arrowentity.setPotionEffect(stack);
            return arrowentity;
        }else {
            return super.createArrow(worldIn, stack, shooter);
        }
    }

}
