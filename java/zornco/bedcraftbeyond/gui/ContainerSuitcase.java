package zornco.bedcraftbeyond.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.item.ItemSuitcase;

public class ContainerSuitcase extends Container
{
	final InventorySuitcase inventory;

	private static final int INV_START = InventorySuitcase.INV_SIZE, INV_END = INV_START+26,
			HOTBAR_START = INV_END+1, HOTBAR_END = HOTBAR_START+8;
	
	public ContainerSuitcase(EntityPlayer par1Player, InventoryPlayer inventoryPlayer, InventorySuitcase inventoryItem) {
		this.inventory = inventoryItem;
        int i = (this.INV_START/9 - 4) * 18;
		for (int j = 0; j < this.INV_START/9; ++j)
        {
            for (int k = 0; k < 9; ++k)
            {
                this.addSlotToContainer(new Slot(inventory, k + j * 9, 8 + k * 18, 18 + j * 18));
            }
        }

        for (int l = 0; l < 3; ++l)
        {
            for (int j1 = 0; j1 < 9; ++j1)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j1 + l * 9 + 9, 8 + j1 * 18, 103 + l * 18 + i));
            }
        }

        for (int i1 = 0; i1 < 9; ++i1)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i1, 8 + i1 * 18, 161 + i));
        }
	}

	@Override
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		// be sure to return the inventory's isUseableByPlayer method
		// if you defined special behavior there:
		return inventory.isUseableByPlayer(entityplayer);
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int index)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);

		if (slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			// If item is in our custom Inventory or armor slot
			if (index < INV_START)
			{
				// try to place in player inventory / action bar
				if (!this.mergeItemStack(itemstack1, INV_START, HOTBAR_END+1, true))
				{
					return null;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			// Item is in inventory / hotbar, try to place in custom inventory or armor slots
			else
			{
				/*
				If your inventory only stores certain instances of Items,
				you can implement shift-clicking to your inventory like this:
				
				// Check that the item is the right type
				if (itemstack1.getItem() instanceof ItemCustom)
				{
					// Try to merge into your custom inventory slots
					// We use 'InventoryItem.INV_SIZE' instead of INV_START just in case
					// you also add armor or other custom slots
					if (!this.mergeItemStack(itemstack1, 0, InventoryItem.INV_SIZE, false))
					{
						return null;
					}
				}
				// If you added armor slots, check them here as well:
				// Item being shift-clicked is armor - try to put in armor slot
				if (itemstack1.getItem() instanceof ItemArmor)
				{
					int type = ((ItemArmor) itemstack1.getItem()).armorType;
					if (!this.mergeItemStack(itemstack1, ARMOR_START + type, ARMOR_START + type + 1, false))
					{
						return null;
					}
				}
				Otherwise, you have basically 2 choices:
				1. shift-clicking between player inventory and custom inventory
				2. shift-clicking between action bar and inventory
				 
				Be sure to choose only ONE of the following implementations!!!
				*/
				/**
				 * Implementation number 1: Shift-click into your custom inventory
				 */
				if (index >= INV_START)
				{
					// place in custom inventory
					if (!this.mergeItemStack(itemstack1, 0, INV_START, false))
					{
						return null;
					}
				}
				
				/**
				 * Implementation number 2: Shift-click items between action bar and inventory
				 */
				// item is in player's inventory, but not in action bar
				if (index >= INV_START && index < HOTBAR_START)
				{
					// place in action bar
					if (!this.mergeItemStack(itemstack1, HOTBAR_START, HOTBAR_END+1, false))
					{
						return null;
					}
				}
				// item in action bar - place in player inventory
				else if (index >= HOTBAR_START && index < HOTBAR_END+1)
				{
					if (!this.mergeItemStack(itemstack1, INV_START, INV_END+1, false))
					{
						return null;
					}
				}
			}

			if (itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if (itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
		}

		return itemstack;
	}

	/**
	 * You should override this method to prevent the player from moving the stack that
	 * opened the inventory, otherwise if the player moves it, the inventory will not
	 * be able to save properly
	 */
	@Override
	public ItemStack slotClick(int slot, int button, int flag, EntityPlayer player) {
		// this will prevent the player from interacting with the item that opened the inventory:
		if (slot >= 0 && getSlot(slot) != null && getSlot(slot).getStack() == player.getHeldItem()) {
			return null;
		}
		return super.slotClick(slot, button, flag, player);
	}
	@Override
	protected boolean mergeItemStack(ItemStack stack, int start, int end, boolean backwards)
	{
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
	public class SlotItemInv extends Slot
	{
		public SlotItemInv(IInventory inv, int index, int xPos, int yPos)
		{
			super(inv, index, xPos, yPos);
		}

		// This is the only method we need to override so that
		// we can't place our inventory-storing Item within
		// its own inventory (thus making it permanently inaccessible)
		// as well as preventing abuse of storing backpacks within backpacks
		/**
		 * Check if the stack is a valid item for this slot.
		 */
		@Override
		public boolean isItemValid(ItemStack itemstack)
		{
			// Everything returns true except an instance of our Item
			return !(itemstack.getItem() instanceof ItemSuitcase);
		}
	}
}
