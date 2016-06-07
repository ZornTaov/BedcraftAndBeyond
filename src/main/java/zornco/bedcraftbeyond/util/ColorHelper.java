package zornco.bedcraftbeyond.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.Constants;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.awt.Color;
import java.util.HashMap;

/**
 * Java Code to get a color name from rgb/hex value/awt color
 * <p>
 * The part of looking up a color name from the rgb values is edited from
 * https://gist.github.com/nightlark/6482130#file-gistfile1-java (that has some errors) by Ryan Mast (nightlark)
 *
 * @author Xiaoxiao Li
 */
public class ColorHelper {

    public static HashMap<Color, String> colorList;

    /**
     * Initialize the color list that we have.
     * All added colors are HEX values.
     */
    public static void initColorList() {
        if (colorList != null && !colorList.isEmpty()) return;
        colorList = new HashMap<>();

        // Additional colors!
        colorList.put(new Color(0xDA, 0xA5, 0x20), "Goldenrod");
        colorList.put(new Color(0x40, 0xE0, 0xD0), "Turquoise");
        colorList.put(new Color(0xEE, 0x82, 0xEE), "Violet");
        colorList.put(new Color(0xFF, 0x00, 0x63), "Crimson");

        colorList.put(new Color(0x00, 0xA6, 0x58), "Zorn");
        colorList.put(new Color(0x3A, 0x7F, 0xE1), "Xylex");
        colorList.put(new Color(0x32, 0x17, 0x4D), "Delenas");

        BedCraftBeyond.logger.info(BedCraftBeyond.MOD_NAME + ": ColorHelper registered " + colorList.size() + " dye colors.");
    }

    public static String getColorNameFromColor(Color color) {
        return getColorNameFromColor(color, 9001);
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
        return Math.abs(c1.getRed() - c2.getRed()) +
            Math.abs(c1.getGreen() - c2.getGreen()) +
            Math.abs(c1.getBlue() - c2.getBlue());
    }

    /**
     * Gets a color from a compound tag (color => r, g, b) or a integer tag (color => INT)
     * If it fails to parse the tag, it returns Color.WHITE.
     *
     * @param stack The itemstack to try and get a color from.
     * @return
     */
    public static Color getColorFromStack(ItemStack stack) {
        if (!stack.hasTagCompound()) return Color.WHITE;
        NBTTagCompound tags = stack.getTagCompound();
        if (!tags.hasKey("color")) return Color.WHITE;
        if (!tags.hasKey("color", Constants.NBT.TAG_COMPOUND)) {
            try {
                return new Color(tags.getInteger("color"));
            } catch (Exception e) {
                return Color.WHITE;
            }
        } else {
            NBTTagCompound color = tags.getCompoundTag("color");
            try {
                return new Color(color.getInteger("r"), color.getInteger("g"), color.getInteger("b"));
            } catch (Exception e) {
                return Color.WHITE;
            }
        }
    }

    public static String getFormattedColorValues(Color c) {
        return TextFormatting.RED + "" + c.getRed() + TextFormatting.WHITE + ", " +
            TextFormatting.GREEN + c.getGreen() + TextFormatting.WHITE + ", " +
            TextFormatting.BLUE + c.getBlue() + TextFormatting.RESET;
    }

    public static NBTTagCompound getTagForColor(Color c) {
        NBTTagCompound color = new NBTTagCompound();
        color.setInteger("r", c.getRed());
        color.setInteger("g", c.getGreen());
        color.setInteger("b", c.getBlue());
        return color;
    }

    public static Color blendColours(Object o0, Object o1) {
        assert (o0 instanceof Color || o0 instanceof Integer);
        assert (o1 instanceof Color || o1 instanceof Integer);
        Color c0 = o0 instanceof Color ? (Color) o0 : new Color((Integer) o0);
        Color c1 = o1 instanceof Color ? (Color) o1 : new Color((Integer) o1);

        double totalAlpha = c0.getAlpha() + c1.getAlpha();
        double weight0 = c0.getAlpha() / totalAlpha;
        double weight1 = c1.getAlpha() / totalAlpha;

        double r = weight0 * c0.getRed() + weight1 * c1.getRed();
        double g = weight0 * c0.getGreen() + weight1 * c1.getGreen();
        double b = weight0 * c0.getBlue() + weight1 * c1.getBlue();
        double a = Math.max(c0.getAlpha(), c1.getAlpha());
        return new Color((int) r, (int) g, (int) b, (int) a);
    }
}