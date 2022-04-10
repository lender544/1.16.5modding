package L_Ender.cataclysm.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.AttackDamageEffect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class EffectBlazing_Brand extends AttackDamageEffect {

    public EffectBlazing_Brand() {
        super(EffectType.HARMFUL, 0X865337,-4);
        this.addAttributesModifier(Attributes.ARMOR, "68078724-8653-42D5-A245-9D14A1F54685", 0.0D, AttributeModifier.Operation.ADDITION);
    }

    public void performEffect(LivingEntity LivingEntityIn, int amplifier) {

    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

}
