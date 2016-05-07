package zornco.bedcraftbeyond.util;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 *
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * @author Xiaoxiao Li
 *
 */
public class ColorHelper {

   public static HashMap<Color, String> colorList;

   /**
    * Initialize the color list that we have.
    * All added colors are HEX values.
    */
   public static void initColorList() {
      if(colorList != null && !colorList.isEmpty()) return;
      colorList = new HashMap<>();

      // "Default" wool colors
      colorList.put(new Color(0xFF, 0xFF, 0xFF), "White");
      colorList.put(new Color(0x99, 0x99, 0x99), "Silver");
      colorList.put(new Color(0x4C, 0x4C, 0x4C), "Gray");
      colorList.put(new Color(0x00, 0x00, 0x00), "Black");
      colorList.put(new Color(0xB2, 0x22, 0x22), "Red");
      colorList.put(new Color(0xC6, 0x62, 0x0F), "Orange");
      colorList.put(new Color(0xFF, 0xFF, 0x17), "Yellow");
      colorList.put(new Color(0x4A, 0xFF, 0x00), "Light Green");  // Lime
      colorList.put(new Color(0x22, 0x8B, 0x22), "Green");
      colorList.put(new Color(0x00, 0xBF, 0xFF), "Light Blue");
      colorList.put(new Color(0x00, 0x8B, 0x8B), "Cyan");
      colorList.put(new Color(0x06, 0x2A, 0x78), "Blue");         // Lapis
      colorList.put(new Color(0xE0, 0x21, 0xEE), "Pink");
      colorList.put(new Color(0xFF, 0x00, 0xFF), "Magenta");
      colorList.put(new Color(0x80, 0x00, 0x80), "Purple");
      colorList.put(new Color(0x3D, 0x0F, 0x02), "Brown");



      // Additional colors!
      colorList.put(new Color(0xDA, 0xA5, 0x20), "Goldenrod");
      colorList.put(new Color(0x40, 0xE0, 0xD0), "Turquoise");
      colorList.put(new Color(0xEE, 0x82, 0xEE), "Violet");
      colorList.put(new Color(0xFF, 0x00, 0x63), "Crimson");

      BedCraftBeyond.logger.info("=======================================");
      BedCraftBeyond.logger.info("Registered " + colorList.size() + " colors.");
      BedCraftBeyond.logger.info("=======================================");
   }

   /**
    * Get the closest color name from our list
    *
    * @param r
    * @param g
    * @param b
    * @return
    */
   public static String getColorNameFromRgb(int r, int g, int b) {
      return getColorNameFromColor(new Color(r, g, b), 1000);
   }

   public static String getColorNameFromColor(Color color) {
      return getColorNameFromColor(color, 1000);
   }

   public static String getColorNameFromColor(Color color, int minMSE) {
      Color closestMatch = null;
      int mse;
      for (Color c : colorList.keySet()) {
         mse = computeMSE(c, color);
         if (mse < minMSE) {
            minMSE = mse;
            closestMatch = c;
         }
      }

      if (closestMatch != null)
         return colorList.get(closestMatch);

      return null;
   }

   private static int computeMSE(Color c1, Color c2) {
      return  Math.abs(c1.getRed() - c2.getRed()) +
              Math.abs(c1.getGreen() - c2.getGreen()) +
              Math.abs(c1.getBlue() - c2.getBlue());
   }

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