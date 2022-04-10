package L_Ender.cataclysm.client.event;

import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class ClientEvent {

    @SubscribeEvent
    public void onSetupCamera(EntityViewRenderEvent.CameraSetup event) {
        PlayerEntity player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getRenderPartialTicks();
        float ticksExistedDelta = player.ticksExisted + delta;
        if (player != null) {
            if (CMConfig.ScreenShake) {
                float shakeAmplitude = 0;
                for (ScreenShake_Entity ScreenShake : player.world.getEntitiesWithinAABB(ScreenShake_Entity.class, player.getBoundingBox().grow(20, 20, 20))) {
                    if (ScreenShake.getDistance(player) < ScreenShake.getRadius()) {
                        shakeAmplitude += ScreenShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

}
