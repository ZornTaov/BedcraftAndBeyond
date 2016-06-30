package zornco.bedcraftbeyond.beds.frames.registry.gui.editor;

import com.google.common.collect.Range;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.beds.frames.registry.FrameException;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelist;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.core.gui.container.SlotGhost;
import zornco.bedcraftbeyond.core.gui.container.SlotItemViewer;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.Set;

public class ContainerFrameWhitelistEditor extends Container {

    public ContainerFrameWhitelistEditor(FrameRegistry.EnumFrameType type, EntityPlayer player){
        SlotGhost ghostSlot = new SlotGhost(new Point(210, 6));
        this.addSlotToContainer(ghostSlot);



        FrameWhitelist list = FrameRegistry.getFrameWhitelist(type);




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
