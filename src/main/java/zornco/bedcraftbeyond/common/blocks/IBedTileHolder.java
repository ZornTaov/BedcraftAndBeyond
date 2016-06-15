package zornco.bedcraftbeyond.common.blocks;

import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public interface IBedTileHolder {
    TileEntity getTileForBed(IBlockAccess world, IBlockState state, BlockPos pos);
}
