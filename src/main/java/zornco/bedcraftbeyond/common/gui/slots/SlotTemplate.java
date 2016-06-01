package zornco.bedcraftbeyond.common.gui.slots;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import zornco.bedcraftbeyond.common.item.ItemTemplate;

public class SlotTemplate extends SlotItemHandler {

    public SlotTemplate(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    /**
     * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
     */
    @Override
    public boolean isItemValid(ItemStack stack)
    {
        if (stack == null)
            return false;

        if(stack.getItem() instanceof ItemTemplate) return true;
        return false;
    }
}
