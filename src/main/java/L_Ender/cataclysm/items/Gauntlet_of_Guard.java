package L_Ender.cataclysm.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class Gauntlet_of_Guard extends Item {
    private final Multimap<Attribute, AttributeModifier> guantletAttributes;
    protected static final UUID ARMOR_MODIFIER = UUID.fromString("C2A4DE55-641E-4DDF-A30C-D65D7C086F02");
    protected static final UUID KNOCKBACK_RESISTANCE_MODIFIER = UUID.fromString("BCACC775-7526-4F70-92DE-D77F725F36AE");

    public Gauntlet_of_Guard(Properties group) {
        super(group);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", 10.0D, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", -2.9F, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ARMOR, new AttributeModifier(ARMOR_MODIFIER, "Tool modifier", 3.0F, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(KNOCKBACK_RESISTANCE_MODIFIER, "Tool modifier", 0.1F, AttributeModifier.Operation.ADDITION));
        this.guantletAttributes = builder.build();
    }


    public UseAction getUseAction(ItemStack p_77661_1_) {
        return UseAction.BOW;
    }

    public int getUseDuration(ItemStack p_77626_1_) {
        return 72000;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
        if (hand == Hand.MAIN_HAND) {
            player.setActiveHand(hand);
            return ActionResult.resultConsume(player.getHeldItem(hand));
        } else {
            return ActionResult.resultFail(player.getHeldItem(hand));
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {
        double radius = 11.0D;
        World world = player.world;
        List<LivingEntity> list = world.getEntitiesWithinAABB(LivingEntity.class, player.getBoundingBox().grow(radius));
        for (LivingEntity entity : list) {
            if (entity instanceof PlayerEntity && ((PlayerEntity) entity).abilities.disableDamage) continue;
            Vector3d diff = entity.getPositionVec().subtract(player.getPositionVec().add(0,0,0));
            diff = diff.normalize().scale(0.1);
            entity.setMotion(entity.getMotion().subtract(diff));

        }

        if (world.isRemote) {
            for (int i = 0; i < 3; ++i) {
                int j = world.rand.nextInt(2) * 2 - 1;
                int k = world.rand.nextInt(2) * 2 - 1;
                double d0 = player.getPosX() + 0.25D * (double) j;
                double d1 = (float) player.getPosY() + world.rand.nextFloat();
                double d2 = player.getPosZ() + 0.25D * (double) k;
                double d3 = world.rand.nextFloat() * (float) j;
                double d4 = ((double) world.rand.nextFloat() - 0.5D) * 0.125D;
                double d5 = world.rand.nextFloat() * (float) k;
                world.addParticle(ParticleTypes.PORTAL, d0, d1, d2, d3, d4, d5);
            }
        }
    }


    @Override
    public boolean canDisableShield(ItemStack stack, ItemStack shield, LivingEntity entity, LivingEntity attacker) {
        return true;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public int getItemEnchantability() {
        return 16;
    }

    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return super.canApplyAtEnchantingTable(stack, enchantment) || enchantment.type != EnchantmentType.BREAKABLE && enchantment.type == EnchantmentType.WEAPON && enchantment != Enchantments.SWEEPING;
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlotType equipmentSlot) {
        return equipmentSlot == EquipmentSlotType.MAINHAND ? this.guantletAttributes : super.getAttributeModifiers(equipmentSlot);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("item.cataclysm.gauntlet_of_guard.desc").mergeStyle(TextFormatting.DARK_GREEN));
    }
}