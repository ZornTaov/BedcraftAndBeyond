package zornco.bedcraftbeyond.core.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class SlotItemViewer extends SlotItemHandler {

    private ItemStack curStack;
    public SlotItemViewer(int posX, int posY){
        super(new EmptyHandler(), 0, posX, posY);
    }

    public void setStack(ItemStack curStack) {
        this.curStack = curStack;
    }

    public ItemStack getStack() {
        return curStack;
    }

    @Override
    public boolean canTakeStack(EntityPlayer playerIn) {
        return false;
    }
}
