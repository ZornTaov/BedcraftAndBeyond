package zornco.bedcraftbeyond.client.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class TabBedCraftBeyond extends CreativeTabs {

	public TabBedCraftBeyond(String label) 
	{
		super(label);
	}
	@Override
	public Item getTabIconItem() {
		return BcbItems.drawerKey;
    }


}
