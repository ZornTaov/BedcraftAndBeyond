package zornco.bedcraftbeyond.storage.tinyDrawer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;

public class TinyDrawerPart extends StoragePart {

    @Override
    public Type getPartType() {
        return Type.STORAGE;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.tinyDrawer;
    }

    @Override
    public int getInventorySize() {
        return 1;
    }

    @Override
    public Dimension getGuiSize() {
        return new Dimension(176, 132);
    }

    @Override
    public Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        return new GuiTinyDrawer(player, tile, handler, id);
    }

    @Override
    public Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        return new ContainerTinyDrawer(player, tile, handler, id);
    }
}
