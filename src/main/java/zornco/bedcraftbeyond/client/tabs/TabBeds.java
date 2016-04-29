package zornco.bedcraftbeyond.client.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class TabBeds extends CreativeTabs {

  public TabBeds() {
    super("bedcraft.beds");
  }

  @Override
  public Item getTabIconItem() {
    return BedCraftBeyond.rugItem;
  }
}
