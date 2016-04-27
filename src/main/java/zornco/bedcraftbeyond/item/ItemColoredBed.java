package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.BlockStateBase;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.TileColoredBed;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.util.List;

public class ItemColoredBed extends Item implements IName
{

	//public static final int[] woodColors = new int[] {0xaf8f58, 0x745733, 0xd0c084, 0xac7c58, 0xb46237, 0x442c15};
	//public static final String[] woodType = new String[] {"oak", "spruce", "birch", "jungle", "acacia", "big_oak"};
	public static final String[] colorNames = new String[] {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};
	@SideOnly(Side.CLIENT)
	//protected IIcon[] bedIcon;
	private ItemStack plankType;

	public ItemColoredBed()
	{
		super();
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

	/*@Override
	@SideOnly(Side.CLIENT)*/
	/**
	 * Gets an icon index based on an item's damage value and the given render pass
	 */
	/*public IIcon getIconFromDamageForRenderPass(int par1, int par2)
	{
		return this.bedIcon[par2];
	}*/

	/*@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.bedIcon = new IIcon[3];
		for (int i = 0; i < 3; i++) {
			this.bedIcon[i] = par1IconRegister.registerIcon("bedcraftbeyond:bed_"+i);
		}
	}*/

	/*@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}*/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (String plank :  PlankHelper.getPlankColorMap().keySet())
		{
			ItemStack bed = new ItemStack(par1, 1, 241);
			bed.setTagCompound(new NBTTagCompound());
			PlankHelper.addPlankInfo(bed.getTagCompound(), PlankHelper.plankItemStackfromString(plank));
			par3List.add(bed);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add(ItemColoredBed.colorNames[getColorFromInt(par1ItemStack.getItemDamage(), 2)]+" Blanket");
		par3List.add(ItemColoredBed.colorNames[getColorFromInt(par1ItemStack.getItemDamage(), 1)]+" Sheet");

		this.plankType = PlankHelper.validatePlank(par1ItemStack);
		String name = this.plankType.getDisplayName();
		par3List.add(name+" Frame");

	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return true;
		}
		else if (side != EnumFacing.UP)
		{
			return false;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);
			Block block = iblockstate.getBlock();
			boolean flag = block.isReplaceable(worldIn, pos);

			if (!flag)
			{
				pos = pos.up();
			}

			int i = MathHelper.floor_double((double)(playerIn.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
			EnumFacing enumfacing = EnumFacing.getHorizontal(i);
			BlockPos blockpos = pos.offset(enumfacing);

			if (playerIn.canPlayerEdit(pos, side, stack) && playerIn.canPlayerEdit(blockpos, side, stack))
			{
				boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
				boolean flag2 = flag || worldIn.isAirBlock(pos);
				boolean flag3 = flag1 || worldIn.isAirBlock(blockpos);

				if (flag2 && flag3 &&
								worldIn.getBlockState(pos.down()).isSideSolid(worldIn, pos.down(), EnumFacing.UP) &&
								worldIn.getBlockState(blockpos.down()).isSideSolid(worldIn, blockpos.down(), EnumFacing.UP))
				{
					IBlockState iblockstate1 = BedCraftBeyond.bedBlock.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

					if (worldIn.setBlockState(pos, iblockstate1, 3))
					{
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
						worldIn.setBlockState(blockpos, iblockstate2, 3);
					}


					TileColoredBed tile = (TileColoredBed)worldIn.getTileEntity(pos);
					if (tile != null)
					{
						tile.setColorCombo(stack.getItemDamage());
						tile.setPlankType(PlankHelper.validatePlank(stack));
						//BedCraftBeyond.logger.info(tile.colorCombo+"");
					}
					TileColoredBed tile2 = (TileColoredBed)worldIn.getTileEntity(blockpos);
					if (tile2 != null)
					{
						tile2.setColorCombo(stack.getItemDamage());
						tile2.setPlankType(PlankHelper.validatePlank(stack));
						//BedCraftBeyond.logger.info(tile2.colorCombo+"");
					}
					if(!playerIn.capabilities.isCreativeMode)
						--stack.stackSize;
					return true;
				}
				else
				{
					return false;
				}
			}
			else
			{
				return false;
			}
		}
	}
	*/

	// TODO: Registry should have this now.
	public String getName() {
		return "CbedItem";
	}
}
