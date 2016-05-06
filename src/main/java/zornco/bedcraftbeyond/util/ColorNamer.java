package zornco.bedcraftbeyond.util;

import net.minecraft.item.EnumDyeColor;
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
public class ColorNamer {

   public static HashMap<Color, String> colorList;
   /**
    * Initialize the color list that we have.
    */
   public static void initColorList() {
      if(colorList != null && !colorList.isEmpty()) return;
      colorList = new HashMap<>();

      // "Default" wool colors
      colorList.put(Color.WHITE, "White");
      colorList.put(Color.ORANGE, "Orange");
      colorList.put(Color.MAGENTA, "Magenta");
      colorList.put(new Color(0xAD, 0xD8, 0xE6), "Light Blue");
      colorList.put(Color.YELLOW, "Yellow");
      colorList.put(new Color(0x90, 0xEE, 0x90), "Light Green"); // Lime
      colorList.put(Color.PINK, "Pink");
      colorList.put(Color.GRAY, "Gray");
      colorList.put(Color.LIGHT_GRAY, "Light Gray"); // Silver
      colorList.put(Color.CYAN, "Cyan");
      colorList.put(new Color(0x80, 0x00, 0x80), "Purple");
      colorList.put(Color.BLUE, "Blue");
      colorList.put(new Color(0xA5, 0x2A, 0x2A), "Brown");
      colorList.put(Color.GREEN, "Green");
      colorList.put(Color.RED, "Red");
      colorList.put(Color.BLACK, "Black");

      // Additional colors!
      colorList.put(new Color(0x8A, 0x2B, 0xE2), "Blue Violet");
      colorList.put(new Color(0x00, 0x00, 0x8B), "Dark Blue");
      colorList.put(new Color(0xB2, 0x22, 0x22), "Fire Brick");
      colorList.put(new Color(0x22, 0x8B, 0x22), "Forest Green");
      colorList.put(new Color(0x40, 0xE0, 0xD0), "Turquoise");
      colorList.put(new Color(0xEE, 0x82, 0xEE), "Violet");

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
      return getColorNameFromColor(new Color(r, g, b), Integer.MAX_VALUE);
   }

   public static String getColorNameFromColor(Color color) {
      return getColorNameFromColor(color, Integer.MAX_VALUE);
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

   public static int computeMSE(Color c1, Color c2) {
      return  Math.abs(c1.getRed() - c2.getRed()) +
              Math.abs(c1.getGreen() - c2.getGreen()) +
              Math.abs(c1.getBlue() - c2.getBlue());
   }
}