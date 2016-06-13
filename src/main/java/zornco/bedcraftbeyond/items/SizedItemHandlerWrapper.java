package zornco.bedcraftbeyond.items;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import java.awt.Dimension;

public class SizedItemHandlerWrapper implements IItemHandlerModifiable, IItemHandlerSized {

    private IItemHandlerModifiable handler;
    private Dimension dim;
    public SizedItemHandlerWrapper(IItemHandlerModifiable handler, Dimension size){
        this.handler = handler;
        this.dim = size;
    }


    @Override
    public Dimension getSize() {
        return dim;
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        handler.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlots() {
        return handler.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return handler.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return handler.insertItem(slot, stack, simulate);
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return handler.extractItem(slot, amount, simulate);
    }
}
