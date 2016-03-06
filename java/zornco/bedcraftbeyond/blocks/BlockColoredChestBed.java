package zornco.bedcraftbeyond.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.item.ItemDrawerKey;

public class BlockColoredChestBed extends BlockColoredBed {

	public BlockColoredChestBed() {
		super();
		this.isBlockContainer = true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing par6, float par7, float par8, float par9) 
	{
		if (player.getHeldItem() == null || (player.getHeldItem() != null && player.getHeldItem().getItem() != null && !(player.getHeldItem().getItem() instanceof ItemDrawerKey)))
		{
			super.onBlockActivated( world, pos, state, player, par6, par7, par8, par9);
			return true;
		}
		TileColoredChestBed tile = (TileColoredChestBed)world.getTileEntity(pos);

		if (tile == null)
		{
			return true;
		}

		if (world.isRemote)
		{
			return true;
		}

		//BedCraftBeyond.logger.info(tile.colorCombo+"");
		player.openGui(BedCraftBeyond.instance, 0, world, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}
	@Override
	public Item getBedItem() {
		return BedCraftBeyond.chestBedItem;
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		world.markBlockForUpdate(pos);
	}
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileColoredChestBed();
	}
	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public Item getItemDropped(IBlockState par1, Random par2Random, int par3)
	{
        return par1.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : BedCraftBeyond.chestBedItem;
	}
	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public Item getItem(World par1World, BlockPos pos)
	{
		return BedCraftBeyond.chestBedItem;
	}
	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileColoredChestBed tileentitychest = (TileColoredChestBed) world.getTileEntity(pos);
		if (tileentitychest != null)
		{
			dropContent(0, tileentitychest, world, tileentitychest.getPos());
		}
		super.breakBlock(world, pos, state);
	}

	public void dropContent(int newSize, IInventory chest, World world, BlockPos pos)
	{
		for (int l = newSize; l < chest.getSizeInventory(); l++)
		{
			ItemStack itemstack = chest.getStackInSlot(l);
			if (itemstack == null)
			{
				continue;
			}
			float f = random.nextFloat() * 0.8F + 0.1F;
			float f1 = random.nextFloat() * 0.8F + 0.1F;
			float f2 = random.nextFloat() * 0.8F + 0.1F;
			while (itemstack.stackSize > 0)
			{
				int i1 = random.nextInt(21) + 10;
				if (i1 > itemstack.stackSize)
				{
					i1 = itemstack.stackSize;
				}
				itemstack.stackSize -= i1;
				EntityItem entityitem = new EntityItem(world, pos.getX() + f, (float) pos.getY() + (newSize > 0 ? 1 : 0) + f1, pos.getZ() + f2,
						new ItemStack(itemstack.getItem(), i1, itemstack.getItemDamage()));
				float f3 = 0.05F;
				entityitem.motionX = (float) random.nextGaussian() * f3;
				entityitem.motionY = (float) random.nextGaussian() * f3 + 0.2F;
				entityitem.motionZ = (float) random.nextGaussian() * f3;
				if (itemstack.hasTagCompound())
				{
					entityitem.getEntityItem().setTagCompound((NBTTagCompound) itemstack.getTagCompound().copy());
				}
				world.spawnEntityInWorld(entityitem);
			}
		}
	}

	/*@Override
	@SideOnly(Side.CLIENT)*/
	/**
	 * When this method is called, your block should register all the icons it needs with the given IconRegister. This
	 * is the only chance you get to register icons.
	 */
	/*public void registerBlockIcons(IIconRegister par1IconRegister)
	{
		super.registerBlockIcons(par1IconRegister);
		this.bedEndIcons[2][0] = par1IconRegister.registerIcon("bedcraftbeyond:chest_bed_feet_end_2");
		this.bedEndIcons[2][1] = par1IconRegister.registerIcon("bedcraftbeyond:chest_bed_head_end_2");
		this.bedSideIcons[2][0] = par1IconRegister.registerIcon("bedcraftbeyond:chest_bed_feet_side_2");
		this.bedSideIcons[2][1] = par1IconRegister.registerIcon("bedcraftbeyond:chest_bed_head_side_2");
	}*/
	/**
	 * Called when the block receives a BlockEvent - see World.addBlockEvent. By default, passes it on to the tile
	 * entity at this location. Args: world, x, y, z, blockID, EventID, event parameter
	 */
	@Override
	public boolean onBlockEventReceived(World par1World, BlockPos pos, IBlockState state, int par5, int par6)
	{
		super.onBlockEventReceived(par1World, pos, state, par5, par6);
		TileEntity tileentity = par1World.getTileEntity(pos);
		return tileentity != null ? tileentity.receiveClientEvent(par5, par6) : false;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return "CCbedBlock";
	}
}
