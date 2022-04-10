package L_Ender.cataclysm.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class zweiender extends SwordItem {

    public zweiender(IItemTier toolMaterial, Properties props) {
        super(toolMaterial, 3, -2.4f, props);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        super.setDamage(stack, 0);
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStackMaterial) {
        return false;
    }


    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.WEAPON;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.cataclysm.zweiender.desc").mergeStyle(TextFormatting.DARK_GREEN));
        tooltip.add(new TranslationTextComponent("item.cataclysm.wip.desc").mergeStyle(TextFormatting.DARK_GREEN));
    }
}




