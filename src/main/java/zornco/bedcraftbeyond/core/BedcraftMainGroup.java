package zornco.bedcraftbeyond.core;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class BedcraftMainGroup extends ItemGroup {

	public BedcraftMainGroup() {
		super(BedCraftBeyond.MOD_ID + "_main");
	}

	@Override
	public ItemStack createIcon() {
		return new ItemStack(BedcraftItems.DRAWER_KEY.get());
	}

}
