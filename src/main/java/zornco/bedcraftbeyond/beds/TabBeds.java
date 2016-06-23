package zornco.bedcraftbeyond.beds;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.BcbItems;

public class TabBeds extends CreativeTabs {

  public TabBeds() {
    super(BedCraftBeyond.MOD_ID + ".beds");
  }

  @Override
  public Item getTabIconItem() {
    return BcbItems.rug;
  }
}
