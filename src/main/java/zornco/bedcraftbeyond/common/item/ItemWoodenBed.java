package zornco.bedcraftbeyond.common.item;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.List;

public class ItemWoodenBed extends ItemBedPlacer implements IItemColor {

	public ItemWoodenBed(Block b) {
		super(b);
		setUnlocalizedName("frames.wooden");
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
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> tags, boolean advanced) {
		tags.add("Frame: ");
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)	return EnumActionResult.SUCCESS;
		if (side != EnumFacing.UP) return EnumActionResult.FAIL;

		boolean canPlaceBedHere = testSimpleBedPlacement(worldIn, playerIn, pos, stack);
		if(!canPlaceBedHere) return EnumActionResult.FAIL;

		pos = pos.up();
		try { placeSimpleBedBlocks(worldIn, playerIn, pos, BcbBlocks.woodenBed, stack); }
		catch(Exception e) { return EnumActionResult.FAIL; }

		TileColoredBed tileTopHalf = BlockWoodenBed.getTileEntity(worldIn, pos);
		if (tileTopHalf != null) {
			// tileTopHalf.setBlanketsColor(BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.BLANKETS));
			// tileTopHalf.setLinenPart(BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.SHEETS));
			tileTopHalf.setPlankType(PlankHelper.validatePlank(stack));
		}

		// If not creative mode, remove placer item
		if(!playerIn.capabilities.isCreativeMode) --stack.stackSize;
		if(stack.stackSize < 1) playerIn.setHeldItem(hand, null);

		return EnumActionResult.SUCCESS;
	}

	@Override
	public int getColorFromItemstack(ItemStack stack, int tintIndex) {
		switch (tintIndex)
		{
			case 0:
				return 0; // ItemDye.dyeColors[BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.BLANKETS).ordinal()];
			case 1:
				return 0; // ItemDye.dyeColors[BlockWoodenBed.getPartColorFromItem(stack, BlockWoodenBed.EnumColoredPart.SHEETS).ordinal()];
			case 2:
				// TODO: Fix plank colors
				// return ItemWoodenBed.woodColors[ItemWoodenBed.getColorFromInt(par1ItemStack.getItemDamage(), 0)];
				// return PlankHelper.getPlankColor(PlankHelper.plankStringfromItemStack(PlankHelper.validatePlank(par1ItemStack)));
				return PlankHelper.oakColor;
			default:
				return 0;
		}
	}
}
