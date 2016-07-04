package zornco.bedcraftbeyond.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.parts.Part;

import java.awt.*;
import java.util.List;

public abstract class StoragePart extends Part {

    public abstract int getInventorySize();
    public abstract List<Slot> layoutSlots(EntityPlayer player, ItemStack stack);
    public abstract ResourceLocation getGuiBackground();

    public abstract Dimension getGuiSize();
}
