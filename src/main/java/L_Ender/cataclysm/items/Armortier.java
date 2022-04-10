package L_Ender.cataclysm.items;

import L_Ender.cataclysm.init.ModItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import java.util.function.Supplier;


 public enum Armortier implements IArmorMaterial {
     THE_DEFILER(new int[] {4, 7, 10, 4}, 3f, 45, 25, 0.15f, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE , ModItems.ENDERITE_INGOT),
     THE_UNMAKER(new int[] {4, 7, 10, 4}, 3f, 45, 25, 0.15f, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE , ModItems.WITHERITE_INGOT),
     IGNITIUM(new int[] {4, 7, 10, 4}, 3f, 45, 25, 0.15f, SoundEvents.ITEM_ARMOR_EQUIP_NETHERITE , ModItems.IGNITIUM_INGOT);

     private static final int[] DURABILITY_ARRAY = new int[] {13, 15, 16, 11};
     private final int durability, enchantability;
     private final int[] dmgReduction; // boots[0], legs[1], chest[2], helm[3]
     private final float toughness, knockbackResistance;
     private final SoundEvent sound;
     private final Supplier<Item> repairMaterial;

     Armortier(int[] dmgReduction, float toughness, int durability, int enchantability, float knockbackResistance, SoundEvent sound, Supplier<Item> repairMaterial)
     {
         this.durability = durability;
         this.dmgReduction = dmgReduction;
         this.enchantability = enchantability;
         this.toughness = toughness;
         this.knockbackResistance = knockbackResistance;
         this.sound = sound;
         this.repairMaterial = repairMaterial;
     }

     @Override
     public int getDurability(EquipmentSlotType slotIn)
     {
         return DURABILITY_ARRAY[slotIn.getIndex()] * durability;
     }

     @Override
     public int getDamageReductionAmount(EquipmentSlotType slot)
     {
         return dmgReduction[slot.getIndex()];
     }

     @Override
     public int getEnchantability()
     {
         return enchantability;
     }

     @Override
     public SoundEvent getSoundEvent()
     {
         return sound;
     }

     @Override
     public Ingredient getRepairMaterial()
     {
         return Ingredient.fromItems(repairMaterial.get());
     }

     @Override
     public String getName()
     {
         return toString().toLowerCase();
     }

     @Override
     public float getToughness()
     {
         return toughness;
     }

     @Override
     public float getKnockbackResistance()
     {
         return knockbackResistance;
     }

 }



