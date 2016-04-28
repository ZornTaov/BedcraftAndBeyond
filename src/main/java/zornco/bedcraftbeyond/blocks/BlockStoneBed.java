package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.BlockBed;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.Random;

public class BlockStoneBed extends BlockBedBase {

	public BlockStoneBed() {
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setHardness(1.0f);
		setUnlocalizedName("beds.stone");
		setRegistryName(BedCraftBeyond.MOD_ID, "stone_bed");
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

}
