package zornco.bedcraftbeyond.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.awt.*;

public interface IStorageItem {

    int getInventorySize();
    Dimension getGuiSize();

    Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) throws Exception;

    Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id) throws Exception;
}
