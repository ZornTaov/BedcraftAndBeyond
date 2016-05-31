package zornco.bedcraftbeyond;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.client.tabs.TabBedCraftBeyond;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.common.CommonProxy;
import zornco.bedcraftbeyond.common.commands.CommandBedcraft;
import zornco.bedcraftbeyond.common.crafting.Recipes;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.config.ConfigHelper;
import zornco.bedcraftbeyond.util.ColorHelper;

@Mod(
        modid = BedCraftBeyond.MOD_ID,
        name = BedCraftBeyond.MOD_NAME,
        version = "${version}",
        acceptedMinecraftVersions = "[1.9.4,)",
        guiFactory = "zornco.bedcraftbeyond.config.ConfigGuiFactory")
public class BedCraftBeyond {

  public static final String MOD_ID = "bedcraftbeyond";
  public static final String MOD_NAME = "BedCraft And Beyond";

  // The instance of your mod that Forge uses.
  @Instance(BedCraftBeyond.MOD_ID)
  public static BedCraftBeyond instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(clientSide = "zornco.bedcraftbeyond.client.ClientProxy", serverSide = "zornco.bedcraftbeyond.server.ServerProxy")
  public static CommonProxy proxy;

  public static CreativeTabs bedCraftBeyondTab;
  public static TabBeds bedsTab;

  public static Logger logger = LogManager.getLogger(BedCraftBeyond.MOD_ID);

  public static SimpleNetworkWrapper network;

  public static Configuration config;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {

    // Do not set up config here, it's setup in ConfigHelper
    ConfigHelper.allModConfigsDir = event.getModConfigurationDirectory();
    ConfigHelper.setup();

    bedCraftBeyondTab = new TabBedCraftBeyond("bedcraftbeyond");
    bedsTab = new TabBeds();

    ColorHelper.initColorList();
    BcbBlocks.registerBlocks();
    BcbItems.registerItems();

    proxy.registerModels();
    network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

    proxy.registerMessages();

    MinecraftForge.EVENT_BUS.register(instance);

    // TODO: If en_US.lang not found, run JsonCompiler.main('en_US')
  }

  @SuppressWarnings("unchecked")
  @EventHandler
  public void init(FMLInitializationEvent event) {

    proxy.init();

    long start = System.currentTimeMillis();

    /** Recipes **/
    OreDictionary.registerOre("coloredBed", new ItemStack(BcbItems.woodenBed, 1, OreDictionary.WILDCARD_VALUE));
    Recipes.addRecipes();

    long elapsedTimeMillis = System.currentTimeMillis() - start;
    BedCraftBeyond.logger.info("Generated wooden frame list and recipes in " + elapsedTimeMillis + " milliseconds.");
    BedCraftBeyond.logger.info(this.MOD_NAME + " has added " + Recipes.recipesAdded + " recipes! That's a lot!");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    // PlankHelper.readyToColor = true;
    proxy.compileFrames();
  }

  @SubscribeEvent
  public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
    if(eventArgs.getModID().equals(BedCraftBeyond.MOD_ID))
      ConfigHelper.refreshConfigs();
  }

  @Mod.EventHandler
  public void serverStarted(FMLServerStartingEvent ev){
    ev.registerServerCommand(new CommandBedcraft());
  }
}