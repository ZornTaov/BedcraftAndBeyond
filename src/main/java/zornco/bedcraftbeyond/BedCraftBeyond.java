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

  public static Item coloredBedItem;
  public static Item chestBedItem;
  public static Item stoneBedItem;
  public static Item rugItem;
  public static Item scissors;
  public static Item drawerKey;

  public static Block plankBlock;
  public static Block rugBlock;
  public static Block coloredBedBlock;
  public static Block stoneBedBlock;

  File confFile;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    confFile = event.getSuggestedConfigurationFile();
    bedCraftBeyondTab = new TabBedCraftBeyond("bedcraftbeyond");
    bedsTab = new TabBeds();

    /** Blocks **/
    rugBlock = new BlockRug();
    stoneBedBlock = new BlockStoneBed();
    coloredBedBlock = new BlockColoredBed();

    GameRegistry.register(plankBlock);
    GameRegistry.register(rugBlock);
    GameRegistry.register(stoneBedBlock);
    GameRegistry.register(coloredBedBlock);

    /** Items **/
    coloredBedItem = new ItemColoredBed(coloredBedBlock);
    scissors = new ItemScissors();
    drawerKey = new ItemDrawerKey();
    stoneBedItem = new ItemStoneBed();
    rugItem = new ItemRug(rugBlock);

    GameRegistry.register(rugItem);
    GameRegistry.register(coloredBedItem);
    GameRegistry.register(drawerKey);
    GameRegistry.register(scissors);
    GameRegistry.register(stoneBedItem);

    proxy.registerModels();
  }

  @SuppressWarnings("unchecked")
  @EventHandler
  public void init(FMLInitializationEvent event) {

    proxy.init();

    /** Registers **/
    proxy.registerRenderInformationInit();
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

    GameRegistry.registerTileEntity(TileColoredBed.class, "CbedTile");
    // GameRegistry.registerTileEntity(TileBedcraftChestBed.class, "CCbedTile");

    long start = System.currentTimeMillis();

    OreDictionary.registerOre("plankWood", new ItemStack(BedCraftBeyond.plankBlock, 1, OreDictionary.WILDCARD_VALUE));

    proxy.compilePlanks();

    /** Recipes **/
    OreDictionary.registerOre("coloredBed", new ItemStack(BedCraftBeyond.coloredBedItem, 1, OreDictionary.WILDCARD_VALUE));
    OreDictionary.registerOre("coloredChestBed", new ItemStack(BedCraftBeyond.chestBedItem, 1, OreDictionary.WILDCARD_VALUE));
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

    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(drawerKey, 1), new Object[]{"xy", 'x', Items.iron_ingot, 'y', Items.gold_ingot}));

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


    for (int i = 0; i < ItemDye.dyeColors.length; i++) {
      GameRegistry.addRecipe(new ItemStack(rugBlock, 4, i),
              new Object[]{"xxx",
                      Character.valueOf('x'), new ItemStack(Blocks.wool, 1, i)
              }
      );
    }
    for (int i = 0; i < ItemDye.dyeColors.length; i++) {
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(rugBlock, 1, 15 - i), new Object[]{"rug", dyes[i]}));
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(rugBlock, 4, 15 - i), new Object[]{"rug", "rug", "rug", "rug", dyes[i]}));
      GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(rugBlock, 8, 15 - i), new Object[]{"rug", "rug", "rug", "rug", "rug", "rug", "rug", "rug", dyes[i]}));
    }

    // Add plank beds
    for (String plank : PlankHelper.plankColorMap.keySet()) {
      ItemStack bed = new ItemStack(BedCraftBeyond.coloredBedItem, 1, 241);
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
    GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BedCraftBeyond.stoneBedItem, 1, 0),
            "SSS", "sss",
            'S', "blockStone",
            's', new ItemStack(Blocks.stone_slab, 1, 0)
    ));
    
    ++recipesAdded;

    long elapsedTimeMillis = System.currentTimeMillis() - start;
    BedCraftBeyond.logger.info("Generated planklist and recipes in " + elapsedTimeMillis + " milliseconds.");

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.bed), "coloredBed"));

    GameRegistry.addShapelessRecipe(new ItemStack(BedCraftBeyond.coloredBedItem, 1, 241), new ItemStack(Items.bed));

    BedCraftBeyond.logger.info(this.MOD_ID + " has added " + recipesAdded + " recipes for beds! That's a lot!");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    PlankHelper.readyToColor = true;
  }
}