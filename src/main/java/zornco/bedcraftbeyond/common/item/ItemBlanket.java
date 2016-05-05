package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.colors.IColoredItem;
import zornco.bedcraftbeyond.client.colors.EnumBedFabricType;

import java.util.List;

public class ItemBlanket extends Item implements IColoredItem {

  public ItemBlanket(){
    setCreativeTab(BedCraftBeyond.bedsTab);
    setMaxStackSize(16);
    setUnlocalizedName(BedCraftBeyond.MOD_ID + ".colored_bed.blanket");
    setRegistryName(BedCraftBeyond.MOD_ID, "blanket");
    setHasSubtypes(true);
  }

  @Override
  public EnumBedFabricType getColor(ItemStack stack) {
    if(!stack.hasTagCompound() || !stack.getTagCompound().hasKey("color")) return EnumBedFabricType.NONE;
    return EnumBedFabricType.valueOf(stack.getTagCompound().getString("color"));
  }

  @Override
  public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
    // TODO: Oh, the woes of Mojang and their privates.
    // TextFormatting.valueOf(getColor(stack).toDye().getUnlocalizedName().toUpperCase())
    // getColor(stack).toDye().getTextColor()
    tooltip.add("Color: " + getColor(stack));
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
}
