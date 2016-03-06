package zornco.bedcraftbeyond.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class TabBedCraftBeyond extends CreativeTabs {

	public TabBedCraftBeyond(String label) 
	{
		super(label);
	}
	@Override
	public Item getTabIconItem() {
		return BedCraftBeyond.drawerKey;
    }


}
