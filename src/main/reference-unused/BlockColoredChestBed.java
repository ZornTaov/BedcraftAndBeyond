package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.blocks.tiles.TileBedcraftChestBed;
import zornco.bedcraftbeyond.item.ItemDrawerKey;

import java.util.Random;

public class BlockColoredChestBed extends BlockColoredBed {

	public BlockColoredChestBed() {
		this.isBlockContainer = true;
		setHardness(1.0f);
		setUnlocalizedName("beds.colored.withChest");
		setRegistryName(BedCraftBeyond.MOD_ID, "colored_chest_bed");
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (heldItem == null || (heldItem != null && heldItem.getItem() != null && !(heldItem.getItem() instanceof ItemDrawerKey)))
		{
			super.onBlockActivated(world, pos, state, player, hand, heldItem, side, hitX, hitY, hitZ);
			return true;
		}
		TileBedcraftChestBed tile = (TileBedcraftChestBed)world.getTileEntity(pos);

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
	public void onBlockAdded(World world, BlockPos pos, IBlockState state)
	{
		super.onBlockAdded(world, pos, state);
		// world.markBlockForUpdate(pos);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileBedcraftChestBed();
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
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(BedCraftBeyond.chestBedItem);
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state)
	{
		TileBedcraftChestBed tileentitychest = (TileBedcraftChestBed) world.getTileEntity(pos);
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
}
