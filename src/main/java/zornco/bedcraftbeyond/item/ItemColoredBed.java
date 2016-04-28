package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.tiles.TileBedcraftBed;
import zornco.bedcraftbeyond.util.BedUtils;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.List;

public class ItemColoredBed extends ItemBlock {

	//public static final int[] woodColors = new int[] {0xaf8f58, 0x745733, 0xd0c084, 0xac7c58, 0xb46237, 0x442c15};
	//public static final String[] woodType = new String[] {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};

	@SideOnly(Side.CLIENT)
	//protected IIcon[] bedIcon;
	private ItemStack plankType;

	// TODO: Fix block placement

	public ItemColoredBed(Block b) {
		super(b);
		setUnlocalizedName("CbedItem");
		setRegistryName(b.getRegistryName());
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	/*@Override
	public int getRenderPasses(int metadata)
	{
		return requiresMultipleRenderPasses() ? 3 : 1;
	}*/

	// TODO: Use new IItemColor interface
	/*@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		switch (par2)
		{
		case 0:
			return ItemDye.dyeColors[ItemColoredBed.getColorFromInt(par1ItemStack.getItemDamage(), 2)];
		case 1:
			return ItemDye.dyeColors[ItemColoredBed.getColorFromInt(par1ItemStack.getItemDamage(), 1)];
		case 2:
			//return ItemColoredBed.woodColors[ItemColoredBed.getColorFromInt(par1ItemStack.getItemDamage(), 0)];
			return PlankHelper.getPlankColor(PlankHelper.plankStringfromItemStack(PlankHelper.validatePlank(par1ItemStack)));
		}
		return 0xFF00FF;
	}*/

	public static int getColorFromInt(int meta, int color)
	{
		switch (color)
		{
		case 0:
			return meta >> 8;
		case 1:
			return meta >> 4 & 0xF;
		case 2:
			return meta & 0xF;
		}
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		for (String plank :  PlankHelper.getPlankColorMap().keySet())
		{
			ItemStack bed = new ItemStack(item, 1, 241);
			bed.setTagCompound(new NBTTagCompound());
			PlankHelper.addPlankInfo(bed.getTagCompound(), PlankHelper.plankItemStackfromString(plank));
			subItems.add(bed);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack stack, EntityPlayer player, List tags, boolean advanced)
	{
		tags.add(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.BLANKETS).getName() +" Blanket");
		tags.add(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.SHEETS).getName() +" Sheet");

		this.plankType = PlankHelper.validatePlank(stack);
		String name = this.plankType.getDisplayName();
		tags.add(name+" Frame");

	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		// pos is position block will be placed at (bottom half of bed)
		// If we're on the client or not trying to place on the top of a block, fail
		if (worldIn.isRemote)	return EnumActionResult.SUCCESS;
		if (side != EnumFacing.UP) return EnumActionResult.FAIL;

		boolean canPlaceBedHere = BedUtils.testBedPlacement(worldIn, playerIn, pos, stack);
		if(!canPlaceBedHere) return EnumActionResult.FAIL;

		BlockPos btmHalf = pos.up();
		BlockPos topHalf = btmHalf.offset(playerIn.getHorizontalFacing());

		// TODO: Look into possible fix for head versus foot state data being wrong
		IBlockState bedFootState = BedCraftBeyond.coloredBedBlock.getDefaultState()
						.withProperty(BlockBed.OCCUPIED, false)
						.withProperty(BlockBed.FACING, playerIn.getHorizontalFacing())
						.withProperty(BlockColoredBed.BlanketColor, BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.BLANKETS))
						.withProperty(BlockColoredBed.SheetColor, BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.SHEETS))
						.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

		if (worldIn.setBlockState(btmHalf, bedFootState, 3)) {
			IBlockState bedHeadState = bedFootState.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
			worldIn.setBlockState(topHalf, bedHeadState, 3);
		}

		TileBedcraftBed tileBtmHalf = (TileBedcraftBed) worldIn.getTileEntity(btmHalf);
		if (tileBtmHalf != null) {
			tileBtmHalf.setBlanketsColor(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.BLANKETS));
			tileBtmHalf.setSheetsColor(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.SHEETS));
			tileBtmHalf.setPlankType(PlankHelper.validatePlank(stack));
		}

		TileBedcraftBed tileTopHalf = (TileBedcraftBed) worldIn.getTileEntity(topHalf);
		if (tileTopHalf != null) {
			tileBtmHalf.setBlanketsColor(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.BLANKETS));
			tileBtmHalf.setSheetsColor(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.SHEETS));
			tileTopHalf.setPlankType(PlankHelper.validatePlank(stack));
		}

		// If not creative mode, remove placer item
		if(!playerIn.capabilities.isCreativeMode) --stack.stackSize;

		return EnumActionResult.SUCCESS;
	}
}
