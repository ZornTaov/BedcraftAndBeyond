package zornco.bedcraftbeyond.storage.drawer;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.core.ModContent;

public class DrawerPart extends StoragePart {

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

    @Override
    public int getInventorySize() {
        return 12;
    }
}
