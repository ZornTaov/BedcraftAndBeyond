package zornco.bedcraftbeyond.blocks;

import java.util.Random;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class BlockStoneBed extends BlockColoredBed {

	public BlockStoneBed() {
		super();
	}

/*	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return Blocks.stone_slab.getIcon(par1, par2);
	}*/

	@Override
	public Item getItemDropped(IBlockState par1, Random par2Random, int par3)
	{
        return par1.getValue(PART) == BlockBed.EnumPartType.HEAD ? null : BedCraftBeyond.stoneBedItem;
	}

	public Item getBedItem() {
		return BedCraftBeyond.stoneBedItem;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileStoneBed();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World par1World, BlockPos pos) 
	{
		return BedCraftBeyond.stoneBedItem;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "SbedBlock";
	}

}
