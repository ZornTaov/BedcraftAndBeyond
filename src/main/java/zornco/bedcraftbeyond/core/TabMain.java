package zornco.bedcraftbeyond.core;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class TabMain extends CreativeTabs {

	public TabMain()
	{
		super(BedCraftBeyond.MOD_ID);
	}
	@Override
	public Item getTabIconItem() {
		return ModContent.Items.drawerKey;
    }


}
