package zornco.bedcraftbeyond.frames.registry.gui.editor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.core.gui.container.SlotGhost;

import javax.annotation.Nullable;
import java.awt.*;

public class ContainerFrameWhitelistEditor extends Container {

    public ContainerFrameWhitelistEditor(FrameRegistry.EnumFrameType type, ResourceLocation id, EntityPlayer player){

        Rectangle guiInventoryArea = new Rectangle(0, 100 + 12, (GuiFrameWhitelistEditor.SIZE.width / 2) - 4, 100);
        Rectangle guiArea = GuiUtils.getInventoryAreaCentered(guiInventoryArea.getSize(), guiInventoryArea.y);

        SlotGhost ghostSlot = new SlotGhost(new Point(10, 10));
        this.addSlotToContainer(ghostSlot);

        GuiUtils.createStandardInventory(player, guiArea.getLocation()).forEach(this::addSlotToContainer);
    }

    @Nullable
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        if(slotId > -1 && getSlot(slotId) instanceof SlotGhost)
            ((SlotGhost) getSlot(slotId)).handleClick(dragType, clickTypeIn, player);

        return super.slotClick(slotId, dragType, clickTypeIn, player);
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
