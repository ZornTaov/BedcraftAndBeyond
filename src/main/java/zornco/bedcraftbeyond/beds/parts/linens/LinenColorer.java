package zornco.bedcraftbeyond.beds.parts.linens;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.core.util.ColorHelper;

import java.awt.*;

public class LinenColorer implements IItemColor {
    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        Color c = ColorHelper.getColorFromStack(stack);
        return c.getRGB();
    }
}
