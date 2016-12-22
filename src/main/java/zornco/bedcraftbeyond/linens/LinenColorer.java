package zornco.bedcraftbeyond.linens;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.core.util.ColorHelper;
import zornco.bedcraftbeyond.dyes.IColoredItem;

import java.awt.*;

public class LinenColorer implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        Color c = ((IColoredItem) stack.getItem()).getColorFromStack(stack);
        return c.getRGB();
    }
}
