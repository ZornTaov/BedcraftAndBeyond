package zornco.bedcraftbeyond.storage.drawer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class DrawerHandler {

    private ItemStackHandler inventory;
    private int filled;

    public DrawerHandler(int size){
        this.inventory = new ItemStackHandler(size);
        this.filled = 0;
    }

    public int getDrawerCount(){
        return filled;
    }

    // TODO: Make this modifiable?
    public int getMaxDrawers(){ return inventory.getSlots(); }

    public boolean canAccept(){
        return filled < getMaxDrawers();
    }

    public ItemStack addDrawer(ItemStack drawer, boolean simulate){
        if(!canAccept()) return drawer;

        ItemStack copy = drawer.copy();
        ItemStack originalCopy = drawer.copy();
        copy.stackSize = 1;

        ItemStack accepted = inventory.insertItem(filled, copy, simulate);
        if(accepted == null) originalCopy.stackSize--;
        if(accepted == null && !simulate) filled++;
        return originalCopy.stackSize < 1 ? null : originalCopy;
    }

}
