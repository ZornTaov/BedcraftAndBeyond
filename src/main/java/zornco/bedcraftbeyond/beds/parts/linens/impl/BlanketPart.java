package zornco.bedcraftbeyond.beds.parts.linens.impl;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.beds.IColorablePart;
import zornco.bedcraftbeyond.beds.parts.BedPart;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.util.ColorHelper;

import javax.annotation.Nullable;
import java.awt.*;

public class BlanketPart extends BedPart implements IColorablePart.IColorableItem {

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
