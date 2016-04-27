package zornco.bedcraftbeyond.item;

public class ItemColoredChestBed extends ItemColoredBed {

	public ItemColoredChestBed() {
		super();
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
	*/
}
