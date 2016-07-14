package zornco.bedcraftbeyond.storage.drawers.reinforced;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.gui.ContainerStorage;

import java.awt.*;

public class ContainerReinforcedDrawer extends ContainerStorage {

    private IStorageHandler storageHandler;
    private String storageID;
    public ContainerReinforcedDrawer(EntityPlayer player, TileEntity tile, IStorageHandler storage, String storageID) {
        super(player, tile, storage, storageID);

        this.storageHandler = storage;
        this.storageID = storageID;

        IItemHandler handler = storage.getSlotItemHandler(storageID);
        if(handler == null) return;
        GuiUtils.createSlotGrid(handler, 0, new Dimension(9,2), new Point(7,19)).forEach(this::addSlotToContainer);
        GuiUtils.createStandardInventory(player, new Point(7,70)).forEach(this::addSlotToContainer);
    }
}
