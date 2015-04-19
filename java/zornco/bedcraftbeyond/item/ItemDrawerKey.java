package zornco.bedcraftbeyond.item;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemDrawerKey extends Item {

	public ItemDrawerKey() {
		super();
		this.maxStackSize = 1;
	}
	public String toString()
	{
		return "item.tool";
	}
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister) {
		itemIcon = par1IconRegister.registerIcon("bedcraftbeyond:drawerkey");
	}
}
