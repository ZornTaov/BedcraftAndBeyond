package zornco.bedcraftbeyond.beds.frames.registry.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.core.gui.container.SlotGhost;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class ContainerFrameWhitelistEditor extends Container {

    public ContainerFrameWhitelistEditor(EntityPlayer player){
        SlotGhost ghostSlot = new SlotGhost(new Point(210, 6));
        this.addSlotToContainer(ghostSlot);

        List<Slot> playerInv = GuiUtils.createStandardInventory(player, GuiFrameWhitelistEditor.INV_AREA.getLocation());
        for(Slot s : playerInv) this.addSlotToContainer(s);
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if(slotId < 0) return null;

        Slot s = getSlot(slotId);
        if(!(s instanceof SlotGhost)) return super.slotClick(slotId, dragType, clickTypeIn, player);

        SlotGhost ghost = (SlotGhost) getSlot(slotId);
        return ghost.handleClick(dragType, clickTypeIn, player);
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
