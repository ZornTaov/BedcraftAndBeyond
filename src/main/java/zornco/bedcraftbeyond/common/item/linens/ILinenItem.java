package zornco.bedcraftbeyond.common.item.linens;

import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import java.awt.Color;

public interface ILinenItem {

  // if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color")) return EnumBedFabricType.NONE;
  // return EnumBedFabricType.valueOf(stack.getTagCompound().getString("color"));
  Color getColor(ItemStack stack);

}
