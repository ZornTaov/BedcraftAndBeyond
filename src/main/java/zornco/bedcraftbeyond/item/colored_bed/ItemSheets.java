package zornco.bedcraftbeyond.item.colored_bed;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemSheets extends Item {

  public ItemSheets(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName(BedCraftBeyond.MOD_ID + ".colored_bed.sheets");
    setRegistryName(BedCraftBeyond.MOD_ID, "sheets");
  }

}
