package zornco.bedcraftbeyond.storage.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

import javax.annotation.Nullable;

public class ContainerStorage extends Container {

    protected StoragePart part;
    protected TileEntity tile;
    private boolean dirty;
    public ContainerStorage(EntityPlayer player, TileEntity tile, IStorageHandler storage, String storageID){
        part = storage.getSlotPart(storageID);
        this.tile = tile;
        dirty = false;
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return null;
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        dirty = true;
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        if(dirty) tile.markDirty();
    }
}
