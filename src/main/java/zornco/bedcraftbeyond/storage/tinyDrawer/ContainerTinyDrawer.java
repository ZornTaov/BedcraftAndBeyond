package zornco.bedcraftbeyond.storage.tinyDrawer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.gui.ContainerStorage;

import java.awt.*;

public class ContainerTinyDrawer extends ContainerStorage {

    private IStorageHandler storageHandler;
    private String storageID;
    public ContainerTinyDrawer(EntityPlayer player, TileEntity tile, IStorageHandler storage, String storageID) {
        super(player, tile, storage, storageID);

        this.storageHandler = storage;
        this.storageID = storageID;

        IItemHandler handler = storage.getSlotItemHandler(storageID);
        if(handler == null) return;
        this.addSlotToContainer(new SlotItemHandler(handler, 0, 8 + (4 * 18), 20));
        GuiUtils.createStandardInventory(player, new Point(7,50)).forEach(this::addSlotToContainer);
    }
}
