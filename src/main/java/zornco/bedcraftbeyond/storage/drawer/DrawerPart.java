package zornco.bedcraftbeyond.storage.drawer;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.gui.GuiUtils;
import zornco.bedcraftbeyond.storage.StoragePart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DrawerPart extends StoragePart {

    public DrawerPart(){
        ModContent.Items.drawer = new ItemDrawer();
    }

    @Override
    public Type getPartType() {
        return Type.STORAGE;
    }

    @Override
    public Item getPartItem() {
        return ModContent.Items.drawer;
    }

    @Override
    public int getInventorySize() {
        return 9;
    }

    @Override
    public List<Slot> layoutSlots(EntityPlayer player, ItemStack stack) {
        ArrayList<Slot> slots = new ArrayList<>();
        IItemHandler handler = ((ICapabilityProvider) stack.getItem()).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);
        slots.addAll(GuiUtils.createSlotGrid(handler, 0, new Dimension(9,1), new Point(7,18)));
        slots.addAll(GuiUtils.createStandardInventory(player, new Point(7,50)));
        return slots;
    }

    @Override
    public ResourceLocation getGuiBackground() {
        return new ResourceLocation(BedCraftBeyond.MOD_ID, "textures/gui/bed_drawer.png");
    }

    @Override
    public Dimension getGuiSize() {
        return new Dimension(176, 132);
    }
}
