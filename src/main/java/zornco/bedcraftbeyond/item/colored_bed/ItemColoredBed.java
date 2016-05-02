package zornco.bedcraftbeyond.item.colored_bed;

import net.minecraft.block.Block;
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
import zornco.bedcraftbeyond.blocks.BcbBlocks;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.blocks.tiles.TileColoredBed;
import zornco.bedcraftbeyond.client.tabs.TabBeds;
import zornco.bedcraftbeyond.item.ItemBedPlacer;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.List;

public class ItemColoredBed extends ItemBedPlacer implements IItemColor {

	public ItemColoredBed() {
		super(BcbBlocks.coloredBed);
		setUnlocalizedName("beds.colored");
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
	public void addInformation(ItemStack stack, EntityPlayer player, List tags, boolean advanced) {
		tags.add(BlockColoredBed.getPartColorFromItem(stack, BlockColoredBed.EnumColoredPart.BLANKETS).getName() +" blanket");
		tags.add(BlockColoredBed.getPartColorFromItem(stack, BlockColoredBed.EnumColoredPart.SHEETS).getName() +" sheet");
	}

	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (worldIn.isRemote)	return EnumActionResult.SUCCESS;
		if (side != EnumFacing.UP) return EnumActionResult.FAIL;

		boolean canPlaceBedHere = testSimpleBedPlacement(worldIn, playerIn, pos, stack);
		if(!canPlaceBedHere) return EnumActionResult.FAIL;

		pos = pos.up();
		try { placeSimpleBedBlocks(worldIn, playerIn, pos, BcbBlocks.coloredBed, stack); }
		catch(Exception e) { return EnumActionResult.FAIL; }

		TileColoredBed tileTopHalf = BlockColoredBed.getTileEntity(worldIn, pos);
		if (tileTopHalf != null) {
			tileTopHalf.setBlanketsColor(BlockColoredBed.getPartColorFromItem(stack, BlockColoredBed.EnumColoredPart.BLANKETS));
			tileTopHalf.setSheetsColor(BlockColoredBed.getPartColorFromItem(stack, BlockColoredBed.EnumColoredPart.SHEETS));
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
				return ItemDye.dyeColors[BlockColoredBed.getPartColorFromItem(stack, BlockColoredBed.EnumColoredPart.BLANKETS).ordinal()];
			case 1:
				return ItemDye.dyeColors[BlockColoredBed.getPartColorFromItem(stack, BlockColoredBed.EnumColoredPart.SHEETS).ordinal()];
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
