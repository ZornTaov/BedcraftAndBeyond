package zornco.bedcraftbeyond.client;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockColored;
import net.minecraft.block.properties.IProperty;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockBCBPlanks;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.BlockColoredChestBed;
import zornco.bedcraftbeyond.blocks.BlockRug;
import zornco.bedcraftbeyond.blocks.BlockStoneBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.client.render.BlockBedRendererTESR;
import zornco.bedcraftbeyond.client.render.TileRainbowBedRenderer;
import zornco.bedcraftbeyond.client.render.TileStoneBedRenderer;
import zornco.bedcraftbeyond.core.CommonProxy;
import zornco.bedcraftbeyond.gui.ContainerSuitcase;
import zornco.bedcraftbeyond.gui.GuiColoredChestBed;
import zornco.bedcraftbeyond.gui.GuiSuitcase;
import zornco.bedcraftbeyond.gui.InventorySuitcase;
import zornco.bedcraftbeyond.item.IName;
import zornco.bedcraftbeyond.item.ItemBCBPlank;
import zornco.bedcraftbeyond.item.ItemColoredBed;
import zornco.bedcraftbeyond.item.ItemColoredChestBed;
import zornco.bedcraftbeyond.item.ItemDrawerKey;
import zornco.bedcraftbeyond.item.ItemRainbowBed;
import zornco.bedcraftbeyond.item.ItemRug;
import zornco.bedcraftbeyond.item.ItemScissors;
import zornco.bedcraftbeyond.item.ItemStoneBed;
import zornco.bedcraftbeyond.util.ClientUtils;
import zornco.bedcraftbeyond.util.PlankHelperClient;

public class ClientProxy extends CommonProxy {

	@Override
	public void registerRenderInformationInit()
	{
		//RenderingRegistry.registerBlockHandler(new BlockRugRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(zornco.bedcraftbeyond.blocks.TileStoneBed.class, new TileStoneBedRenderer());
		ClientRegistry.bindTileEntitySpecialRenderer(zornco.bedcraftbeyond.blocks.TileColoredBed.class, new BlockBedRendererTESR());
		ClientRegistry.bindTileEntitySpecialRenderer(zornco.bedcraftbeyond.blocks.TileRainbowBed.class, new TileRainbowBedRenderer());
	}

	@Override
	public void registerModels() {
		
		ModelLoader.setCustomStateMapper(BedCraftBeyond.bedBlock, (new StateMap.Builder()).ignore(new IProperty[] {BlockBed.OCCUPIED}).ignore(new IProperty[] {BlockBed.PART}).ignore(new IProperty[] {BlockBed.FACING}).build());
		ModelLoader.setCustomStateMapper(BedCraftBeyond.chestBedBlock, (new StateMap.Builder()).ignore(new IProperty[] {BlockBed.OCCUPIED}).ignore(new IProperty[] {BlockBed.PART}).ignore(new IProperty[] {BlockBed.FACING}).build());
		ModelLoader.setCustomStateMapper(BedCraftBeyond.stoneBedBlock, (new StateMap.Builder()).ignore(new IProperty[] {BlockBed.OCCUPIED}).ignore(new IProperty[] {BlockBed.PART}).ignore(new IProperty[] {BlockBed.FACING}).build());
		ModelLoader.setCustomStateMapper(BedCraftBeyond.rainbowBedBlock, (new StateMap.Builder()).ignore(new IProperty[] {BlockBed.OCCUPIED}).ignore(new IProperty[] {BlockBed.PART}).ignore(new IProperty[] {BlockBed.FACING}).build());
		/*ResourceLocation[] resLocs = new ResourceLocation[BlockBCBPlanks.EnumType.values().length];
		for (int i = 0; i < resLocs.length; i++) {
			resLocs[i] = new ResourceLocation(BedCraftBeyond.MOD_ID, "plank_" + BlockBCBPlanks.EnumType.byMetadata(i));
		}
		
		ModelBakery.registerItemVariants(Item.getItemFromBlock(BedCraftBeyond.plankBlock), resLocs);*/

		for (int i = 0; i < BlockBCBPlanks.EnumType.values().length; i++) {

			//registerItemRender(plankItem, i, ((ItemBCBPlank) plankItem).getName());
			ModelBakery.registerItemVariants(Item.getItemFromBlock(BedCraftBeyond.plankBlock), new ResourceLocation(BedCraftBeyond.MOD_ID + ":plank/" + BlockBCBPlanks.EnumType.byMetadata(i)));
			registerBlockModelAsItem(BedCraftBeyond.plankBlock, i, "plank/" + BlockBCBPlanks.EnumType.byMetadata(i));
		}
		//registerBlockModelAsItem(BedCraftBeyond.bedBlock, ((BlockColoredBed) BedCraftBeyond.bedBlock).getName());
		//registerBlockModelAsItem(BedCraftBeyond.chestBedBlock, ((BlockColoredChestBed) BedCraftBeyond.chestBedBlock).getName());
		//registerBlockModelAsItem(BedCraftBeyond.stoneBedBlock, ((BlockStoneBed) BedCraftBeyond.stoneBedBlock).getName());
		for (int i = 0; i < 255; i++) {
			
			registerItemModel(BedCraftBeyond.bedItem, i, ((ItemColoredBed) BedCraftBeyond.bedItem).getName(), "inventory");
			registerItemModel(BedCraftBeyond.chestBedItem, i, ((ItemColoredChestBed) BedCraftBeyond.chestBedItem).getName(), "inventory");
		}

		registerItemModel(BedCraftBeyond.rainbowBedItem, 255, ((ItemRainbowBed) BedCraftBeyond.rainbowBedItem).getName(), "inventory");
		for (int i = 0; i < EnumDyeColor.values().length; i++) {
			registerBlockModelAsItem(BedCraftBeyond.rugBlock, i, "rugBlock");
			//ModelBakery.registerItemVariants(Item.getItemFromBlock(BedCraftBeyond.rugBlock), names);
		}
		
		registerItemModel(BedCraftBeyond.scissors, ((IName) BedCraftBeyond.scissors).getName());
		//registerItemModel(BedCraftBeyond.rugItem, ((IName) BedCraftBeyond.rugItem).getName());
		
		registerItemModel(BedCraftBeyond.drawerKey, ((IName) BedCraftBeyond.drawerKey).getName());
		registerItemModel(BedCraftBeyond.stoneBedItem, ((IName) BedCraftBeyond.stoneBedItem).getName());
		registerItemModel(BedCraftBeyond.suitcase, ((IName) BedCraftBeyond.suitcase).getName());
	}

	public void registerBlockModelAsItem(final Block block, final String blockName)
    {
        registerBlockModelAsItem(block, 0, blockName);
    }

    public void registerBlockModelAsItem(final Block block, final String blockName, final String variantName)
    {
        registerBlockModelAsItem(block, 0, blockName, variantName);
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName)
    {
        registerBlockModelAsItem(block, meta, blockName, "inventory");
    }

    public void registerBlockModelAsItem(final Block block, int meta, final String blockName, final String variantName)
    {
        registerItemModel(Item.getItemFromBlock(block), meta, blockName, variantName);
    }

    public void registerItemModel(final ItemStack stack, final String itemName, final String variantName)
    {
        registerItemModel(stack.getItem(), stack.getMetadata(), itemName, variantName);
    }

    public void registerItemModel(final Item item, final String itemName)
    {
        registerItemModel(item, 0, itemName, "inventory");
    }

    public void registerItemModel(final Item item, int meta, final String itemName, final String variantName)
    {
        ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(BedCraftBeyond.MOD_ID + ":" + itemName, variantName));
    }
	@Override
	public void compilePlanks() {
		super.compilePlanks();

		((IReloadableResourceManager)ClientUtils.mc().getResourceManager()).registerReloadListener(new PlankHelperClient());
	}
	@Override
	public int getAverageBlockColour(ItemStack stack2) {
		return ClientUtils.getAverageBlockColour(stack2);
	}
	@Override
	public World getClientWorld()
	{
		return FMLClientHandler.instance().getClient().theWorld;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID) {
		case GUI_BED:
			TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
			if (te != null && te instanceof TileColoredChestBed)
			{
				return new GuiColoredChestBed(player.inventory, (TileColoredChestBed) te);
			}
		case GUI_SUITCASE:
			return new GuiSuitcase((ContainerSuitcase) new ContainerSuitcase(player, player.inventory, new InventorySuitcase(player.getHeldItem())));
		}
		

		return null;
	}
}
