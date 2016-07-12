package zornco.bedcraftbeyond.storage.drawers.standard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;

public class DrawerPart extends StoragePart {

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
        return 9;
    }

    @Override
    public Dimension getGuiSize() {
        return new Dimension(176, 132);
    }

    @Override
    public Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        return new GuiDrawer(player, tile, handler, id);
    }

    @Override
    public Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        return new ContainerDrawer(player, tile, handler, id);
    }
}
