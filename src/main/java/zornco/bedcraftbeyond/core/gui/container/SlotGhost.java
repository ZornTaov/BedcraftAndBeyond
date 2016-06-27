package zornco.bedcraftbeyond.core.gui.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.wrapper.EmptyHandler;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

import java.awt.*;

public class SlotGhost extends SlotItemHandler {

    private ItemStack ghostItem;
    public SlotGhost(Point position){
        super(new EmptyHandler(), 0, position.x, position.y);
    }

    public void setGhostItem(ItemStack stack){
        if(stack == null){
            this.ghostItem = null;
            return;
        }

        ItemStack copy = stack.copy();
        copy.stackSize = 1;
        this.ghostItem = copy;
    }

    @Override
    public void putStack(ItemStack stack) { }

    @Override
    public ItemStack getStack() {
        return ghostItem;
    }

    @Override
    public ItemStack decrStackSize(int amount) {
        if(amount > 0) ghostItem = null;
        return null;
    }

    public ItemStack handleClick(int mouseButton, ClickType clickType, EntityPlayer player){
        BedCraftBeyond.LOGGER.info("Mouse: " + mouseButton);
        BedCraftBeyond.LOGGER.info("ClickType: " + clickType.name());
        BedCraftBeyond.LOGGER.info("World: " + player.getEntityWorld());
        switch(mouseButton){
            // Left mouse
            case 0:
                ItemStack held = player.inventory.getItemStack();
                setGhostItem(held);
                break;

            // Right mouse
            case 1:
                setGhostItem(null);
                break;
        }

        return null;
    }
}
