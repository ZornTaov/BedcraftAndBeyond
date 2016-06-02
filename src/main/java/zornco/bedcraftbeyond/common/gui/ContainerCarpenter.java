package zornco.bedcraftbeyond.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.gui.slots.SlotTemplate;
import zornco.bedcraftbeyond.common.item.ItemTemplate;

import javax.annotation.Nullable;
import javax.vecmath.Vector2f;

public class ContainerCarpenter extends Container {

    private TileCarpenter carpenter;
    private int CRAFTING_START;

    public ContainerCarpenter(EntityPlayer player, TileCarpenter tile) {
        this.carpenter = tile;
        try {
            GuiUtils.createStandardInventory(player, new Vector2f(6, 92)).forEach(this::addSlotToContainer);
            if(tile.craftingInv.getSlots() != 0)
                GuiUtils.createSlotGrid(tile.craftingInv, 0, new Vector2f(3, 2), new Vector2f(24, 16)).forEach(this::addSlotToContainer);
            CRAFTING_START = player.inventory.mainInventory.length;

            GuiUtils.createSlotGrid(tile.outputs, 0, new Vector2f(1, 3), new Vector2f(132, 16)).forEach(this::addSlotToContainer);
            addSlotToContainer(new SlotTemplate(tile.template, 0, 97, 62));
        }

        catch (Exception e){

        }
    }

    @Nullable
    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        if(getSlot(index).getStack() == null) return null;
        ItemStack stack = getSlot(index).getStack().copy();
        if (index >= 0 && index < CRAFTING_START) {
            // Moving from player inventory somewhere into the gui
            try {
                if (stack != null && stack.getItem() instanceof ItemTemplate) {

                    if (carpenter.template.insertItem(0, stack, true) == null) {
                        // Item able to be accepted
                        getSlot(index).putStack(null);
                        carpenter.template.insertItem(0, stack, false);
                        return null;
                    } else {
                        return null;
                    }
                }

                ItemStack canMove = ItemHandlerHelper.insertItemStacked(carpenter.craftingInv, stack, true);
                if (canMove == null){
                    ItemHandlerHelper.insertItemStacked(carpenter.craftingInv, stack, false);
                    getSlot(index).putStack(null);
                }
            }

            catch(Exception e){ }

            return null;
        }

        if (index >= CRAFTING_START && index <= CRAFTING_START + carpenter.craftingInv.getSlots()) {
            // Moving from a crafting slot (or the template slot, 42) to the player's inventory.
            boolean added = playerIn.inventory.addItemStackToInventory(stack);
            if (added) getSlot(index).putStack(null);
            else return null;
        }

        if(index >= 42 && index <= 45){
            return null;
        }
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }
}
