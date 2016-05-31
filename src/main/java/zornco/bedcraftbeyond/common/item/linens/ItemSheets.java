package zornco.bedcraftbeyond.common.item.linens;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemSheets extends Item implements ILinenItem {

  public ItemSheets(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName(BedCraftBeyond.MOD_ID + ".linens.sheets");
    setRegistryName(BedCraftBeyond.MOD_ID, "sheets");
    setHasSubtypes(true);
  }

  @Override
  public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
    ItemStack stack = new ItemStack(this);
    NBTTagCompound tags = new NBTTagCompound();
    tags.setTag("color", ColorHelper.getTagForColor(Color.WHITE));
    tags.setString("type", EnumBedFabricType.SOLID_COLOR.name());
    stack.setTagCompound(tags);
    subItems.add(stack);
  }

  @Override
  public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
    NBTTagCompound tags = stack.hasTagCompound() ? stack.getTagCompound() : new NBTTagCompound();
    if(!tags.hasKey("color"))
      tags.setTag("color", ColorHelper.getTagForColor(Color.WHITE));
    if(!tags.hasKey("type"))
      tags.setString("type", EnumBedFabricType.SOLID_COLOR.name());
    stack.setTagCompound(tags);
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    Color c = getColor(stack);
    if (c != null) {
      String closest = ColorHelper.getColorNameFromColor(c);
      tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c) + (closest != null ? " (~" + closest + ")" : ""));
    }
  }

  @Override
  public Color getColor(ItemStack stack) {
    return ColorHelper.getColorFromStack(stack);
  }
}
