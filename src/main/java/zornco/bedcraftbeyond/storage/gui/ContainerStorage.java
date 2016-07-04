package zornco.bedcraftbeyond.storage.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.storage.IStorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;

public class ContainerStorage extends Container {

    public ContainerStorage(EntityPlayer player, IStorageHandler storage, String storageID){
        StoragePart part = storage.getSlotPart(storageID);
        part.layoutSlots(player, storage.getSlotItemStack(storageID, false)).forEach(this::addSlotToContainer);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
