package L_Ender.cataclysm;

import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.config.ConfigHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import static L_Ender.cataclysm.cataclysm.MODID;


@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CommonProxy {


    public void init() {
    }

    public void clientInit() {
    }

    public Item.Properties setupISTER(Item.Properties group) {
        return group;
    }

    public PlayerEntity getClientSidePlayer() {
        return null;
    }

    public Object getArmorModel(int armorId, LivingEntity entity) {
        return null;
    }

    public void onEntityStatus(Entity entity, byte updateKind) {
    }


}
