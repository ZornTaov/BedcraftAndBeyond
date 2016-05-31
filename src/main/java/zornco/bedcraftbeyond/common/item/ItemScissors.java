package zornco.bedcraftbeyond.common.item;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemScissors extends Item {

	public ItemScissors() {
		setUnlocalizedName(BedCraftBeyond.MOD_ID + ".scissors");
		setRegistryName(BedCraftBeyond.MOD_ID, "scissors");
		setCreativeTab(BedCraftBeyond.bedCraftBeyondTab);
		setMaxStackSize(1);
	}
}
