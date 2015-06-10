package zornco.bedcraftbeyond;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockColored;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.BlockColoredChestBed;
import zornco.bedcraftbeyond.blocks.BlockRug;
import zornco.bedcraftbeyond.blocks.BlockStoneBed;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileStoneBed;
import zornco.bedcraftbeyond.client.TabBedCraftBeyond;
import zornco.bedcraftbeyond.core.CommonProxy;
import zornco.bedcraftbeyond.item.ItemColoredBed;
import zornco.bedcraftbeyond.item.ItemColoredChestBed;
import zornco.bedcraftbeyond.item.ItemDrawerKey;
import zornco.bedcraftbeyond.item.ItemRug;
import zornco.bedcraftbeyond.item.ItemScissors;
import zornco.bedcraftbeyond.item.ItemStoneBed;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=BedCraftBeyond.MODID, version=BedCraftBeyond.VERSION)
public class BedCraftBeyond {

	public static final String MODID = "BedCraftAndBeyond";
	public static final String VERSION = "1.0";
	// The instance of your mod that Forge uses.
	@Instance(BedCraftBeyond.MODID)
	public static BedCraftBeyond instance;

	// Says where the client and server 'proxy' code is loaded.
	@SidedProxy(clientSide="zornco.bedcraftbeyond.client.ClientProxy", serverSide="zornco.bedcraftbeyond.core.CommonProxy")
	public static CommonProxy proxy;

	public static CreativeTabs bedCraftBeyondTab;

	public static Logger logger = LogManager.getLogger(BedCraftBeyond.MODID);
	
	public static Item rugItem;
	public static Item bedItem;
	public static Item chestBedItem;
	public static Item stoneBedItem;
	public static Item scissors;
	public static Item drawerKey;

	public static Block rugBlock;
	public static Block bedBlock;
	public static Block chestBedBlock;
	public static Block stoneBedBlock;

	//ID's
	public static int rugRI = -1;
	public static int bedRI = -1;
	public static int chestBedRI = -1;
	public static int stoneBedRI = -1;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		bedCraftBeyondTab = new TabBedCraftBeyond("BedCraftAndBeyond");
		/** Blocks **/
		rugBlock = new BlockRug().setHardness(0.8F).setStepSound(Block.soundTypeCloth).setBlockName("rugBlock");
		bedBlock = new BlockColoredBed().setHardness(1.0f).setStepSound(Block.soundTypeCloth).setBlockName("CbedBlock");
		chestBedBlock = new BlockColoredChestBed().setHardness(1.0f).setStepSound(Block.soundTypeCloth).setBlockName("CCbedBlock");
		stoneBedBlock = new BlockStoneBed().setHardness(1.0f).setStepSound(Block.soundTypeCloth).setBlockName("SbedBlock");
		registerBlock(rugBlock, ItemRug.class);
		registerBlock(bedBlock);
		registerBlock(chestBedBlock);
		registerBlock(stoneBedBlock);
		
		/** Items **/
		scissors = new ItemScissors().setUnlocalizedName("scissorsItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		drawerKey = new ItemDrawerKey().setUnlocalizedName("drawerKey").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		bedItem = new ItemColoredBed().setMaxStackSize(1).setUnlocalizedName("CbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		chestBedItem = new ItemColoredChestBed().setMaxStackSize(1).setUnlocalizedName("CCbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		stoneBedItem = new ItemStoneBed().setMaxStackSize(1).setUnlocalizedName("SbedItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		//registerItem(new ItemRug(GameRegistry.findBlock(BedCraftBeyond.MODID, "rugBlock")).setUnlocalizedName("rugItem").setCreativeTab(BedCraftBeyond.bedCraftBeyondTab));
		registerItem(bedItem);
		registerItem(chestBedItem);
		registerItem(stoneBedItem);
		registerItem(scissors);
		registerItem(drawerKey);

	}

	@SuppressWarnings("unchecked")
	@EventHandler
	public void load(FMLInitializationEvent event) {

		
		/*GameRegistry.registerItem(rugItem, "rugitem");
		GameRegistry.registerBlock(rugBlock, "rugblock");
		GameRegistry.registerItem(bedItem, "bedItem");
		GameRegistry.registerBlock(bedBlock, "bedBlock");
		GameRegistry.registerItem(chestBedItem, "chestBedItem");
		GameRegistry.registerBlock(chestBedBlock, "chestBedBlock");
		GameRegistry.registerItem(stoneBedItem, "stoneBedItem");
		GameRegistry.registerBlock(stoneBedBlock, "stoneBedBlock");*/
		
		/** Names **/
		/*LanguageRegistry.instance().addStringLocalization( "itemGroup.BedCraftBeyond", "Bed Craft & Beyond" );
		LanguageRegistry.addName(scissors, "Scissors");

		for(String a : ItemRug.rugColorNames)
		{
			LanguageRegistry.instance().addStringLocalization("item.rug."+a+".name", "en_US", a+" Rug");
			LanguageRegistry.instance().addStringLocalization("tile.rug."+a+".name", "en_US", a+" Rug");
		}
		LanguageRegistry.instance().addStringLocalization("item.Cbed.name", "en_US", "Colored Bed");
		LanguageRegistry.instance().addStringLocalization("tile.Cbed.name", "en_US", "Colored Bed");
		LanguageRegistry.instance().addStringLocalization("item.CCbed.name", "en_US", "Colored Chest Bed");
		LanguageRegistry.instance().addStringLocalization("tile.CCbed.name", "en_US", "Colored Chest Bed");
		LanguageRegistry.instance().addStringLocalization("item.Sbed.name", "en_US", "Stone Bed");
		LanguageRegistry.instance().addStringLocalization("tile.Sbed.name", "en_US", "Stone Bed");
*/


		/** Recipes **/
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
		for(int i = 0; i < 16; i++)
			GameRegistry.addRecipe(new ItemStack(rugBlock, 4, i),
				new Object[] { "xxx", 
					Character.valueOf('x'), new ItemStack(Blocks.wool, 1, i)
				}
			);
		for(int i = 0; i < 16; i++)
			for(int j = 0; j < 16; j++)
				addShapelessOreRecipe(new ItemStack(rugBlock, 1, 15-i), // blue plate
						new Object[]{new ItemStack(rugBlock, 1, j), dyes[i] } );
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				for (int k = 0; k < 4; k++) {
					GameRegistry.addRecipe(new ItemStack(BedCraftBeyond.bedItem, 1, getFreqFromColours(k, BlockColored.func_150032_b(j), BlockColored.func_150032_b(i))), new Object[]{
						"bbp",
						"fff",
						'b', new ItemStack(Blocks.wool, 1, i),
						'p', new ItemStack(Blocks.wool, 1, j),
						'f', new ItemStack(Blocks.planks, 1, k)
						}
					);
					GameRegistry.addRecipe(new ItemStack(BedCraftBeyond.chestBedItem, 1, getFreqFromColours(k, BlockColored.func_150032_b(j), BlockColored.func_150032_b(i))), new Object[]{
						"bbp",
						"fcf",
						'b', new ItemStack(Blocks.wool, 1, i),
						'p', new ItemStack(Blocks.wool, 1, j),
						'f', new ItemStack(Blocks.planks, 1, k),
						'c', new ItemStack(Blocks.chest, 1)
						}
					);
				}
			}
		}
		GameRegistry.addRecipe(new ItemStack(BedCraftBeyond.stoneBedItem, 1, 0), new Object[]{
			"SSS",
			"sss",
			'S', new ItemStack(Blocks.stone, 1),
			's', new ItemStack(Blocks.stone_slab, 1, 0)
		});
		/** Registers **/
		proxy.registerRenderInformation();
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
		GameRegistry.registerTileEntity(TileColoredBed.class, "CbedTile");
		GameRegistry.registerTileEntity(TileColoredChestBed.class, "CCbedTile");
		GameRegistry.registerTileEntity(TileStoneBed.class, "SbedTile");
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		
	}
	
	public static int getFreqFromColours(int colour1, int colour2, int colour3)
    {
        return (colour1 << 8) + ((colour2 & 0xF) << 4) + (colour3 & 0xF);
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
 		GameRegistry.registerItem(item, item.getUnlocalizedName().replace("item.", ""));
	}
	
	public static void registerBlock(Block block)
	{
		GameRegistry.registerBlock(block, block.getUnlocalizedName().replace("tile.", ""));	
	}
	
	public static void registerBlock(Block block, Class<? extends ItemBlock> itemBlockClass)
	{
		GameRegistry.registerBlock(block, itemBlockClass, block.getUnlocalizedName().replace("tile.", ""));
	}
}