package zornco.bedcraftbeyond;

import java.io.File;
import java.util.Iterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.base.Function;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zornco.bedcraftbeyond.blocks.BlockBCBPlanks;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.BlockColoredChestBed;
import zornco.bedcraftbeyond.blocks.BlockRainbowBed;
import zornco.bedcraftbeyond.blocks.BlockRug;
import zornco.bedcraftbeyond.blocks.BlockStoneBed;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileRainbowBed;
import zornco.bedcraftbeyond.blocks.TileStoneBed;
import zornco.bedcraftbeyond.client.TabBedCraftBeyond;
import zornco.bedcraftbeyond.core.CommonProxy;
import zornco.bedcraftbeyond.item.ItemBCBPlank;
import zornco.bedcraftbeyond.item.ItemColoredBed;
import zornco.bedcraftbeyond.item.ItemColoredChestBed;
import zornco.bedcraftbeyond.item.ItemDrawerKey;
import zornco.bedcraftbeyond.item.ItemRainbowBed;
import zornco.bedcraftbeyond.item.ItemRug;
import zornco.bedcraftbeyond.item.ItemScissors;
import zornco.bedcraftbeyond.item.ItemStoneBed;
import zornco.bedcraftbeyond.util.PlankHelper;

@Mod(
		modid = BedCraftBeyond.MOD_ID,
		name = BedCraftBeyond.MOD_NAME,
		version = BedCraftBeyond.VERSION,
		acceptedMinecraftVersions = "[1.8.9,)")
public class BedCraftBeyond {

	public static final String MOD_ID = "bedcraftbeyond";
	public static final String MOD_NAME = "BedCraft And Beyond";
	public static final String VERSION = "${version}";
	// The instance of your mod that Forge uses.
	@Instance(BedCraftBeyond.MOD_ID)
	public static BedCraftBeyond instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="zornco.bedcraftbeyond.client.ClientProxy", serverSide="zornco.bedcraftbeyond.core.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs bedCraftBeyondTab;

	public static Logger logger = LogManager.getLogger(BedCraftBeyond.MOD_ID);

	//public static Item plankItem;
	public static Item bedItem;
	public static Item chestBedItem;
	public static Item stoneBedItem;
	public static Item rainbowBedItem;
	public static Item rugItem;
	public static Item scissors;
	public static Item drawerKey;

	public static Block plankBlock;
	public static Block rugBlock;
	public static Block bedBlock;
	public static Block chestBedBlock;
	public static Block stoneBedBlock;
	public static Block rainbowBedBlock;

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
		plankBlock = new BlockBCBPlanks().setHardness(2.0F).setResistance(5.0F).setStepSound(Block.soundTypeWood).setUnlocalizedName("bcbwood");
		rugBlock = new BlockRug().setHardness(0.8F).setStepSound(Block.soundTypeCloth).setUnlocalizedName("rugBlock");
		bedBlock = new BlockColoredBed().setHardness(1.0f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("CbedBlock");
		chestBedBlock = new BlockColoredChestBed().setHardness(1.0f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("CCbedBlock");
		stoneBedBlock = new BlockStoneBed().setHardness(1.0f).setStepSound(Block.soundTypeStone).setUnlocalizedName("SbedBlock");
		rainbowBedBlock = new BlockRainbowBed().setHardness(1.0f).setStepSound(Block.soundTypeCloth).setUnlocalizedName("RbedBlock");
		registerBlock(rugBlock, ItemRug.class);
		registerBlock(plankBlock, ItemBCBPlank.class);
		registerBlock(bedBlock);
		registerBlock(chestBedBlock);
		registerBlock(stoneBedBlock);
		registerBlock(rainbowBedBlock);

		/** Items **/
		scissors = new ItemScissors();
		drawerKey = new ItemDrawerKey();
		//plankItem = new ItemBCBPlank(plankBlock).setUnlocalizedName("bcbwood").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		bedItem = new ItemColoredBed().setMaxStackSize(1).setUnlocalizedName("CbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		chestBedItem = new ItemColoredChestBed().setMaxStackSize(1).setUnlocalizedName("CCbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		stoneBedItem = new ItemStoneBed().setMaxStackSize(1).setUnlocalizedName("SbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		rainbowBedItem = new ItemRainbowBed().setMaxStackSize(1).setUnlocalizedName("RbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		//registerItem(plankItem);
		//rugItem = new ItemRug(GameRegistry.findBlock(BedCraftBeyond.MOD_ID, "rugBlock")).setUnlocalizedName("rugItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		//registerItem(rugItem);
		registerItem(bedItem);
		registerItem(chestBedItem);
		registerItem(stoneBedItem);
		registerItem(rainbowBedItem);
		//registerItem(scissors);
		proxy.registerModels();

	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void load(FMLInitializationEvent event) {

		/** Registers **/
		proxy.registerRenderInformationInit();
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		GameRegistry.registerTileEntity(TileColoredBed.class, "CbedTile");
		GameRegistry.registerTileEntity(TileColoredChestBed.class, "CCbedTile");
		GameRegistry.registerTileEntity(TileStoneBed.class, "SbedTile");
		GameRegistry.registerTileEntity(TileRainbowBed.class, "RbedTile");
		long start = System.currentTimeMillis();
		OreDictionary.registerOre("plankWood", new ItemStack(BedCraftBeyond.plankBlock, 1, OreDictionary.WILDCARD_VALUE));
		proxy.compilePlanks();
		//BedCraftBeyond.logger.info(PlankHelper.getPlankColorMap2());
		/** Recipes **/
		OreDictionary.registerOre("coloredBed", new ItemStack(BedCraftBeyond.bedItem, 1, OreDictionary.WILDCARD_VALUE));
		//OreDictionary.registerOre("rug", new ItemStack(BedCraftBeyond.rugBlock, 1, OreDictionary.WILDCARD_VALUE));
		OreDictionary.registerOre("coloredChestBed", new ItemStack(BedCraftBeyond.chestBedItem, 1, OreDictionary.WILDCARD_VALUE));
		int recipesAdded = 0;
		// This will be made into an option as soon as someone shows me a recipe that requires the vanilla bed!
		Iterator<IRecipe> iterator = CraftingManager.getInstance().getRecipeList().iterator();
		while(iterator.hasNext())
		{
			ItemStack r = iterator.next().getRecipeOutput();
			if(r != null && r.getItem() == Items.bed)
			{
				iterator.remove();
				logger.info("Removed Vanilla Bed.");
			}
		}
		/*GameRegistry.addShapelessRecipe(new ItemStack(scissors, 1, 16), // scissors
				new Object[]{ Items.iron_ingot, Items.redstone } );*/
		/*addOreRecipe(new ItemStack(scissors, 1),
				new Object[] { " x ", "xxy", " y ", 
			'x', Items.iron_ingot, 
			'y', "dyeRed"
				}
			);*/
		addOreRecipe(new ItemStack(drawerKey, 1),
				new Object[] { "xy", 
						'x', Items.iron_ingot, 
						'y', Items.gold_ingot
		}
				);
		recipesAdded++;
		String[] dyes = 
			{
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

		
		for(int i = 0; i < ItemDye.dyeColors.length; i++)
		{
			GameRegistry.addRecipe(new ItemStack(rugBlock, 4, i),
				new Object[] { "xxx", 
					Character.valueOf('x'), new ItemStack(Blocks.wool, 1, i)
				}
			);
		}
		for(int i = 0; i < ItemDye.dyeColors.length; i++)
		{
			addShapelessOreRecipe(new ItemStack(rugBlock, 1, 15-i), 
					new Object[]{"rug", dyes[i] } );
			addShapelessOreRecipe(new ItemStack(rugBlock, 4, 15-i), 
					new Object[]{"rug", "rug", "rug", "rug", dyes[i] } );
			addShapelessOreRecipe(new ItemStack(rugBlock, 8, 15-i), 
					new Object[]{"rug", "rug", "rug", "rug", "rug", "rug", "rug", "rug", dyes[i] } );
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
					'f', new ItemStack((Item)(Item.itemRegistry.getObject(new ResourceLocation(plank.split("@")[0]))), 1, Integer.parseInt(plank.split("@")[1]))
			}
					);
			recipesAdded++;
			GameRegistry.addRecipe(chestBed, new Object[]{
					"bbb",
					"fcf",
					'b', Blocks.wool,//new ItemStack(Blocks.wool, 1, i),
					//'p', new ItemStack(Blocks.wool, 1, j),
					'f', new ItemStack((Item)(Item.itemRegistry.getObject(new ResourceLocation(plank.split("@")[0]))), 1, Integer.parseInt(plank.split("@")[1])),
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

		long elapsedTimeMillis = System.currentTimeMillis()-start;
		BedCraftBeyond.logger.info("Time Took to generate planklist: " + elapsedTimeMillis);

		addShapelessOreRecipe(new ItemStack(Items.bed), new Object[]{ "coloredBed" } );

		GameRegistry.addShapelessRecipe(new ItemStack(BedCraftBeyond.bedItem, 1, 241), new Object[]{ new ItemStack(Items.bed) } );

		BedCraftBeyond.logger.info(this.MOD_ID + " has added " + recipesAdded + " Recipes for Beds! That's a lot!");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		BedCraftBeyond.logger.info(this.MOD_ID + " has posted");
		PlankHelper.readyToColor = true;

	}

	@SuppressWarnings("unchecked")
	public static void addOreRecipe(ItemStack output, Object[] input) {
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(output, new Object[] { Boolean.valueOf(true), input }));
	}

	@SuppressWarnings("unchecked")
	public static void addShapelessOreRecipe(ItemStack output, Object[] input)
	{
		CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(output, input));
	}

	public static void registerItem(Item item)
	{
		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", "").replace("tile.", ""));
	}

	public static void registerBlock(Block block)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));	
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass)
	{
		GameRegistry.registerBlock(block, itemBlockClass, block.getUnlocalizedName().replace("tile.", ""));
	}

	public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass, Object... itemCtorArgs)
	{
		GameRegistry.registerBlock(block, itemBlockClass, block.getUnlocalizedName().replace("tile.", ""), itemCtorArgs);
	}
}