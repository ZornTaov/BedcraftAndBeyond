package zornco.bedcraftbeyond.common.crafting;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemStackHandler;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.gui.ContainerBlank;
import zornco.bedcraftbeyond.items.IItemHandlerSized;
import zornco.bedcraftbeyond.items.ItemHandlerGridHelper;

import javax.annotation.Nullable;
import java.awt.*;

public class ItemHandlerCrafting extends InventoryCrafting implements IItemHandlerSized {

    private ItemStackHandler handler;
    private Dimension size;
    public ItemHandlerCrafting(Dimension size){
        super(new ContainerBlank(), size.width, size.height);
        this.size = size;
        this.handler = new ItemStackHandler(size.width * size.height);
    }

    //region ItemHandler
    @Override
    public Dimension getSize() {
        return size;
    }

    @Override
    public int getSizeInventory() {
        return handler.getSlots();
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return handler.getStackInSlot(index);
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
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        ItemStack inserted = handler.insertItem(slot, stack, simulate);
        try {
            if (inserted == null || inserted.stackSize < stack.stackSize)
                if(!simulate)
                    onContentsChanged(slot);
        }

        catch(Exception e){

        }

        return inserted;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack extracted = handler.extractItem(slot, amount, simulate);
        if(extracted != null && !simulate) onContentsChanged(slot);
        return extracted;
    }

    protected void onContentsChanged(int slot) {
        BedCraftBeyond.LOGGER.debug(String.format("Contents of slot %d changed.", slot));
    }

    public NBTTagCompound serializeNBT(){
        return handler.serializeNBT();
    }

    public void deserializeNBT(NBTTagCompound nbt){
        handler.deserializeNBT(nbt);
    }
    //endregion

    /**
     * Gets the ItemStack in the slot specified.
     */
    @Nullable
    public ItemStack getStackInRowAndColumn(int row, int column) {
        ItemStack stack = ItemHandlerGridHelper.getStackFromPoint(new Point(column, row), this);
        return stack;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Nullable
    public ItemStack removeStackFromSlot(int index) {
        return handler.extractItem(index, 64, false);
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Nullable
    public ItemStack decrStackSize(int index, int count) {
        return handler.extractItem(index, count, false);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        handler.setStackInSlot(index, stack);
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        if (stack == null)
            return false;
        ItemStack remainder = handler.insertItem(index, stack, true);
        return remainder == null || remainder.stackSize < stack.stackSize;
    }

    public void clear() {
        for (int i = 0; i < this.getSlots(); ++i) {
            this.setStackInSlot(i, null);
        }
    }

    public int getHeight()
    {
        return this.getSize().height;
    }

    public int getWidth()
    {
        return this.getSize().width;
    }
}
