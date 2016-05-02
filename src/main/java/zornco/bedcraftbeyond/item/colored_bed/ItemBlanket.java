package zornco.bedcraftbeyond.item.colored_bed;

import net.minecraft.item.Item;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class ItemBlanket extends Item {

  public ItemBlanket(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName(BedCraftBeyond.MOD_ID + ".colored_bed.blanket");
    setRegistryName(BedCraftBeyond.MOD_ID, "blanket");
  }

}
