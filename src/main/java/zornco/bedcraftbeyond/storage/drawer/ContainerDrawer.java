package zornco.bedcraftbeyond.storage.drawer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.gui.ContainerStorage;

import java.awt.*;

public class ContainerDrawer extends ContainerStorage {

    private IStorageHandler storageHandler;
    private String storageID;
    public ContainerDrawer(EntityPlayer player, TileEntity tile, IStorageHandler storage, String storageID) {
        super(player, tile, storage, storageID);

        this.storageHandler = storage;
        this.storageID = storageID;

        IItemHandler handler = storage.getSlotItemHandler(storageID);
        if(handler == null) return;
        GuiUtils.createSlotGrid(handler, 0, new Dimension(9,1), new Point(7,19)).forEach(this::addSlotToContainer);
        GuiUtils.createStandardInventory(player, new Point(7,50)).forEach(this::addSlotToContainer);
    }
}
