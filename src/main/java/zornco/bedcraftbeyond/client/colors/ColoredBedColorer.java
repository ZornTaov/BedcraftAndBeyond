package zornco.bedcraftbeyond.client.colors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemDye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public class ColoredBedColorer implements IBlockColor {

  // PLANKS = 0
  // SHEETS = 1
  // BLANKS = 2

  @Override
  public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
    state = state.getBlock().getActualState(state, world, pos);
    switch(tintIndex){
      case 0:
        return PlankHelper.oakColor;

      case 1:
        return ItemDye.dyeColors[state.getValue(BlockColoredBed.SHEETS).getDyeDamage()];

      case 2:
        return ItemDye.dyeColors[state.getValue(BlockColoredBed.BLANKETS).getDyeDamage()];

      default:
        return 0;
    }
  }
}
