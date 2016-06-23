package zornco.bedcraftbeyond.beds.parts.linens;

import net.minecraft.item.ItemStack;
import java.awt.Color;

public interface ILinenItem {

  // if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color")) return PropertyFabricType.NONE;
  // return PropertyFabricType.valueOf(stack.getTagCompound().getString("color"));
  Color getColor(ItemStack stack);

  LinenType getLinenType();

}
