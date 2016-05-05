package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.colors.EnumBedFabricType;
import zornco.bedcraftbeyond.client.colors.IColoredItem;

import java.util.List;

public class ItemSheets extends Item implements IColoredItem {

  public ItemSheets(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName(BedCraftBeyond.MOD_ID + ".colored_bed.sheets");
    setRegistryName(BedCraftBeyond.MOD_ID, "sheets");
    setHasSubtypes(true);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    for(EnumBedFabricType color : EnumBedFabricType.values()){
      if(!color.isDyeType()) continue;
      ItemStack variant = new ItemStack(this, 1);
      variant.setTagCompound(new NBTTagCompound());
      NBTTagCompound tags = variant.getTagCompound();
      tags.setString("color", color.name());
      subItems.add(variant);
    }
  }

  @Override
  public EnumBedFabricType getColor(ItemStack stack) {
    if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color")) return EnumBedFabricType.NONE;
    return EnumBedFabricType.valueOf(stack.getTagCompound().getString("color"));
  }
}
