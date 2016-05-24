package zornco.bedcraftbeyond.client.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class TabBeds extends CreativeTabs {

  public TabBeds() {
    super("bedcraft.beds");
  }

  @Override
  public Item getTabIconItem() {
    return BcbItems.rug;
  }
}