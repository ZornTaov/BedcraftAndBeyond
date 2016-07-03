package zornco.bedcraftbeyond.dyes;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.core.util.ColorHelper;

public class DyeColorer implements IItemColor {

    @Override
    public int getColorFromItemstack(ItemStack stack, int tintIndex) {
        if (tintIndex == 1) return ColorHelper.getColorFromStack(stack).getRGB();
        return -1;
    }

}
