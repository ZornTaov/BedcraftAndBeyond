package zornco.bedcraftbeyond.beds.parts.linens;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import zornco.bedcraftbeyond.beds.parts.linens.impl.ItemBlanket;
import zornco.bedcraftbeyond.beds.parts.linens.impl.ItemSheets;

import java.awt.*;

public class LinenHandler extends ItemStackHandler {

    public LinenHandler(){
        super(2);
    }

    public ItemStack getSheets(){ return getStackInSlot(0); }
    public ItemStack getBlankets(){ return getStackInSlot(1); }

    public void setSheets(ItemStack stack){ setStackInSlot(0, stack); }
    public void setBlankets(ItemStack stack){ setStackInSlot(1, stack); }

    public boolean hasBothParts(){
        return hasLinenPart(LinenType.SHEETS) && hasLinenPart(LinenType.BLANKETS);
    }

    public PropertyFabricType getLinenType(LinenType type){
        ItemStack part = getLinenPart(type, false);
        if (part == null) return PropertyFabricType.NONE;

        if (!part.hasTagCompound()) {
            part.setTagCompound(new NBTTagCompound());
        }
        if (!part.getTagCompound().hasKey("type")) {
            part.getTagCompound().setString("type", PropertyFabricType.NONE.name());
            return PropertyFabricType.NONE;
        }

        try {
            return PropertyFabricType.valueOf(part.getTagCompound().getString("type"));
        } catch (Exception e) {
            return PropertyFabricType.NONE;
        }
    }

    public Color getLinenColor(LinenType type){
        ItemStack i = getLinenPart(type, false);
        if (i == null) Color.WHITE.getRGB();
        if (getLinenType(type) != PropertyFabricType.SOLID_COLOR) return Color.WHITE;

        return ((ILinenItem) i.getItem()).getColor(i);
    }

    @SuppressWarnings("unused")
    public boolean hasLinenPart(LinenType part) {
        switch (part) {
            case SHEETS:
                return getStackInSlot(0) != null;

            case BLANKETS:
                return getStackInSlot(1) != null;

            default:
                return false;
        }
    }

    public ItemStack getLinenPart(LinenType part, boolean extract) {
        switch (part) {
            case SHEETS:
                return extractItem(0, 1, !extract);

            case BLANKETS:
                return extractItem(1, 1, !extract);

            default:
                return null;
        }
    }

    public boolean setLinenPart(LinenType part, ItemStack linen) {
        if (linen == null || linen.getItem() == null) return false;
        ItemStack linenCopy = ItemHandlerHelper.copyStackWithSize(linen, 1);
        int slot = 0;
        switch (part) {
            case SHEETS:
                if(!(linenCopy.getItem() instanceof ItemSheets)) return false;
                slot = 0; break;

            case BLANKETS:
                if(!(linenCopy.getItem() instanceof ItemBlanket)) return false;
                slot = 1; break;
        }

        if (getStackInSlot(slot) == null) {
            ItemStack inserted = insertItem(slot, linenCopy, false);
            if (inserted == null) return true;
        }

        return false;
    }

}
