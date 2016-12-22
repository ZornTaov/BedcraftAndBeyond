package zornco.bedcraftbeyond.linens.cap;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;
import zornco.bedcraftbeyond.linens.ILinenItem;
import zornco.bedcraftbeyond.linens.LinenType;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LinenHandler implements ILinenHandler , INBTSerializable<NBTTagList> {

    protected ImmutableList<LinenType> availableSlots;
    protected Map<LinenType, ItemStack> storage;

    public LinenHandler(){
        this.availableSlots = ImmutableList.of();
        this.storage = new HashMap<>();
    }

    @Override
    public void clearAvailableSlots() {
        this.storage.clear();
    }

    @Override
    public boolean clearSlot(LinenType slot) {
        this.setSlotItem(slot, null);
        return this.storage.get(slot) == null;
    }

    @Override
    public boolean setSlotItem(LinenType slot, ItemStack stack) {
        if(stack == null || !(stack.getItem() instanceof ILinenItem)) return false;

        if(this.isSlotFilled(slot)) {
            ItemStack newStack = this.storage.replace(slot, stack);
            return ItemStack.areItemsEqual(newStack, stack);
        } else {
            this.storage.put(slot, stack);
            return true;
        }
    }

    @Override
    public ImmutableList<LinenType> getAvailableSlots() {
        return this.availableSlots;
    }

    @Override
    public boolean hasParticularSlot(LinenType slot) {
        return availableSlots.contains(slot);
    }

    @Override
    public ItemStack getSlotItem(LinenType slot, boolean extract) {
        if(!hasParticularSlot(slot)) return null;
        if(!this.storage.containsKey(slot)) return null;

        ItemStack copyStack = this.storage.get(slot).copy();
        if(extract) this.storage.remove(slot);
        return copyStack;
    }

    @Override
    public boolean isSlotFilled(LinenType slot) {
        return this.storage.containsKey(slot) && this.storage.get(slot) != null;
    }

    @Override
    public NBTTagList serializeNBT() {
        NBTTagList tags = new NBTTagList();
        Iterator i = storage.entrySet().iterator();
        while(i.hasNext()) {
            Map.Entry<LinenType, ItemStack> ent = (Map.Entry<LinenType, ItemStack>) i.next();
            NBTTagCompound linen = new NBTTagCompound();
            linen.setString("slot", ent.getKey().name());
            linen.setTag("item", ent.getValue().serializeNBT());

            tags.appendTag(linen);
        }

        return tags;
    }

    @Override
    public void deserializeNBT(NBTTagList nbt) {
        if(nbt.hasNoTags()) return;
        for(int linenItemIndex = 0; linenItemIndex < nbt.tagCount(); ++linenItemIndex) {
            NBTTagCompound itemsAt = nbt.getCompoundTagAt(linenItemIndex);
            ItemStack item = ItemStack.loadItemStackFromNBT(itemsAt.getCompoundTag("item"));
            LinenType part = LinenType.valueOf(itemsAt.getString("slot"));

            this.setSlotItem(part, item);
        }
    }
}
