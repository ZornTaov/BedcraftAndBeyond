package zornco.bedcraftbeyond.storage;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class ItemDrawerKey extends Item {

    private static Properties DEFAULT_PROPS = new Item.Properties().group(BedCraftBeyond.BEDS_TAB).maxStackSize(1);

    public ItemDrawerKey() {
        super(DEFAULT_PROPS);
        // setRegistryName(BedCraftBeyond.MOD_ID, "drawer_key");
    }

    public String toString() {
        return "bcb_drawer_key";
    }
}
