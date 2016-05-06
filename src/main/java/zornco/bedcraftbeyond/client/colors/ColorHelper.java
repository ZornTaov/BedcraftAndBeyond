package zornco.bedcraftbeyond.client.colors;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import java.awt.*;

public class ColorHelper {

   public static Color getColorFromStack(ItemStack stack){
      if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color")) return Color.WHITE;
      NBTTagCompound color = stack.getTagCompound().getCompoundTag("color");
      try { return new Color(color.getInteger("r"), color.getInteger("b"), color.getInteger("g")); }
      catch(Exception e){ return Color.WHITE; }
   }

   public static String getFormattedColorValues(Color c){
      return TextFormatting.RED + "" + c.getRed() + TextFormatting.WHITE + ", " +
              TextFormatting.GREEN + c.getGreen() + TextFormatting.WHITE + ", " +
              TextFormatting.BLUE + c.getBlue() + TextFormatting.RESET;
   }
}
