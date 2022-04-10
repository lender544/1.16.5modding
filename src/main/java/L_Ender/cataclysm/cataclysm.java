package L_Ender.cataclysm;


import L_Ender.cataclysm.config.CMConfig;
import L_Ender.cataclysm.config.ConfigHolder;
import L_Ender.cataclysm.event.ServerEventHandler;
import L_Ender.cataclysm.init.*;
//import L_Ender.cataclysm.init.ModStructures;
import L_Ender.cataclysm.util.Cataclysm_Group;
import L_Ender.cataclysm.util.Modcompat;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(cataclysm.MODID)
@Mod.EventBusSubscriber(modid = cataclysm.MODID)
public class cataclysm {
    public static final String MODID = "cataclysm";
    public static final Logger LOGGER = LogManager.getLogger();
    public static final SimpleChannel NETWORK_WRAPPER;
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final ItemGroup CATACLYSM_GROUP = new Cataclysm_Group("cataclysmtab");
    public static CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> CommonProxy::new);


    static {
        NetworkRegistry.ChannelBuilder channel = NetworkRegistry.ChannelBuilder.named(new ResourceLocation("cataclysm", "main_channel"));
        String version = PROTOCOL_VERSION;
        version.getClass();
        channel = channel.clientAcceptedVersions(version::equals);
        version = PROTOCOL_VERSION;
        version.getClass();
        NETWORK_WRAPPER = channel.serverAcceptedVersions(version::equals).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    }

    public cataclysm() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);
        bus.addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onModConfigEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHolder.COMMON_SPEC, "cataclysm.toml");
        ModItems.ITEMS.register(bus);
        ModEffect.EFFECTS.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModTileentites.TILE_ENTITY_TYPES.register(bus);
        ModEntities.ENTITY_TYPE.register(bus);
        ModStructures.STRUCTURE_FEATURES.register(bus);
        ModSounds.SOUNDS.register(bus);
        PROXY.init();
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ServerEventHandler());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.NORMAL, this::onWorldLoad);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::onBiomeLoading);
    }

    @SubscribeEvent
    public void onModConfigEvent(final ModConfig.ModConfigEvent event) {
        final ModConfig config = event.getConfig();
        // Rebake the configs when they change
        if (config.getSpec() == ConfigHolder.COMMON_SPEC) {
            CMConfig.bake(config);
        }
    }

    private void setupClient(FMLClientSetupEvent event) {
        PROXY.clientInit();
    }


    public void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModStructures.setupStructures();
            ModConfiguredStructures.registerConfiguredStructures();
            Modcompat.registerDispenserBehaviors();
        });
    }

    public void onBiomeLoading(BiomeLoadingEvent event) {
        ModConfiguredStructures.onBiomeLoading(event);
    }

    public void onWorldLoad(final WorldEvent.Load event) {
        ModStructures.addDimensionalSpacing(event);
    }

}



