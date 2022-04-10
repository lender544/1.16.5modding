package L_Ender.cataclysm.util;

import L_Ender.cataclysm.entity.projectile.Void_Scatter_Arrow_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModItems;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IPosition;
import net.minecraft.dispenser.ProjectileDispenseBehavior;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Modcompat {

    public static void registerDispenserBehaviors() {
        DispenserBlock.registerDispenseBehavior(ModItems.VOID_SCATTER_ARROW.get(), new ProjectileDispenseBehavior() {
            @Override
            protected ProjectileEntity getProjectileEntity(World world, IPosition position, ItemStack stack) {
                return new Void_Scatter_Arrow_Entity(ModEntities.VOID_SCATTER_ARROW.get(), position.getX(), position.getY(), position.getZ(), world);
            }
        });
    }
}
