package zornco.bedcraftbeyond.client.tabs;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class TabBeds extends CreativeTabs {

  public TabBeds() {
    super(BedCraftBeyond.MOD_ID + ".beds");
  }

  @Override
  public Item getTabIconItem() {
    return BcbItems.rug;
  }
}
