package zornco.bedcraftbeyond.common.item;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemDrawerKey extends Item {

	public ItemDrawerKey() {
		setRegistryName(BedCraftBeyond.MOD_ID, "drawerkey");
		setUnlocalizedName(BedCraftBeyond.MOD_ID + "." + "drawerKey");
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setMaxStackSize(1);
	}

	public String toString()
	{
		return "item.tool";
	}
}
