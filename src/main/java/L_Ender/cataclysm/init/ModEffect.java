package L_Ender.cataclysm.init;


import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.effects.EffectBlazing_Brand;
import L_Ender.cataclysm.effects.EffectMonstrous;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModEffect {

    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS,
            cataclysm.MODID);

    public static final RegistryObject<Effect> EFFECTMONSTROUS = EFFECTS.register("monstrous", EffectMonstrous::new);

    public static final RegistryObject<Effect> EFFECTBLAZING_BRAND = EFFECTS.register("blazing_brand", EffectBlazing_Brand::new);
}
