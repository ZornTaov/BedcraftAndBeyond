package zornco.bedcraftbeyond.storage.handling;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import zornco.bedcraftbeyond.storage.StoragePart;

public interface IStorageHandler {

    /**
     * Returns whether or not the given slot name is valid.
     * This is not a means to get whether the slot is filled.
     *
     * @param name
     * @return
     */
    boolean hasSlotWithName(String name);

    /**
     * Extracts the item used to store the items.
     *
     * @param name
     * @param extract
     * @return
     */
    ItemStack getSlotItemStack(String name, boolean extract);

    /**
     * Get a storage handler with a given name.
     *
     * @param name
     * @return
     */
    IItemHandler getSlotItemHandler(String name);

    /**
     * Set a storage item for a given slot.
     *
     * @param name
     * @param stack
     * @return
     */
    boolean setNamedSlot(String name, ItemStack stack);

    /**
     * Tells whether or not a slot is filled or not.
     *
     * @param name
     * @return
     */
    boolean isSlotFilled(String name);

    ImmutableList<String> getSlotNames();

    ItemStack fillSlot(String slotName, ItemStack stack);

    StoragePart getSlotPart(String storageID);

    ImmutableList<ItemStack> getItems();
}
