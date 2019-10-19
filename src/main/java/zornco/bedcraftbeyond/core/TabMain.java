package zornco.bedcraftbeyond.core;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class TabMain extends ItemGroup {

	public TabMain()
	{
		super(BedCraftBeyond.MOD_ID);
	}
	@Override
	public ItemStack createIcon() {
		// TODO Auto-generated method stub
		return new ItemStack(ModContent.Items.drawerKey);
	}


}
