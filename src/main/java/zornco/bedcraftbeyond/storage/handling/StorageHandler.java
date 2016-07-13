package zornco.bedcraftbeyond.storage.handling;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import zornco.bedcraftbeyond.parts.Part;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.util.HashMap;
import java.util.Map;

public class StorageHandler implements IStorageHandler, INBTSerializable<NBTTagList> {

    protected ImmutableList<String> registeredSlots;
    private HashMap<String, ItemStack> storage;

    public StorageHandler() {
        this.registeredSlots = ImmutableList.of("default");
        this.storage = new HashMap<>();
    }

    public IItemHandler getSlotItemHandler(String name) {
        if (!hasSlotWithName(name) || !storage.containsKey(name)) return null;

        return storage.get(name).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
    }

    public ItemStack getSlotItemStack(String name, boolean extract) {
        if(!hasSlotWithName(name)) return null;

        ItemStack stack = storage.get(name).copy();
        if(extract) storage.remove(name);
        return stack;
    }

    public boolean hasSlotWithName(String name) {
        return registeredSlots.contains(name);
    }

    public boolean setNamedSlot(String name, ItemStack stack) {
        if(!hasSlotWithName(name)) return false;

        if(Part.getPartType(stack) != Part.Type.STORAGE) return false;
        if(!(Part.getPartReference(stack) instanceof StoragePart)) return false;

        if(!stack.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) return false;

        storage.remove(name);
        storage.put(name, stack);
        return true;
    }

    public boolean isSlotFilled(String name) {
        return storage.containsKey(name);
    }

    @Override
    public ImmutableList<String> getSlotNames() {
        return registeredSlots;
    }

    @Override
    public ItemStack fillSlot(String slotName, ItemStack stack) {
        if(!registeredSlots.contains(slotName)) return stack.copy();
        if(isSlotFilled(slotName)) return stack.copy();

        ItemStack copy = ItemHandlerHelper.copyStackWithSize(stack, 1);
        if(!setNamedSlot(slotName, copy)) return stack.copy();
        return ItemHandlerHelper.copyStackWithSize(stack, stack.stackSize - 1);
    }

    @Override
    public StoragePart getSlotPart(String storageID) {
        if(!hasSlotWithName(storageID)) return null;
        if(!isSlotFilled(storageID)) return null;

        return (StoragePart) Part.getPartReference(storage.get(storageID));
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
            setNamedSlot(itemsAt.getString("name"), item);
        }
    }
}
