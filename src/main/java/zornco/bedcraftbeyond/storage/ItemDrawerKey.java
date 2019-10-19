package zornco.bedcraftbeyond.storage;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class ItemDrawerKey extends Item {

public ItemDrawerKey(Properties properties) {
		super(properties.group(BedCraftBeyond.BEDS_TAB).maxStackSize(1));

//    public ItemDrawerKey() {
        setRegistryName(BedCraftBeyond.MOD_ID, "drawerkey");

    }

    public String toString() {
        return "item.tool";
    }
}
