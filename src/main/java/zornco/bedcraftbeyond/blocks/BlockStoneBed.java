package zornco.bedcraftbeyond.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.item.BcbItems;

import java.util.ArrayList;
import java.util.List;

public class BlockStoneBed extends BlockBedBase {

	public BlockStoneBed() {
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setHardness(1.0f);
		setUnlocalizedName("beds.stone");
		setRegistryName(BedCraftBeyond.MOD_ID, "stone_bed");
		setDefaultState(getDefaultState().withProperty(HEAD, false).withProperty(FACING, EnumFacing.NORTH));
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ArrayList<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(BcbItems.stoneBed));
		return drops;
	}
}
