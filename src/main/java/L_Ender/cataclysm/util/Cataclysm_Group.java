package L_Ender.cataclysm.util;

import L_Ender.cataclysm.init.ModItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class Cataclysm_Group extends ItemGroup {

    public Cataclysm_Group(String label) {
        super(label);
    }

    @Override
    public ItemStack createIcon() {
        return ModItems.FINAL_FRACTAL.get().getDefaultInstance();
    }

    @Override
    public void fill(NonNullList<ItemStack> items) {
        super.fill(items);
    }
}

