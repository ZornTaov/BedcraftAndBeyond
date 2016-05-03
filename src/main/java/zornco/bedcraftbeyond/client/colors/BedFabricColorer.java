package zornco.bedcraftbeyond.client.colors;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.ItemDye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.util.PlankHelper;

public class BedFabricColorer implements IBlockColor {

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
        EnumBedFabricType sheetsType = state.getValue(BlockWoodenBed.SHEETS);
        return sheetsType.isDyeType() ? sheetsType.getDyeColor() : 0;

      case 2:
        EnumBedFabricType blanketsType = state.getValue(BlockWoodenBed.BLANKETS);
        return blanketsType.isDyeType() ? blanketsType.getDyeColor() : 0;

      default:
        return 0;
    }
  }
}
