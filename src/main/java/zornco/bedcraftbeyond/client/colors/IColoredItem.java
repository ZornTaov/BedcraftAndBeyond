package zornco.bedcraftbeyond.client.colors;

import net.minecraft.item.ItemStack;

public interface IColoredItem {

  // if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color")) return EnumBedFabricType.NONE;
  // return EnumBedFabricType.valueOf(stack.getTagCompound().getString("color"));
  EnumBedFabricType getColor(ItemStack stack);

}
