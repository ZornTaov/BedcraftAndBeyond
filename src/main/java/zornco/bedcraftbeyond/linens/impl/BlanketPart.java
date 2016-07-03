package zornco.bedcraftbeyond.linens.impl;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.parts.IColorablePart;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.util.ColorHelper;

import java.awt.*;

public class BlanketPart extends Part implements IColorablePart.IColorableItem {

    public BlanketPart(){
        ModContent.Items.blanket = new ItemBlanket();
    }

    @Override
    public Type getPartType() {
        return Type.BLANKETS;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.blanket;
    }

    @Override
    public Color getPartColor(ItemStack stack) {
        return ColorHelper.getColorFromStack(stack);
    }
}
