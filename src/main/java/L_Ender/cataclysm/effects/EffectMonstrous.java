package L_Ender.cataclysm.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectMonstrous extends Effect {

    public EffectMonstrous() {
        super(EffectType.BENEFICIAL, 0X865337);
        this.addAttributesModifier(Attributes.KNOCKBACK_RESISTANCE, "953533ED-0994-4421-9E4E-47557FA8EE2A", 0.5D, AttributeModifier.Operation.ADDITION);
        this.addAttributesModifier(Attributes.ARMOR, "E6C06C84-8021-4296-A512-AFB0C98806CA", 3.0D, AttributeModifier.Operation.ADDITION);
        this.addAttributesModifier(Attributes.ARMOR_TOUGHNESS, "1F329CAC-F59E-41C1-A5E6-18A45A3237B8", 2.0D, AttributeModifier.Operation.ADDITION);
    }

    public void performEffect(LivingEntity LivingEntityIn, int amplifier) {
        if (LivingEntityIn.getHealth() < LivingEntityIn.getMaxHealth() * 1/2) {
            LivingEntityIn.heal(1.0F);
        }

    }

    public boolean isReady(int duration, int amplifier) {
        int k = 50 >> amplifier;
        if (k > 0) {
            return duration % k == 0;
        } else {
            return true;
        }
    }

}
