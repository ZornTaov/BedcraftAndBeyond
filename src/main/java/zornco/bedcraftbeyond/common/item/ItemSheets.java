package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.colors.ColorHelper;
import zornco.bedcraftbeyond.client.colors.EnumBedFabricType;
import zornco.bedcraftbeyond.client.colors.ILinenItem;
import zornco.bedcraftbeyond.util.ColorNamer;

import java.awt.*;
import java.util.List;

public class ItemSheets extends Item implements ILinenItem {

  public ItemSheets(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName("linens.sheets");
    setRegistryName(BedCraftBeyond.MOD_ID, "sheets");
    setHasSubtypes(true);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    for (Color c : ColorNamer.colorList.keySet()) {
      ItemStack variant = new ItemStack(this, 1);
      variant.setTagCompound(new NBTTagCompound());
      NBTTagCompound tags = variant.getTagCompound();
      NBTTagCompound colorTag = new NBTTagCompound();
      colorTag.setInteger("r", c.getRed());
      colorTag.setInteger("g", c.getGreen());
      colorTag.setInteger("b", c.getBlue());

      tags.setTag("color", colorTag);
      subItems.add(variant);
    }
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    Color c = getColor(stack);
    if (c != null) {
      String closest = ColorNamer.getColorNameFromColor(c);
      tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c) + (closest != null ? " (~" + closest + ")" : ""));
    }
  }

  @Override
  public Color getColor(ItemStack stack) {
    return ColorHelper.getColorFromStack(stack);
  }
}
