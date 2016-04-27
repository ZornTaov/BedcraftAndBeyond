package zornco.bedcraftbeyond.item;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.TileColoredChestBed;
import zornco.bedcraftbeyond.blocks.TileRainbowBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public class ItemRainbowBed extends ItemColoredBed {
	
	public ItemRainbowBed() {
		super();
	}

	@Override
	public int getDefaultColor() {
		// TODO Auto-generated method stub
		return 255;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4)
	{
		par3List.add("RAINBOW Blanket");
		par3List.add("RAINBOW Sheet");

		this.plankType = PlankHelper.validatePlank(par1ItemStack);
		String name = this.plankType.getDisplayName();
		par3List.add(name+" Frame");

	}
	@Override
	public boolean hasEffect(ItemStack stack) {
		return true;
	}
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
					IBlockState iblockstate1 = BedCraftBeyond.rainbowBedBlock.getDefaultState().withProperty(BlockBed.OCCUPIED, Boolean.valueOf(false)).withProperty(BlockBed.FACING, enumfacing).withProperty(BlockBed.PART, BlockBed.EnumPartType.FOOT);

					if (worldIn.setBlockState(pos, iblockstate1, 3))
					{
						IBlockState iblockstate2 = iblockstate1.withProperty(BlockBed.PART, BlockBed.EnumPartType.HEAD);
						worldIn.setBlockState(blockpos, iblockstate2, 3);
					}

					TileRainbowBed tile = (TileRainbowBed)worldIn.getTileEntity(pos);
					Random random = new Random();
					int color1 = random.nextInt(600), color2 = random.nextInt(600);
					if (tile != null)
					{
						tile.setColorCombo(255);
						tile.setPlankType(PlankHelper.validatePlank(stack));
						tile.setRainbowColor1(color1);
						tile.setRainbowColor2(color2);
						//BedCraftBeyond.logger.info(tile.colorCombo+"");
					}
					TileRainbowBed tile2 = (TileRainbowBed)worldIn.getTileEntity(blockpos);
					if (tile2 != null)
					{
						tile2.setColorCombo(255);
						tile2.setPlankType(PlankHelper.validatePlank(stack));
						tile2.setRainbowColor1(color1);
						tile2.setRainbowColor2(color2);
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
		return "RbedItem";
	}
}
