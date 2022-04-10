package L_Ender.cataclysm.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Bulwark_of_the_flame extends Item {
    public Bulwark_of_the_flame(Item.Properties group) {
        super(group);
    }

    public boolean isShield(ItemStack stack, @Nullable LivingEntity entity) {
        return true;
    }

    public UseAction getUseAction(ItemStack p_77661_1_) {
        return UseAction.BLOCK;
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }

    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        ItemStack lvt_4_1_ = player.getHeldItem(hand);
        player.setActiveHand(hand);
        return ActionResult.resultConsume(lvt_4_1_);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.cataclysm.wip.desc").mergeStyle(TextFormatting.DARK_GREEN));
    }
}