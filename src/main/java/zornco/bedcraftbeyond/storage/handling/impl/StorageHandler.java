package zornco.bedcraftbeyond.storage.handling.impl;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import zornco.bedcraftbeyond.storage.IStorageItem;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.util.HashMap;
import java.util.Map;

public class StorageHandler implements IStorageHandler, INBTSerializable<NBTTagList> {

    protected ImmutableList<String> availableSlots;
    private HashMap<String, ItemStack> storage;

    public StorageHandler() {
        this.availableSlots = ImmutableList.of("default");
        this.storage = new HashMap<>();
    }

    public IItemHandler getSlotItemHandler(String name) {
        if (!hasSlotWithName(name) || !storage.containsKey(name)) return null;

        return storage.get(name).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    @Override
    public IStorageItem getSlotItem(String name) throws Exception {
        ItemStack stack = getSlotItemstack(name, false);
        if(stack == null) throw new Exception("Attempted to access an invalid slot in a storage handler without checking if the slot is filled.");

        return (IStorageItem) stack.getItem();
    }

    public ItemStack getSlotItemstack(String name, boolean extract) {
        if(!hasSlotWithName(name)) return null;

        ItemStack stack = storage.get(name).copy();
        if(extract) storage.remove(name);
        return stack;
    }

    public boolean hasSlotWithName(String name) {
        return availableSlots.contains(name);
    }

    public boolean setSlotItem(String name, ItemStack stack) {
        if(!hasSlotWithName(name)) return false;

        if(!(stack.getItem() instanceof IStorageItem)) return false;

        if(!stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) return false;

        if(storage.containsKey(name))
            storage.replace(name, stack);
        else
            storage.put(name, stack);

        return true;
    }

    public boolean isSlotFilled(String name) {
        return storage.containsKey(name);
    }

    @Override
    public ImmutableList<String> getSlotNames() {
        return availableSlots;
    }

    @Override
    public ImmutableList<ItemStack> getItems() {
        return ImmutableList.copyOf(storage.values());
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList compound = new NBTTagList();
        for(Map.Entry<String, ItemStack> part : storage.entrySet()) {
            IItemHandler itemHandler = part.getValue().getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            NBTTagCompound storageTag = new NBTTagCompound();
            storageTag.setString("name", part.getKey());
            storageTag.setInteger("size", itemHandler.getSlots());
            storageTag.setTag("item", part.getValue().serializeNBT());
            compound.appendTag(storageTag);
        }

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagList nbt) {
        if(nbt.hasNoTags()) return;
        for(int storageIndex = 0; storageIndex < nbt.tagCount(); ++storageIndex) {
            NBTTagCompound itemsAt = nbt.getCompoundTagAt(storageIndex);
            ItemStack item = ItemStack.loadItemStackFromNBT(itemsAt.getCompoundTag("item"));
            setSlotItem(itemsAt.getString("name"), item);
        }
    }
}
