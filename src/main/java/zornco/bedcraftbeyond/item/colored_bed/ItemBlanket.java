package zornco.bedcraftbeyond.item.colored_bed;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.List;

public class ItemBlanket extends Item {

  public ItemBlanket(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName(BedCraftBeyond.MOD_ID + ".colored_bed.blanket");
    setRegistryName(BedCraftBeyond.MOD_ID, "blanket");
    setHasSubtypes(true);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    super.getSubItems(itemIn, tab, subItems);
  }
}
