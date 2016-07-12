package zornco.bedcraftbeyond.storage.handling;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileInventoryWatcher extends ItemStackHandler {

    private TileEntity master;
    public TileInventoryWatcher(TileEntity master, IItemHandler handler) {
        this.master = master;
        this.setSize(handler.getSlots());
        this.deserializeNBT((NBTTagCompound) CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.writeNBT(handler, null));
    }

    public TileInventoryWatcher(TileEntity master, int size) {
        super(size);
        this.master = master;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);
        master.markDirty();
    }
}
