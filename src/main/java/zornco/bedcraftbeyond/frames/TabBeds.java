package zornco.bedcraftbeyond.frames;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.ModContent;

public class TabBeds extends CreativeTabs {

  public TabBeds() {
    super(BedCraftBeyond.MOD_ID + ".beds");
  }

  @Override
  public Item getTabIconItem() {
    return ModContent.Items.rug;
  }
}
