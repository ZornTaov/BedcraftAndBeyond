package zornco.bedcraftbeyond.suitcase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerSuitcase extends Container {

    final InventorySuitcase inventory;

    private static final int INV_SIZE = InventorySuitcase.INV_SIZE;

    public ContainerSuitcase(EntityPlayer par1Player, InventoryPlayer inventoryPlayer, InventorySuitcase inventoryItem) {
        int lockedSlot = inventoryPlayer.currentItem;
        this.inventory = inventoryItem;
        int i = (this.INV_SIZE / 9 - 4) * 18;
        for (int j = 0; j < this.INV_SIZE / 9; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlotToContainer(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l) {
            for (int j1 = 0; j1 < 9; ++j1) {
                this.addSlotToContainer(new Slot(inventoryPlayer, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1) {
            if (i1 == lockedSlot)
                this.addSlotToContainer(new SlotSuitcase(inventoryPlayer, i1, 8 + i1 * 18, 161 + i));
            else
                this.addSlotToContainer(new Slot(inventoryPlayer, i1, 8 + i1 * 18, 161 + i));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        // be sure to return the inventory's isUseableByPlayer method
        // if you defined special behavior there:
        return inventory.isUseableByPlayer(entityplayer);
    }

    /**
     * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index) {
        ItemStack itemstack = null;
        Slot slot = (Slot) this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < this.INV_SIZE) {
                if (!this.mergeItemStack(itemstack1, this.INV_SIZE, this.inventorySlots.size(), true)) {
                    return null;
                }
            } else if (!this.mergeItemStack(itemstack1, 0, this.INV_SIZE, false)) {
                return null;
            }

            if (itemstack1.stackSize == 0) {
                slot.putStack((ItemStack) null);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }

    /**
     * You should override this method to prevent the player from moving the stack that
     * opened the inventory, otherwise if the player moves it, the inventory will not
     * be able to save properly
     */
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        // this will prevent the player from interacting with the item that opened the inventory:
        if (slotId > -1 && getSlot(slotId) instanceof SlotSuitcase) {
            return null;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards) {
        return super.mergeItemStack(stack, start, end, backwards);
        /*boolean flag1 = false;
		int k = (backwards ? end - 1 : start);
		Slot slot;
		ItemStack itemstack1;

		if (stack.isStackable())
		{
			while (stack.stackSize > 0 && (!backwards && k < end || backwards && k >= start))
			{
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!slot.isItemValid(stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 != null && itemstack1.getItem() == stack.getItem() &&
						(!stack.getHasSubtypes() || stack.getItemDamage() == itemstack1.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, itemstack1))
				{
					int l = itemstack1.stackSize + stack.stackSize;

					if (l <= stack.getMaxStackSize() && l <= slot.getSlotStackLimit()) {
						stack.stackSize = 0;
						itemstack1.stackSize = l;
						inventory.markDirty();
						flag1 = true;
					} else if (itemstack1.stackSize < stack.getMaxStackSize() && l < slot.getSlotStackLimit()) {
						stack.stackSize -= stack.getMaxStackSize() - itemstack1.stackSize;
						itemstack1.stackSize = stack.getMaxStackSize();
						inventory.markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}
		if (stack.stackSize > 0)
		{
			k = (backwards ? end - 1 : start);
			while (!backwards && k < end || backwards && k >= start) {
				slot = (Slot) inventorySlots.get(k);
				itemstack1 = slot.getStack();

				if (!slot.isItemValid(stack)) {
					k += (backwards ? -1 : 1);
					continue;
				}

				if (itemstack1 == null) {
					int l = stack.stackSize;
					if (l <= slot.getSlotStackLimit()) {
						slot.putStack(stack.copy());
						stack.stackSize = 0;
						inventory.markDirty();
						flag1 = true;
						break;
					} else {
						putStackInSlot(k, new ItemStack(stack.getItem(), slot.getSlotStackLimit(), stack.getItemDamage()));
						stack.stackSize -= slot.getSlotStackLimit();
						inventory.markDirty();
						flag1 = true;
					}
				}

				k += (backwards ? -1 : 1);
			}
		}

		return flag1;*/
    }

    public class SlotSuitcase extends Slot {

        public SlotSuitcase(IInventory inv, int index, int xPos, int yPos) {
            super(inv, index, xPos, yPos);
        }

        // This is the only method we need to override so that
        // we can't place our inventory-storing Item within
        // its own inventory (thus making it permanently inaccessible)
        // as well as preventing abuse of storing suitcases within suitcases

        /**
         * Check if the stack is a valid item for this slot.
         */
        @Override
        public boolean isItemValid(ItemStack itemstack) {
            // Everything returns true except an instance of our Item
            return !(itemstack.getItem() instanceof ItemSuitcase);
        }
    }
}
