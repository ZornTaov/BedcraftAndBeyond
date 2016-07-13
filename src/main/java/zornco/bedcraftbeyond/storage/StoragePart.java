package zornco.bedcraftbeyond.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.awt.*;

public abstract class StoragePart extends Part {

    public abstract int getInventorySize();
    public abstract Dimension getGuiSize();

    public abstract Object createGUI(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id);

    public abstract Container createContainer(EntityPlayer player, TileEntity tile, IStorageHandler handler, String id);
}
