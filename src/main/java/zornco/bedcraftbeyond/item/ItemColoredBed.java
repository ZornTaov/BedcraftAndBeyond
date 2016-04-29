package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
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
import zornco.bedcraftbeyond.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.util.BedUtils;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.List;

public class ItemColoredBed extends ItemBlock implements IItemColor {

	//public static final int[] woodColors = new int[] {0xaf8f58, 0x745733, 0xd0c084, 0xac7c58, 0xb46237, 0x442c15};
	//public static final String[] woodType = new String[] {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};

	@SideOnly(Side.CLIENT)
	//protected IIcon[] bedIcon;
	private ItemStack plankType;

	public ItemColoredBed(Block b) {
		super(b);
		setUnlocalizedName("beds.colored");
		setRegistryName(b.getRegistryName());
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setMaxStackSize(1);
		this.setHasSubtypes(true);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubItems(Item item, CreativeTabs tab, List subItems)
	{
		if(tab instanceof TabBeds) {
			for (String plank : PlankHelper.getPlankColorMap().keySet()) {
				ItemStack bed = new ItemStack(item, 1, 241);
				bed.setTagCompound(new NBTTagCompound());
				PlankHelper.addPlankInfo(bed.getTagCompound(), PlankHelper.plankItemStackfromString(plank));
				subItems.add(bed);
			}
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

		IBlockState bedFootState = BedCraftBeyond.coloredBedBlock.getDefaultState()
						.withProperty(BlockBed.OCCUPIED, false)
						.withProperty(BlockBed.FACING, playerIn.getHorizontalFacing())
						.withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

		// TODO: Figure out why placement is not working. WTF.
		if (worldIn.setBlockState(btmHalf, bedFootState, 3)) {
			IBlockState bedHeadState = bedFootState
							// .withProperty(BlockBed.FACING, playerIn.getHorizontalFacing().getOpposite())
							.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
			boolean success = worldIn.setBlockState(topHalf, bedHeadState, 3);
			if(!success) return EnumActionResult.FAIL;
		}

		TileColoredBed tileTopHalf = BlockColoredBed.getTileEntity(worldIn, topHalf);
		if (tileTopHalf != null) {
			tileTopHalf.setBlanketsColor(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.BLANKETS));
			tileTopHalf.setSheetsColor(BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.SHEETS));
			tileTopHalf.setPlankType(PlankHelper.validatePlank(stack));
		}

		// If not creative mode, remove placer item
		if(!playerIn.capabilities.isCreativeMode) --stack.stackSize;

		return EnumActionResult.SUCCESS;
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		switch (tintIndex)
		{
			case 0:
				return ItemDye.dyeColors[BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.BLANKETS).ordinal()];
			case 1:
				return ItemDye.dyeColors[BedUtils.getColor(stack, BedUtils.EnumColoredBedPiece.SHEETS).ordinal()];
			case 2:
				// TODO: Fix plank colors
				// return ItemColoredBed.woodColors[ItemColoredBed.getColorFromInt(par1ItemStack.getItemDamage(), 0)];
				// return PlankHelper.getPlankColor(PlankHelper.plankStringfromItemStack(PlankHelper.validatePlank(par1ItemStack)));
				return PlankHelper.oakColor;
			default:
				return 0;
		}
	}
}
