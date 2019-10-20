package zornco.bedcraftbeyond.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(BedCraftBeyond.MOD_ID)
@EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BedCraftBeyond {
    public static final String MOD_ID = "bedcraftbeyond";
    public static final String MOD_VERSION = "@VERSION@";
    public static final String MOD_NAME = "BedCraft And Beyond";

    public static BedCraftBeyond instance;

    // public static Configuration CONFIG;
    // public static CommonProxy proxy = DistExecutor.runForDist(() ->
    // ClientProxy::new, () -> ServerProxy::new);

    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();

    public static final ItemGroup MAIN_GROUP = new BedcraftMainGroup();

    public BedCraftBeyond() {
        // Register the setup method for modloading
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        BedcraftItems.registerItems(bus);
        BedcraftBlocks.registerBlocks(bus);
    }

    @SubscribeEvent
    public void setup(final FMLCommonSetupEvent event) {
        instance = (BedCraftBeyond) ModLoadingContext.get().getActiveContainer().getMod();
        // proxy.init();
    }

    @SubscribeEvent
    public void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
        // proxy.init();
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");
    }
}
