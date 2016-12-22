package zornco.bedcraftbeyond.dyes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import java.awt.*;

public interface IColoredItem {
    // Marker interface, use ColorHelper to manage colors

    /**
     * Gets a color from a compound tag (color => r, g, b) or a integer tag (color => INT)
     * If it fails to parse the tag, it returns Color.WHITE.
     *
     * @param stack The itemstack to try and get a color from.
     * @return
     */
    default Color getColorFromStack(ItemStack stack){
        return getColorFromStack(stack, "color");
    }

    /**
     * Gets a color from a named tag in an itemstack's nbt.
     * If it fails to parse the tag, it returns Color.WHITE.
     *
     * @param stack The itemstack to try and get a color from.
     * @param tagName The tag name to get a color from.
     * @return
     */
    default Color getColorFromStack(ItemStack stack, String tagName) {
        if (!stack.hasTagCompound()) return Color.WHITE;
        NBTTagCompound tags = stack.getTagCompound();
        if (!tags.hasKey(tagName)) return Color.WHITE;
        if (!tags.hasKey(tagName, Constants.NBT.TAG_COMPOUND)) {
            try {
                return new Color(tags.getInteger(tagName));
            } catch (Exception e) {
                return Color.WHITE;
            }
        } else {
            NBTTagCompound color = tags.getCompoundTag(tagName);
            return getColorFromNBT(color);
        }
    }

    /**
     * Parses a color from an NBT compound tag.
     * This does not dig for the tag.
     *
     * @param compound The root tag that holds color data.
     * @return
     */
    default Color getColorFromNBT(NBTTagCompound compound){
        if(compound == null) return Color.WHITE;
        if(!compound.hasKey("r") || !compound.hasKey("g") || !compound.hasKey("b")) return Color.WHITE;
        return new Color(compound.getInteger("r"), compound.getInteger("g"), compound.getInteger("b"));
    }
}
