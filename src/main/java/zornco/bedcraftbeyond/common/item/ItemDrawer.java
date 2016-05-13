package zornco.bedcraftbeyond.common.item;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemDrawer extends Item {

  public ItemDrawer(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(4);
    setUnlocalizedName("beds.drawer");
    setRegistryName(BedCraftBeyond.MOD_ID, "drawer");
  }

}
