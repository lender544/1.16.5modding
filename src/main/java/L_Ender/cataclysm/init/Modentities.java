package L_Ender.cataclysm.init;


import L_Ender.cataclysm.cataclysm;
import L_Ender.cataclysm.entity.*;
import L_Ender.cataclysm.entity.effect.ScreenShake_Entity;
import L_Ender.cataclysm.entity.projectile.*;
import L_Ender.cataclysm.items.Modspawnegg;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = cataclysm.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES, cataclysm.MODID);

    public static final RegistryObject<EntityType<Ender_Golem_Entity>> ENDER_GOLEM = ENTITY_TYPE.register("ender_golem", () -> EntityType.Builder.create(Ender_Golem_Entity::new, EntityClassification.MONSTER)
            .size(2.5F, 3.5F)
            .immuneToFire()
            .build(cataclysm.MODID + ":ender_golem"));

    public static final RegistryObject<EntityType<Ender_Guardian_Entity>> ENDER_GUARDIAN = ENTITY_TYPE.register("ender_guardian", () -> EntityType.Builder.create(Ender_Guardian_Entity::new, EntityClassification.MONSTER)
            .size(2.5F, 3.8F)
            .immuneToFire()
            .build(cataclysm.MODID + ":ender_guardian"));

    public static final RegistryObject<EntityType<Netherite_Monstrosity_Entity>> NETHERITE_MONSTROSITY = ENTITY_TYPE.register("netherite_monstrosity", () -> EntityType.Builder.create(Netherite_Monstrosity_Entity::new, EntityClassification.MONSTER)
            .size(3.0f, 5.75f)
            .immuneToFire()
            .build(cataclysm.MODID + ":netherite_monstrosity"));

    public static final RegistryObject<EntityType<Lava_Bomb_Entity>> LAVA_BOMB = ENTITY_TYPE.register("lava_bomb", () -> EntityType.Builder.<Lava_Bomb_Entity>create(Lava_Bomb_Entity::new, EntityClassification.MISC)
            .size(0.5f, 0.5f)
            .immuneToFire()
            .setUpdateInterval(20)
            .build(cataclysm.MODID + ":lava_bomb"));


    public static final RegistryObject<EntityType<Nameless_Sorcerer_Entity>> NAMELESS_SORCERER = ENTITY_TYPE.register("nameless_sorcerer", () -> EntityType.Builder.create(Nameless_Sorcerer_Entity::new, EntityClassification.MONSTER)
            .size(0.6F, 1.95F)
            .build(cataclysm.MODID + ":nameless_sorcerer"));

    public static final RegistryObject<EntityType<Ignis_Entity>> IGNIS = ENTITY_TYPE.register("ignis", () -> EntityType.Builder.create(Ignis_Entity::new, EntityClassification.MONSTER)
            .size(2.5F, 3.5F)
            .immuneToFire()
            .build(cataclysm.MODID + ":ignis"));

    public static final RegistryObject<EntityType<Ender_Guardian_Bullet_Entity>> ENDER_GUARDIAN_BULLET = ENTITY_TYPE.register("ender_guardian_bullet", () -> EntityType.Builder.<Ender_Guardian_Bullet_Entity>create(Ender_Guardian_Bullet_Entity::new, EntityClassification.MISC)
            .size(0.3125f, 0.3125f).setUpdateInterval(1).setTrackingRange(64).setShouldReceiveVelocityUpdates(true)
            .build(cataclysm.MODID + ":ender_guardian_bullet"));

    public static final RegistryObject<EntityType<Void_Rune_Entity>> VOID_RUNE = ENTITY_TYPE.register("void_rune", () -> EntityType.Builder.<Void_Rune_Entity>create(Void_Rune_Entity::new, EntityClassification.MISC)
            .size(0.6F, 1.95F).trackingRange(6).updateInterval(2)
            .immuneToFire()
            .build(cataclysm.MODID + ":void_rune"));

    public static final RegistryObject<EntityType<Endermaptera_Entity>> ENDERMAPTERA = ENTITY_TYPE.register("endermaptera", () -> EntityType.Builder.create(Endermaptera_Entity::new, EntityClassification.MONSTER)
            .size(1.2F, 0.8f)
            .immuneToFire()
            .build(cataclysm.MODID + ":endermaptera"));

    public static final RegistryObject<EntityType<Void_Scatter_Arrow_Entity>> VOID_SCATTER_ARROW = ENTITY_TYPE.register("void_scatter_arrow", () -> EntityType.Builder.<Void_Scatter_Arrow_Entity>create(Void_Scatter_Arrow_Entity::new, EntityClassification.MISC)
            .size(0.5f, 0.5f)
            .setCustomClientFactory(Void_Scatter_Arrow_Entity::new)
            .updateInterval(20)
            .trackingRange(4)
            .build(cataclysm.MODID + ":void_scatter_arrow"));

    public static final RegistryObject<EntityType<Void_Shard_Entity>> VOID_SHARD = ENTITY_TYPE.register("void_shard", () -> EntityType.Builder.<Void_Shard_Entity>create(Void_Shard_Entity::new, EntityClassification.MISC)
            .size(0.5f, 0.5f)
            .setCustomClientFactory(Void_Shard_Entity::new)
            .updateInterval(20)
            .trackingRange(4)
            .build(cataclysm.MODID + ":void_shard"));

    public static final RegistryObject<EntityType<ScreenShake_Entity>> SCREEN_SHAKE = ENTITY_TYPE.register("screen_shake", () -> EntityType.Builder.<ScreenShake_Entity>create(ScreenShake_Entity::new, EntityClassification.MISC)
            .disableSummoning()
            .size(1.0f, 1.0f)
            .setUpdateInterval(Integer.MAX_VALUE)
            .build(cataclysm.MODID + ":screen_shake"));

    public static final RegistryObject<EntityType<Ignis_Fireball_Entity>> IGNIS_FIREBALL = ENTITY_TYPE.register("ignis_fireball", () ->  EntityType.Builder.<Ignis_Fireball_Entity>create(Ignis_Fireball_Entity::new, EntityClassification.MISC)
            .size(0.3125F, 0.3125F)
            .trackingRange(4)
            .updateInterval(10)
            .build(cataclysm.MODID + ":ignis_fireball"));


    @SubscribeEvent
    public static void initializeAttributes(EntityAttributeCreationEvent event) {
        event.put(ENDER_GOLEM.get(), Ender_Golem_Entity.bakeAttributes().create());
        event.put(NETHERITE_MONSTROSITY.get(), Netherite_Monstrosity_Entity.bakeAttributes().create());
        event.put(NAMELESS_SORCERER.get(), Nameless_Sorcerer_Entity.bakeAttributes().create());
        event.put(IGNIS.get(), Ignis_Entity.bakeAttributes().create());
        event.put(ENDER_GUARDIAN.get(), Ender_Guardian_Entity.bakeAttributes().create());
        event.put(ENDERMAPTERA.get(), Endermaptera_Entity.bakeAttributes().create());

    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onPostRegisterEntities(final RegistryEvent.Register<EntityType<?>> event) {
        EntitySpawnPlacementRegistry.register(ENDERMAPTERA.get(), EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Endermaptera_Entity::canSpawn);
        Modspawnegg.initSpawnEggs();
    }

}

