package L_Ender.cataclysm.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;


public class EffectStun extends Effect {

    public EffectStun() {
        super(EffectType.HARMFUL, 0xFF8C00);
        this.addAttributesModifier(Attributes.MOVEMENT_SPEED, "57F1BADC-F545-4D89-B218-751C2FF8053D", -0.5D, AttributeModifier.Operation.ADDITION);

    }

    public void performEffect(LivingEntity LivingEntityIn, int amplifier) {

    }

    public boolean isReady(int duration, int amplifier) {
        return duration > 0;
    }

}
