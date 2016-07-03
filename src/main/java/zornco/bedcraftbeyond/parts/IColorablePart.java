package zornco.bedcraftbeyond.parts;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.awt.*;

public interface IColorablePart {
    interface IColorableItem {
        Color getPartColor(ItemStack stack);
    }

    interface IColorableBlock {
        Color getPartColor(IBlockAccess world, IBlockState state, BlockPos pos);
    }
}
