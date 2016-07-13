package zornco.bedcraftbeyond.parts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3f;

public interface IPartAcceptor {

    boolean canAcceptPart(World world, IBlockState state, BlockPos pos, EnumFacing side, Vector3f hit, ItemStack stack);

    ItemStack addPart(World world, IBlockState state, BlockPos pos, EnumFacing side, Vector3f hit, ItemStack stack);
}
