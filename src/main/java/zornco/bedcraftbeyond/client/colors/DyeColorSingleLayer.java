package zornco.bedcraftbeyond.client.colors;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;

public class DyeColorSingleLayer implements IItemColor {

  @Override
  public int getColorFromItemstack(ItemStack stack, int tintIndex) {
    return ItemDye.dyeColors[EnumDyeColor.byDyeDamage(stack.getItemDamage()).getMetadata()];
  }
}
