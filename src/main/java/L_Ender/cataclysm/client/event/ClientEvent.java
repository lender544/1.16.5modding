package L_Ender.cataclysm.client.event;

import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.Ignis_Entity;
import L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import L_Ender.cataclysm.init.ModEffect;
import L_Ender.cataclysm.init.ModItems;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
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

            if (Minecraft.getInstance().player.getActivePotionEffect(ModEffect.EFFECTSTUN.get()) != null && !Minecraft.getInstance().isGamePaused()) {
                EffectInstance effectinstance1 = Minecraft.getInstance().player.getActivePotionEffect(ModEffect.EFFECTSTUN.get());
                float shakeAmplitude = (float) ((1 + effectinstance1.getAmplifier()) * 0.01);
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }

    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        FluidState fluidstate = event.getInfo().getFluidState();
        ItemStack itemstack = Minecraft.getInstance().player != null ? Minecraft.getInstance().player.inventory.armorItemInSlot(3) : null;
        if (itemstack.getItem() == (ModItems.IGNITIUM_HELMET.get()) && fluidstate.isTagged(FluidTags.LAVA)) {
            if (fluidstate.isTagged(FluidTags.LAVA)) {
                event.setDensity(0.05F);
                event.setCanceled(true);
            }
        }
    }



    @SubscribeEvent
    public void onRenderHUD(RenderGameOverlayEvent.Pre event) {
        PlayerEntity player = Minecraft.getInstance().player;
        if (player != null && player.isPassenger()) {
            if (player.getRidingEntity() instanceof Ignis_Entity) {
                if (event.getType().equals(RenderGameOverlayEvent.ElementType.ALL)) {
                    Minecraft.getInstance().ingameGUI.setOverlayMessage(new TranslationTextComponent("you_cant_escape"), false);
                }
            }
        }
    }
}
