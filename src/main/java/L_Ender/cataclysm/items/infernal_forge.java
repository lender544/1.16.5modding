package L_Ender.cataclysm.items;


import L_Ender.cataclysm.init.ModSounds;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class infernal_forge extends PickaxeItem {
    public infernal_forge(IItemTier toolMaterial, Properties props) {

        super(toolMaterial, 8, -3.2f, props);
    }

    @Override
    public boolean hitEntity(ItemStack heldItemStack, LivingEntity target, LivingEntity attacker) {
        if (!target.world.isRemote) {
            target.playSound(ModSounds.HAMMERTIME.get(), 0.5F, 0.5F);
            target.applyKnockback( 1F, attacker.getPosX() - target.getPosX(), attacker.getPosZ() - target.getPosZ());
        }
        return true;
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        ItemStack stack = context.getItem();
        PlayerEntity player = context.getPlayer();
        if (player.getHeldItemMainhand() == stack) {
            EarthQuake(context);
            player.getCooldownTracker().setCooldown(this, 80);
            return ActionResultType.SUCCESS;
        }
        return super.onItemUse(context);
    }

    private void EarthQuake(ItemUseContext context) {
        PlayerEntity player = context.getPlayer();
        World world = context.getWorld();
        boolean berserk = player.getMaxHealth() * 1 / 2 >= player.getHealth();
        double radius = 4.0D;
        world.playSound(null, player.getPosX(), player.getPosY(), player.getPosZ(), SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.PLAYERS, 1.5f, 1F / (random.nextFloat() * 0.4F + 0.8F));
        List<Entity> list = world.getEntitiesWithinAABBExcludingEntity(player, player.getBoundingBox().grow(radius, radius, radius));
        for (Entity entity : list) {
            if (entity instanceof LivingEntity) {
                entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (float) player.getAttributeValue(Attributes.ATTACK_DAMAGE));
                entity.setMotion(entity.getMotion().mul(0.0, 2.0, 0.0));
                if (berserk) {
                    entity.setFire((int) 5.0);
                }
            }
        }
        if (world.isRemote) {
            BlockState block = world.getBlockState(player.getPosition().down());
            double NumberofParticles = radius * 4.0D;
            for (double i = 0.0D; i < 80; i++) {
                double d0 = player.getPosX() + radius * MathHelper.sin((float) (i / NumberofParticles * 360.0f));
                double d1 = player.getPosY() + 0.15;
                double d2 = player.getPosZ() + radius * MathHelper.cos((float) (i / NumberofParticles * 360.0f));
                double d3 = random.nextGaussian() * 0.2D;
                double d4 = random.nextGaussian() * 0.2D;
                double d5 = random.nextGaussian() * 0.2D;
                world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, block), d0, d1, d2, d3, d4, d5);
                if (berserk) {
                    world.addParticle(ParticleTypes.FLAME, d0, d1, d2, d3, d4, d5);

                }
            }

        }
    }

    @Override
    public void setDamage(ItemStack stack, int damage){
        super.setDamage(stack, 0);
    }


    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean getIsRepairable(ItemStack itemStack, ItemStack itemStackMaterial) {
        return false;
    }

    @Override
    public int getItemEnchantability() {
        return 16;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment.type != EnchantmentType.BREAKABLE && enchantment.type ==  EnchantmentType.WEAPON && enchantment != Enchantments.SWEEPING
                || enchantment.type == EnchantmentType.DIGGER;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.cataclysm.infernal_forge.desc").mergeStyle(TextFormatting.DARK_GREEN));
        tooltip.add(new TranslationTextComponent("item.cataclysm.infernal_forge.desc2").mergeStyle(TextFormatting.DARK_GREEN));
    }
}







