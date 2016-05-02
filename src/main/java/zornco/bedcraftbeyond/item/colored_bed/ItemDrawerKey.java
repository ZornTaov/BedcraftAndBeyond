package zornco.bedcraftbeyond.item.colored_bed;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemDrawerKey extends Item {

	public ItemDrawerKey() {
		setRegistryName(BedCraftBeyond.MOD_ID, "drawerkey");
		setUnlocalizedName(BedCraftBeyond.MOD_ID + "_" + "drawerkey");
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setMaxStackSize(1);
	}

	public String toString()
	{
		return "item.tool";
	}
}
