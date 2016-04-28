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
import zornco.bedcraftbeyond.blocks.tiles.TileBedcraftBed;
import zornco.bedcraftbeyond.client.TabBedCraftBeyond;
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

  public static Logger logger = LogManager.getLogger(BedCraftBeyond.MOD_ID);

  //public static Item plankItem;
  public static Item bedItem;
  public static Item chestBedItem;
  public static Item stoneBedItem;
  public static Item rugItem;
  public static Item scissors;
  public static Item drawerKey;

  public static Block plankBlock;
  public static Block rugBlock;
  public static Block coloredBedBlock;
  public static Block chestBedBlock;
  public static Block stoneBedBlock;

  //ID's
  public static int rugRI = -1;
  public static int bedRI = -1;
  public static int chestBedRI = -1;
  public static int stoneBedRI = -1;
  File confFile;

  @EventHandler
  public void preInit(FMLPreInitializationEvent event) {
    confFile = event.getSuggestedConfigurationFile();
    bedCraftBeyondTab = new TabBedCraftBeyond("bedcraftbeyond");
    /** Blocks **/
    plankBlock = new BlockBCBPlanks();
    rugBlock = new BlockRug();
    // chestBedBlock = new BlockColoredChestBed();
    stoneBedBlock = new BlockStoneBed();
    coloredBedBlock = new BlockColoredBed();

    GameRegistry.register(plankBlock);
    GameRegistry.register(rugBlock);
    // GameRegistry.register(chestBedBlock);
    GameRegistry.register(stoneBedBlock);
    GameRegistry.register(coloredBedBlock);

    GameRegistry.register(new ItemRug(rugBlock).setRegistryName(rugBlock.getRegistryName()));
    GameRegistry.register(new ItemBCBPlank(plankBlock).setRegistryName(plankBlock.getRegistryName()));

    /** Items **/
    bedItem = new ItemColoredBed(coloredBedBlock);
    scissors = new ItemScissors();
    drawerKey = new ItemDrawerKey();
    stoneBedItem = new ItemStoneBed();

    GameRegistry.register(bedItem);

    // GameRegistry.register(chestBedItem);
    GameRegistry.register(stoneBedItem);

    proxy.registerModels();
  }

  @SuppressWarnings("unchecked")
  @EventHandler
  public void load(FMLInitializationEvent event) {

    /** Registers **/
    proxy.registerRenderInformationInit();
    NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);

    GameRegistry.registerTileEntity(TileBedcraftBed.class, "CbedTile");
    // GameRegistry.registerTileEntity(TileBedcraftChestBed.class, "CCbedTile");

    long start = System.currentTimeMillis();

    OreDictionary.registerOre("plankWood", new ItemStack(BedCraftBeyond.plankBlock, 1, OreDictionary.WILDCARD_VALUE));

    proxy.compilePlanks();

    /** Recipes **/
    OreDictionary.registerOre("coloredBed", new ItemStack(BedCraftBeyond.bedItem, 1, OreDictionary.WILDCARD_VALUE));
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

    for (String plank : PlankHelper.plankColorMap.keySet()) {
      ItemStack bed = new ItemStack(BedCraftBeyond.bedItem, 1, 241);//getFreqFromColours(BlockColored.func_150032_b(j), BlockColored.func_150032_b(i)));
      ItemStack chestBed = new ItemStack(BedCraftBeyond.chestBedItem, 1, 241);//getFreqFromColours(BlockColored.func_150032_b(j), BlockColored.func_150032_b(i)));
      bed.setTagCompound(new NBTTagCompound());
      //PlankHelper.addPlankInfo(bed.stackTagCompound, plank);
      bed.getTagCompound().setString("plankNameSpace", plank);
      GameRegistry.addRecipe(bed, new Object[]{
                      "bbb",
                      "fff",
                      'b', Blocks.wool,//new ItemStack(Blocks.wool, 1, i),
                      //'p', new ItemStack(Blocks.wool, 1, j),
                      'f', new ItemStack((Item) (Item.itemRegistry.getObject(new ResourceLocation(plank.split("@")[0]))), 1, Integer.parseInt(plank.split("@")[1]))
              }
      );
      recipesAdded++;
      GameRegistry.addRecipe(chestBed, new Object[]{
                      "bbb",
                      "fcf",
                      'b', Blocks.wool,//new ItemStack(Blocks.wool, 1, i),
                      //'p', new ItemStack(Blocks.wool, 1, j),
                      'f', new ItemStack((Item) (Item.itemRegistry.getObject(new ResourceLocation(plank.split("@")[0]))), 1, Integer.parseInt(plank.split("@")[1])),
                      'c', Blocks.chest
              }
      );
      recipesAdded++;

    }
    GameRegistry.addRecipe(new ItemStack(BedCraftBeyond.stoneBedItem, 1, 0), new Object[]{
            "SSS",
            "sss",
            'S', new ItemStack(Blocks.stone, 1),
            's', new ItemStack(Blocks.stone_slab, 1, 0)
    });

    long elapsedTimeMillis = System.currentTimeMillis() - start;
    BedCraftBeyond.logger.info("Time Took to generate planklist: " + elapsedTimeMillis);

    GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.bed), new Object[]{"coloredBed"}));

    GameRegistry.addShapelessRecipe(new ItemStack(BedCraftBeyond.bedItem, 1, 241), new Object[]{new ItemStack(Items.bed)});

    BedCraftBeyond.logger.info(this.MOD_ID + " has added " + recipesAdded + " recipes for Beds! That's a lot!");
  }

  @EventHandler
  public void postInit(FMLPostInitializationEvent event) {
    PlankHelper.readyToColor = true;
  }
}