package zornco.bedcraftbeyond;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.blocks.*;
import zornco.bedcraftbeyond.client.tabs.TabBedCraftBeyond;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.common.BedHelper;
import zornco.bedcraftbeyond.common.CommonProxy;
import zornco.bedcraftbeyond.common.Crafting;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.config.ConfigHelper;
import zornco.bedcraftbeyond.item.*;

import java.io.File;
import java.util.Iterator;

@Mod(
        modid = BedCraftBeyond.MOD_ID,
        name = BedCraftBeyond.MOD_NAME,
        version = "${version}",
        acceptedMinecraftVersions = "[1.9,)")
public class BedCraftBeyond {

  public static final String MOD_ID = "bedcraftbeyond";
  public static final String MOD_NAME = "BedCraft And Beyond";

  // The instance of your mod that Forge uses.
  @Instance(BedCraftBeyond.MOD_ID)
  public static BedCraftBeyond instance;

  // Says where the client and server 'proxy' code is loaded.
  @SidedProxy(clientSide = "zornco.bedcraftbeyond.client.ClientProxy", serverSide = "zornco.bedcraftbeyond.core.CommonProxy")
  public static CommonProxy proxy;

  public static CreativeTabs bedCraftBeyondTab;
  public static TabBeds bedsTab;

  public static Logger logger = LogManager.getLogger(BedCraftBeyond.MOD_ID);

  public static SimpleNetworkWrapper network;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    ConfigHelper.modConfigs = event.getModConfigurationDirectory();
    ConfigHelper.setupModDirs();

    bedCraftBeyondTab = new TabBedCraftBeyond("bedcraftbeyond");
    bedsTab = new TabBeds();

    BcbBlocks.registerBlocks();
    BcbItems.registerItems();

    proxy.registerModels();
    network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);
    zornco.bedcraftbeyond.network.Registration.registerMessages();
  }

  @SuppressWarnings("unchecked")
  @EventHandler
  public void init(FMLInitializationEvent event) {

    proxy.init();

    /** Registers **/
    proxy.registerRenderInformationInit();
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

    GameRegistry.registerTileEntity(TileColoredBed.class, "CbedTile");

    long start = System.currentTimeMillis();

    proxy.compilePlanks();

    /** Recipes **/
    OreDictionary.registerOre("coloredBed", new ItemStack(BcbItems.coloredBed, 1, OreDictionary.WILDCARD_VALUE));

    Crafting.addRecipes();

    long elapsedTimeMillis = System.currentTimeMillis() - start;
    BedCraftBeyond.logger.info("Generated wooden frame list and recipes in " + elapsedTimeMillis + " milliseconds.");
    BedCraftBeyond.logger.info(this.MOD_ID + " has added " + Crafting.recipesAdded + " recipes for beds! That's a lot!");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    // PlankHelper.readyToColor = true;
    BedHelper.compileWoodFrames();
  }
}