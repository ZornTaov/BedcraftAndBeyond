package zornco.bedcraftbeyond.beds.parts.drawer;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.beds.parts.BedPart;
import zornco.bedcraftbeyond.core.ModContent;

import javax.annotation.Nullable;

public class DrawerPart extends BedPart {

    public DrawerPart(){
        ModContent.Items.drawer = new ItemDrawer();
    }

    @Override
    public Type getPartType() {
        return Type.STORAGE;
    }

    @Override
    public boolean hasBlock() {
        return false;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.drawer;
    }

    @Nullable
    @Override
    public Block getPartBlock() {
        return null;
    }
}
