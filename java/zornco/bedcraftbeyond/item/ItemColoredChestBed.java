package zornco.bedcraftbeyond.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.BlockColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public class ItemColoredChestBed extends ItemColoredBed {

	public ItemColoredChestBed() {
		super();
	}
	/*@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.bedIcon = new IIcon[3];
		for (int i = 0; i < 3; i++) {
			this.bedIcon[i] = par1IconRegister.registerIcon("bedcraftbeyond:bed_chest_"+i);
		}
	}*/
	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS !
	 */
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

				if (flag2 && flag3 && World.doesBlockHaveSolidTopSurface(worldIn, pos.down()) && World.doesBlockHaveSolidTopSurface(worldIn, blockpos.down()))
				{
					IBlockState iblockstate1 = BedCraftBeyond.chestBedBlock.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

					if (worldIn.setBlockState(pos, iblockstate1, 3))
					{
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
						worldIn.setBlockState(blockpos, iblockstate2, 3);
					}

					TileColoredChestBed tile = (TileColoredChestBed)worldIn.getTileEntity(pos);
					if (tile != null)
					{
						tile.setColorCombo(stack.getItemDamage());
						tile.setOwnerName(playerIn.getGameProfile().getName());
						tile.setPlankType(PlankHelper.validatePlank(stack));
						//BedCraftBeyond.logger.info(tile.colorCombo+"");
					}
					TileColoredChestBed tile2 = (TileColoredChestBed)worldIn.getTileEntity(blockpos);
					if (tile2 != null)
					{
						tile2.setColorCombo(stack.getItemDamage());
						tile2.setOwnerName(playerIn.getGameProfile().getName());
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
	public String getName() {
		return "CCbedItem";
	}
}
