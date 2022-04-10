package L_Ender.cataclysm.items;

import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.init.ModEffect;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class Ignitium_Armor extends ArmorItem {

    public Ignitium_Armor(IArmorMaterial material, EquipmentSlotType slot, Properties properties) {
        super(material, slot, properties);

    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return cataclysm.MODID + ":textures/armor/ignitium_armor" + (slot == EquipmentSlotType.LEGS ? "_legs.png" : ".png");
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
        if (this.slot == EquipmentSlotType.HEAD) {
            return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.ARMOR || enchantment.type == EnchantmentType.ARMOR_HEAD;
        }
        if (this.slot == EquipmentSlotType.CHEST) {
            return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.ARMOR || enchantment.type == EnchantmentType.ARMOR_CHEST;
        }
        if (this.slot == EquipmentSlotType.LEGS) {
            return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.ARMOR;
        }
        if (this.slot == EquipmentSlotType.FEET) {
            return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.ARMOR || enchantment.type == EnchantmentType.ARMOR_FEET;
        }
        return super.canApplyAtEnchantingTable(stack,enchantment);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return (A) cataclysm.PROXY.getArmorModel(slot == EquipmentSlotType.LEGS ? 2 : 1, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {

    }
}