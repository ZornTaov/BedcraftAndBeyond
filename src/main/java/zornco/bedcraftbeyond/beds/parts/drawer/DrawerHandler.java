package zornco.bedcraftbeyond.beds.parts.drawer;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;

public class DrawerHandler {

    private ItemStackHandler inventory;
    private int filled;

    public DrawerHandler(){
        this.inventory = new ItemStackHandler(2);
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
        ItemStack accepted = inventory.insertItem(filled, drawer, simulate);
        if(accepted == null && !simulate) filled++;
        return accepted;
    }

}
