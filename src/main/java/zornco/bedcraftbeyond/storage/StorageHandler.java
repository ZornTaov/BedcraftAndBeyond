package zornco.bedcraftbeyond.storage;

import com.google.common.collect.ImmutableList;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zornco.bedcraftbeyond.parts.IPart;
import zornco.bedcraftbeyond.parts.Part;

import java.util.HashMap;

public class StorageHandler implements IStorageHandler {

    protected ImmutableList<String> registeredSlots;
    private HashMap<String, ItemStack> storage;

    private int size;

    public StorageHandler(int size) {
        this.registeredSlots = ImmutableList.of("default");
        this.storage = new HashMap<>();
        this.size = size;
    }

    public IItemHandler getSlotItemHandler(String name) {
        if (!hasSlotWithName(name) || !storage.containsKey(name)) return null;

        ItemStack stack = storage.get(name);
        StoragePart part = (StoragePart) ((IPart) stack.getItem()).getPartReference();
        ItemStackHandler handler = new ItemStackHandler(part.getInventorySize());
        if(!stack.hasTagCompound()) {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setTag("storage", handler.serializeNBT());
            stack.setTagCompound(compound);
            return handler;
        }

        if(!stack.getTagCompound().hasKey("storage")) {
            stack.getTagCompound().setTag("storage", handler.serializeNBT());
            return handler;
        }

        handler.deserializeNBT(stack.getTagCompound().getCompoundTag("storage"));
        return handler;
    }

    public ItemStack getSlotItemStack(String name, boolean extract) {
        if(!hasSlotWithName(name)) return null;
        ItemStack stack = storage.get(name);
        if(extract) storage.remove(name);
        return stack;
    }

    public boolean hasSlotWithName(String name) {
        return registeredSlots.contains(name);
    }

    public boolean setNamedSlot(String name, ItemStack stack) {
        if(!hasSlotWithName(name)) return false;
        if(!(stack.getItem() instanceof ICapabilityProvider)) return false;

        if(Part.getPartType(stack) != Part.Type.STORAGE) return false;
        if(!(Part.getPartReference(stack) instanceof StoragePart)) return false;

        ICapabilityProvider provider = (ICapabilityProvider) stack.getItem();
        if(!provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP)) return false;

        storage.remove(name);
        storage.put(name, stack);
        return true;
    }

    public boolean isSlotFilled(String name) {
        IItemHandler handler = getSlotItemHandler(name);
        if (handler == null) return false;
        return true;
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

        ItemStack stack = getSlotItemStack(storageID, false);
        return (StoragePart) ((IPart) stack.getItem()).getPartReference();
    }
}
