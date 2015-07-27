package zornco.bedcraftbeyond.client;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.BedCraftBeyond;
import cpw.mods.fml.common.registry.GameRegistry;

public class TabBedCraftBeyond extends CreativeTabs {

	public TabBedCraftBeyond(String label) 
	{
		super(label);
	}
	@Override
	public Item getTabIconItem() {
		return null;
	}
	@Override
    public ItemStack getIconItemStack()
    {
		return new ItemStack(GameRegistry.findBlock(BedCraftBeyond.MOD_ID, "rugBlock"), 1, 0);
    }


}
