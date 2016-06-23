package zornco.bedcraftbeyond.beds.parts.drawer;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class ItemDrawerKey extends Item {

    public ItemDrawerKey() {
        setRegistryName(BedCraftBeyond.MOD_ID, "drawerkey");
        setUnlocalizedName(BedCraftBeyond.MOD_ID + "." + "drawerKey");
        setCreativeTab(BedCraftBeyond.BEDS_TAB);
        setMaxStackSize(1);

        GameRegistry.register(this);
    }

    public String toString() {
        return "item.tool";
    }
}
