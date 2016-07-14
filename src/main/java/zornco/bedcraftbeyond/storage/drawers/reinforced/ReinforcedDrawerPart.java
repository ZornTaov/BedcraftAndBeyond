package zornco.bedcraftbeyond.storage.drawers.reinforced;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;

public class ReinforcedDrawerPart extends StoragePart {

    @Override
    public Type getPartType() {
        return Type.STORAGE;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.reinforcedDrawer;
    }

    @Override
    public int getInventorySize() {
        return 18;
    }

    @Override
    public Dimension getGuiSize() {
        return new Dimension(176, 153);
    }

    @Override
    public Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        return new GuiReinforcedDrawer(player, tile, handler, id);
    }

    @Override
    public Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) {
        return new ContainerReinforcedDrawer(player, tile, handler, id);
    }
}
