package zornco.bedcraftbeyond.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.util.ColorHelper;
import zornco.bedcraftbeyond.util.PlankHelper;

import java.awt.*;

public class Colors {

   public static class DyeColorizer implements IItemColor {
      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {
         return ItemDye.dyeColors[EnumDyeColor.byDyeDamage(stack.getItemDamage()).getMetadata()];
      }
   }

   public static class LinenColorer implements IItemColor {
      @Override
      public int getColorFromItemstack(ItemStack stack, int tintIndex) {
         Color c = ColorHelper.getColorFromStack(stack);
         return c.getRGB();
      }
   }

   public static class BedColorizer implements IBlockColor {
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

}
