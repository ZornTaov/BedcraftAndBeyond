package zornco.bedcraftbeyond.storage.drawer;

import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;
import zornco.bedcraftbeyond.core.config.ConfigSettings;
import zornco.bedcraftbeyond.parts.IPart;
import zornco.bedcraftbeyond.parts.Part;

public class ItemDrawer extends Item implements ICapabilityProvider, IPart {

    private ItemStackHandler items = new ItemStackHandler(ConfigSettings.DRAWER_ITEM_LIMIT);

    public ItemDrawer() {
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setMaxStackSize(4);
        setUnlocalizedName(BedCraftBeyond.MOD_ID + ".beds.drawer");
        setRegistryName(BedCraftBeyond.MOD_ID, "drawer");

        GameRegistry.register(this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) return true;
        return false;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        if(capability.equals(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)) return (T) items;
        return null;
    }

    @Override
    public Part getPartReference() {
        return ModContent.BedParts.drawer;
    }
}
