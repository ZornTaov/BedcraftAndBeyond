package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.blocks.BlockRug;

import java.util.List;

public class ItemRug extends ItemBlock {

	public static final String[] rugColorNames = new String[] {"Black", "Red", "Green", "Brown", "Blue", "Purple", "Cyan", "LightGray", "Gray", "Pink", "Lime", "Yellow", "LightBlue", "Magenta", "Orange", "White"};
	//public static final String[] rugIconNames = new String[] {"rug_black", "rug_red", "rug_green", "rug_brown", "rug_blue", "rug_purple", "rug_cyan", "rug_silver", "rug_gray", "rug_pink", "rug_lime", "rug_yellow", "rug_lightBlue", "rug_magenta", "rug_orange", "rug_white"};

	/*@SideOnly(Side.CLIENT)
	private IIcon[] rugIconList;*/

	public ItemRug(Block par2) {
		super(par2);
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
	}
	
	/**
	 * Returns the metadata of the block which this Item (ItemBlock) can place
	 */
	@Override
	public int getMetadata(int par1)
	{
		return par1;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		return super.getUnlocalizedName() + "." + ItemRug.rugColorNames[BlockRug.getBlockFromDye(stack.getItemDamage())];
	}

	// TODO: Move to IItemColor
	/*@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		return ItemDye.dyeColors[EnumDyeColor.byDyeDamage(par1ItemStack.getItemDamage()).getMetadata()];
	}*/

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
	 */
	public void getSubItems(Item par1, CreativeTabs par2CreativeTabs, List par3List)
	{
		for (int var4 = 0; var4 < 16; ++var4)
		{
			par3List.add(new ItemStack(par1, 1, var4));
		}
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	/*@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ)
	{
		Block var11 = worldIn.getBlock(pos);

		if (var11 == Blocks.snow)
		{
			side = 1;
		}
		else if (var11 != Blocks.vine && var11 != Blocks.tallgrass && var11 != Blocks.deadbush
				&& (var11 == null || !var11.isReplaceable(worldIn, pos)))
		{
			if (side == 0)
			{
				--par5;
			}

			if (side == 1)
			{
				++par5;
			}

			if (side == 2)
			{
				--par6;
			}

			if (side == 3)
			{
				++par6;
			}

			if (side == 4)
			{
				--par4;
			}

			if (side == 5)
			{
				++par4;
			}
		}

		if (stack.stackSize == 0)
		{
			return false;
		}
		else if (!playerIn.canPlayerEdit(pos, side, stack))
		{
			return false;
		}
		else if (par5 == 255 && this.block.getMaterial().isSolid())
		{
			return false;
		}
		else if (worldIn.canPlaceEntityOnSide(this.block, pos, false, side, playerIn, stack))
		{
			Block var12 = this.block;
			int var13 = this.getMetadata(stack.getItemDamage());
			IBlockState var14 = this.block.onBlockPlaced(worldIn, pos, side, hitX, hitY, hitZ, var13, playerIn);

			if (placeBlockAt(stack, playerIn, worldIn, pos, side, hitX, hitY, hitZ, var14))
			{
				worldIn.playSoundEffect(par4 + 0.5F, par5 + 0.5F, par6 + 0.5F, var12.stepSound.func_150496_b(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
				if(!playerIn.capabilities.isCreativeMode)
	        		--stack.stackSize;
			}

			return true;
		}
		else
		{
			return false;
		}
	}*/

	//@SideOnly(Side.CLIENT)

	/**
	 * Returns true if the given ItemBlock can be placed on the given side of the given block position.
	 */
	/*public boolean canPlaceItemBlockOnSide(World par1World, int par2, int par3, int par4, int par5, EntityPlayer par6EntityPlayer, ItemStack par7ItemStack)
	{
		Block var8 = par1World.getBlock(par2, par3, par4);

		if (var8 == Blocks.snow)
		{
			par5 = 1;
		}
		else if (var8 != Blocks.vine && var8 != Blocks.tallgrass && var8 != Blocks.deadbush
				&& (var8 == null || !var8.isReplaceable(par1World, par2, par3, par4)))
		{
			if (par5 == 0)
			{
				--par3;
			}

			if (par5 == 1)
			{
				++par3;
			}

			if (par5 == 2)
			{
				--par4;
			}

			if (par5 == 3)
			{
				++par4;
			}

			if (par5 == 4)
			{
				--par2;
			}

			if (par5 == 5)
			{
				++par2;
			}
		}

		return par1World.canPlaceEntityOnSide(this.block, par2, par3, par4, false, par5, (Entity)null, par7ItemStack);
	}*/

	@Override
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState) {
		/*if (!world.setBlock(x, y, z, this.block, metadata, 3))
		{
			return false;
		}

		if (world.getBlock(x, y, z) == this.block)
		{
			this.block.onBlockPlacedBy(world, x, y, z, player, stack);
			this.block.onPostBlockPlaced(world, x, y, z, metadata);
		}

		return true; */
		return false;
	}
}
