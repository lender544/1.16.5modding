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
import net.minecraft.item.Item;
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

public class Monstrous_Helm extends ArmorItem {

    public Monstrous_Helm(IArmorMaterial material, EquipmentSlotType slot, Item.Properties properties) {
        super(material, slot, properties);

    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlotType slot, String type) {
        return cataclysm.MODID + ":textures/armor/monstrous_helm.png";
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
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        boolean berserk = player.getMaxHealth() * 1 / 2 >= player.getHealth();
        double radius = 4.0D;
        double xx = MathHelper.cos(player.rotationYaw % 360.0F / 180.0F * 3.1415927F) * 0.75F;
        double zz = MathHelper.sin(player.rotationYaw % 360.0F / 180.0F * 3.1415927F) * 0.75F;
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(radius));
        if(berserk && !(player.getCooldownTracker().hasCooldown(this))) {
           // player.playSound(SoundEvents.ENTITY_RAVAGER_ROAR, 0.75F, 0.5F);
            for (Entity entity : list) {
                if (entity instanceof LivingEntity) {
                    entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE)* 1/2);
                    double d0 = entity.getPosX() - player.getPosX();
                    double d1 = entity.getPosZ() - player.getPosZ();
                    double d2 = Math.max(d0 * d0 + d1 * d1, 0.001D);
                    entity.addVelocity(d0 / d2 * 1.5 , 0.15D, d1 / d2 * 1.5);
                }
            }
            world.createExplosion(player, player.getPosX() + xx, player.getPosY() + (double) player.getEyeHeight(), player.getPosZ() + zz, 1.5F, Explosion.Mode.NONE);
            player.getCooldownTracker().setCooldown(this, 350);
            player.addPotionEffect(new EffectInstance(ModEffect.EFFECTMONSTROUS.get(), 200, 0, false, true));
        }
    }


    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {

        return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.ARMOR || enchantment.type == EnchantmentType.ARMOR_HEAD;

    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public <A extends BipedModel<?>> A getArmorModel(LivingEntity entityLiving, ItemStack stack, EquipmentSlotType armorSlot, A _default) {
        return (A) cataclysm.PROXY.getArmorModel(0, entityLiving);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.cataclysm.monstrous_helm.desc").mergeStyle(TextFormatting.DARK_GREEN));
        tooltip.add(new TranslationTextComponent("item.cataclysm.monstrous_helm2.desc").mergeStyle(TextFormatting.DARK_GREEN));
    }
}