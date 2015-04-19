package zornco.bedcraftbeyond.blocks;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockStoneBed extends BlockColoredBed {

	public BlockStoneBed() {
		super();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int par1, int par2) {
		return Blocks.stone_slab.getIcon(par1, par2);
	}

	@Override
	public int getRenderType() {
		return -1;
	}

	@Override
	public Item getItemDropped(int par1, Random par2Random, int par3) 
	{
		return BedCraftBeyond.stoneBedItem;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		return new TileStoneBed();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Item getItem(World par1World, int par2, int par3, int par4) 
	{
		return super.getItem(par1World, par2, par3, par4);
	}

}
