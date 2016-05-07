package zornco.bedcraftbeyond.client.colors;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;

public class ColorSingleLayerRenderer implements IItemColor {

   @Override
   public int getColorFromItemstack(ItemStack stack, int tintIndex) {
      Color c = ColorHelper.getColorFromStack(stack);
      return c.getRGB();
   }
}
