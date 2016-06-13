package zornco.bedcraftbeyond.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class ItemHelper {

    public static boolean areItemStacksEqual(ItemStack[] items){
        return areItemStacksEqual(new ItemStackHandler(items));
    }

    public static boolean areItemStacksEqual(IItemHandler items){
        if(items == null || items.getSlots() < 2) return true;
        ItemStack first = items.getStackInSlot(0);
        if(first == null) return false;
        for(int slot = 1; slot < items.getSlots(); ++slot){
            ItemStack stackIn = items.getStackInSlot(slot);
            if(stackIn == null) return false;
            if(!(stackIn.isItemEqual(first))) return false;
        }

        return true;
    }

}
