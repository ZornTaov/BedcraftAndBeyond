package zornco.bedcraftbeyond.beds.parts.drawer;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.beds.parts.BedPart;
import zornco.bedcraftbeyond.core.ModContent;

public class DrawerPart extends BedPart {

    public DrawerPart(){
        ModContent.Items.drawer = new ItemDrawer();
    }

    @Override
    public Type getPartType() {
        return Type.STORAGE;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.drawer;
    }

}
