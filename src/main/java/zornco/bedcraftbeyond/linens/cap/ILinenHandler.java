package zornco.bedcraftbeyond.linens.cap;

import com.google.common.collect.ImmutableList;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import zornco.bedcraftbeyond.linens.LinenType;

public interface ILinenHandler {

    void clearAvailableSlots();

    boolean clearSlot(LinenType slot);

    ImmutableList<LinenType> getAvailableSlots();

    boolean hasParticularSlot(LinenType slot);

    boolean setSlotItem(LinenType slot, ItemStack newItem);

    ItemStack getSlotItem(LinenType slot, boolean extract);

    boolean isSlotFilled(LinenType slot);
}
