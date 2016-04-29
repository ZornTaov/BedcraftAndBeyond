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

	public Item getBedItem(IBlockState state){ return BedCraftBeyond.stoneBedItem; }

}
