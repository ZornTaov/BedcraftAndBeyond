package zornco.bedcraftbeyond;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.*;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
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
import zornco.bedcraftbeyond.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.client.tabs.TabBedCraftBeyond;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.core.CommonProxy;
import zornco.bedcraftbeyond.item.*;
import zornco.bedcraftbeyond.item.colored_bed.ItemColoredBed;
import zornco.bedcraftbeyond.item.colored_bed.ItemDrawerKey;
import zornco.bedcraftbeyond.util.PlankHelper;

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

  File confFile;
  public static SimpleNetworkWrapper network;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    confFile = event.getSuggestedConfigurationFile();
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

    int recipesAdded = 0;

    // This will be made into an option as soon as someone shows me a recipe that requires the vanilla bed!
    Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
    while (iterator.hasNext()) {
      ItemStack r = iterator.next().getRecipeOutput();
      if (r != null && r.getItem() == Items.bed) {
        iterator.remove();
        logger.info("Removed Vanilla Bed.");
      }
    }

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.drawerKey, 1), "xy", 'x', "ingotIron", 'y', "ingotGold"));

    recipesAdded++;
    String[] dyes = {
            "dyeBlack",
            "dyeRed",
            "dyeGreen",
            "dyeBrown",
            "dyeBlue",
            "dyePurple",
            "dyeCyan",
            "dyeLightGray",
            "dyeGray",
            "dyePink",
            "dyeLime",
            "dyeYellow",
            "dyeLightBlue",
            "dyeMagenta",
            "dyeOrange",
            "dyeWhite"
    };


    for (int i = 0; i < ItemDye.dyeColors.length; i++)
      GameRegistry.addRecipe(new ItemStack(BcbItems.rug, 4, i), "xxx", 'x', new ItemStack(Blocks.wool, 1, i));

    for (int i = 0; i < ItemDye.dyeColors.length; i++) {
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BcbItems.rug, 1, 15 - i), new Object[]{"rug", dyes[i]}));
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BcbItems.rug, 4, 15 - i), new Object[]{"rug", "rug", "rug", "rug", dyes[i]}));
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(BcbItems.rug, 8, 15 - i), new Object[]{"rug", "rug", "rug", "rug", "rug", "rug", "rug", "rug", dyes[i]}));
    }

    // Add plank beds
    for (String plank : PlankHelper.plankColorMap.keySet()) {
      ItemStack bed = new ItemStack(BcbItems.coloredBed, 1, 241);
      bed.setTagCompound(new NBTTagCompound());
      bed.getTagCompound().setString("plankType", plank);
      GameRegistry.addRecipe(new ShapedOreRecipe(bed,
              "bbb", "fff",
              'b', "blockWool",
              'f', new ItemStack(Item.itemRegistry.getObject(new ResourceLocation(plank.split("@")[0])), 1, Integer.parseInt(plank.split("@")[1]))
      ));

      recipesAdded++;
    }

    // Add stone bed recipe
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.stoneBed, 1, 0),
            "SSS", "sss",
            'S', "blockStone",
            's', new ItemStack(Blocks.stone_slab, 1, 0)
    ));

    ++recipesAdded;

    long elapsedTimeMillis = System.currentTimeMillis() - start;
    BedCraftBeyond.logger.info("Generated planklist and recipes in " + elapsedTimeMillis + " milliseconds.");

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.bed), "coloredBed"));

    GameRegistry.addShapelessRecipe(new ItemStack(BcbItems.coloredBed, 1, 241), new ItemStack(Items.bed));

    BedCraftBeyond.logger.info(this.MOD_ID + " has added " + recipesAdded + " recipes for beds! That's a lot!");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    PlankHelper.readyToColor = true;
  }
}