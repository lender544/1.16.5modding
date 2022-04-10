package L_Ender.cataclysm;

import L_Ender.cataclysm.client.event.ClientEvent;
import L_Ender.cataclysm.client.model.armor.ModelIgnitium_Armor;
import L_Ender.cataclysm.client.model.armor.ModelMonstrousHelm;
import L_Ender.cataclysm.client.render.CMItemstackRenderer;
import L_Ender.cataclysm.client.render.entity.*;
import L_Ender.cataclysm.client.sound.SoundEnderGuardianMusic;
import L_Ender.cataclysm.client.sound.SoundMonstrosityMusic;
import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.entity.Ender_Guardian_Entity;
import L_Ender.cataclysm.entity.Netherite_Monstrosity_Entity;
import L_Ender.cataclysm.init.ModEntities;
import L_Ender.cataclysm.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = cataclysm.MODID, value = Dist.CLIENT)
public class ClientProxy extends CommonProxy {

    public static final Map<Integer, SoundMonstrosityMusic> MONSTROSITY_SOUND_MAP = new HashMap<>();

    private static final ModelMonstrousHelm MONSTROUS_HELM_MODEL = new ModelMonstrousHelm(0.3F);
    private static final ModelIgnitium_Armor IGNITIUM_ARMOR_MODEL = new ModelIgnitium_Armor(0.5F);
    private static final ModelIgnitium_Armor IGNITIUM_ARMOR_MODEL_LEGS = new ModelIgnitium_Armor(0.2F);

    public static final Map<Integer, SoundEnderGuardianMusic> GUARDIAN_SOUND_MAP = new HashMap<>();

    public void clientInit() {
        ItemRenderer itemRendererIn = Minecraft.getInstance().getItemRenderer();
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENDER_GOLEM.get(), RendererEnder_Golem::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.NETHERITE_MONSTROSITY.get(), RendererNetherite_Monstrosity::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.LAVA_BOMB.get(), RendererLava_Bomb::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.NAMELESS_SORCERER.get(), RendererNameless_Sorcerer::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.IGNIS.get(), RendererIgnis::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENDER_GUARDIAN.get(), RendererEnder_Guardian::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENDER_GUARDIAN_BULLET.get(), RendererEnder_Guardian_bullet::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.VOID_RUNE.get(), RendererVoid_Rune::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.ENDERMAPTERA.get(), RendererEndermaptera::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.VOID_SCATTER_ARROW.get(), RendererVoid_Scatter_Arrow::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.VOID_SHARD.get(), manager -> new SpriteRenderer(manager, itemRendererIn));
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.SCREEN_SHAKE.get(), RendererNull::new);
        RenderingRegistry.registerEntityRenderingHandler(ModEntities.IGNIS_FIREBALL.get(), RendererIgnis_Fireball::new);
        MinecraftForge.EVENT_BUS.register(new ClientEvent());
        try {
            ItemModelsProperties.registerProperty(ModItems.BULWARK_OF_THE_FLAME.get(), new ResourceLocation("blocking"), (stack, p_239421_1_, p_239421_2_) -> p_239421_2_ != null && p_239421_2_.isHandActive() && p_239421_2_.getActiveItemStack() == stack ? 1.0F : 0.0F);
            ItemModelsProperties.registerProperty(Items.CROSSBOW, new ResourceLocation(cataclysm.MODID, "void_scatter_arrow"), (stack, world, entity) -> entity != null && CrossbowItem.isCharged(stack) && CrossbowItem.hasChargedProjectile(stack, ModItems.VOID_SCATTER_ARROW.get()) ? 1.0F : 0.0F);
        } catch (Exception e) {
            cataclysm.LOGGER.warn("Could not load item models for weapons");

        }
    }


    public Item.Properties setupISTER(Item.Properties group) {
        return group.setISTER(ClientProxy::getTEISR);
    }

    @OnlyIn(Dist.CLIENT)
    public static Callable<ItemStackTileEntityRenderer> getTEISR() {
        return CMItemstackRenderer::new;
    }

    @OnlyIn(Dist.CLIENT)
    public Object getArmorModel(int armorId, LivingEntity entity) {
        switch (armorId) {
            case 0:
                return MONSTROUS_HELM_MODEL;
            case 1:
                return IGNITIUM_ARMOR_MODEL;
            case 2:
                return IGNITIUM_ARMOR_MODEL_LEGS;
        }
        return null;
    }

    public PlayerEntity getClientSidePlayer() {
        return Minecraft.getInstance().player;
    }


    @OnlyIn(Dist.CLIENT)
    public void onEntityStatus(Entity entity, byte updateKind) {
        if (CMConfig.BossMusic) {
            if (entity instanceof Netherite_Monstrosity_Entity && entity.isAlive() && updateKind == 67) {
                float f2 = Minecraft.getInstance().gameSettings.getSoundLevel(SoundCategory.RECORDS);
                if (f2 <= 0) {
                    MONSTROSITY_SOUND_MAP.clear();
                } else {
                    SoundMonstrosityMusic sound;
                    if (MONSTROSITY_SOUND_MAP.get(entity.getEntityId()) == null) {
                        sound = new SoundMonstrosityMusic((Netherite_Monstrosity_Entity) entity);
                        MONSTROSITY_SOUND_MAP.put(entity.getEntityId(), sound);
                    } else {
                        sound = MONSTROSITY_SOUND_MAP.get(entity.getEntityId());
                    }
                    if (!Minecraft.getInstance().getSoundHandler().isPlaying(sound) && sound.isNearest()) {
                        Minecraft.getInstance().getSoundHandler().play(sound);
                    }
                }

            }
            if (entity instanceof Ender_Guardian_Entity && entity.isAlive() && updateKind == 67) {
                float f2 = Minecraft.getInstance().gameSettings.getSoundLevel(SoundCategory.RECORDS);
                if (f2 <= 0) {
                    GUARDIAN_SOUND_MAP.clear();
                } else {
                    SoundEnderGuardianMusic sound;
                    if (GUARDIAN_SOUND_MAP.get(entity.getEntityId()) == null) {
                        sound = new SoundEnderGuardianMusic((Ender_Guardian_Entity) entity);
                        GUARDIAN_SOUND_MAP.put(entity.getEntityId(), sound);
                    } else {
                        sound = GUARDIAN_SOUND_MAP.get(entity.getEntityId());
                    }
                    if (!Minecraft.getInstance().getSoundHandler().isPlaying(sound) && sound.isNearest()) {
                        Minecraft.getInstance().getSoundHandler().play(sound);
                    }
                }

            }
        }


    }

}