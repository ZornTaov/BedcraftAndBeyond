package zornco.bedcraftbeyond.common.item;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemScissors extends Item {

	private final String name = "scissors";
	public ItemScissors() {
		setUnlocalizedName(BedCraftBeyond.MOD_ID + "_" + name);
		setRegistryName(BedCraftBeyond.MOD_ID, name);
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setMaxStackSize(1);
	}
}
