package zornco.bedcraftbeyond.linens.impl;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.parts.IColorablePart;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.util.ColorHelper;

import java.awt.*;

public class SheetPart extends Part implements IColorablePart.IColorableItem {

    public SheetPart(){
        ModContent.Items.sheets = new ItemSheets();
    }

    @Override
    public Type getPartType() {
        return Type.SHEETS;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.sheets;
    }

    @Override
    public Color getPartColor(ItemStack stack) {
        return ColorHelper.getColorFromStack(stack);
    }
}
