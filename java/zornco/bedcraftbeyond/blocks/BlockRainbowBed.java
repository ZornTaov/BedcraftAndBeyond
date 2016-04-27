package zornco.bedcraftbeyond.blocks;

import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class BlockRainbowBed extends BlockColoredBed {

	public BlockRainbowBed() {
		super();
	}
	@Override
	public TileEntity createNewTileEntity(World world, int metadata)
	{
		return new TileRainbowBed();
	}
	/**
	 * Returns the ID of the items to drop on destruction.
	 */
	@Override
	public Item getItemDropped(IBlockState par1, Random par2Random, int par3)
	{
        return par1.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : BedCraftBeyond.rainbowBedItem;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumFacing par6, float par7, float par8, float par9) {

		if (world.isRemote)
		{
			return true;
		}
		this.onBedActivated(world, pos, state, player);
		world.markBlockForUpdate(pos);
		return true;
	}
	@Override
	@SideOnly(Side.CLIENT)

	/**
	 * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
	 */
	public Item getItem(World par1World, BlockPos pos)
	{
		return BedCraftBeyond.rainbowBedItem;
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

	public String getName() {
		// TODO Auto-generated method stub
		return "RbedBlock";
	}

}
