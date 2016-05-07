package zornco.bedcraftbeyond.common.item.linens;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.client.colors.EnumBedFabricType;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemBlanket extends Item implements ILinenItem {

   public ItemBlanket() {
      setCreativeTab(BedCraftBeyond.bedsTab);
      setMaxStackSize(16);
      setUnlocalizedName("linens.blanket");
      setRegistryName(BedCraftBeyond.MOD_ID, "blanket");
      setHasSubtypes(true);
   }

   @Override
   public Color getColor(ItemStack stack) {
      return ColorHelper.getColorFromStack(stack);
   }

   @Override
   public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
      // TODO: Oh, the woes of Mojang and their privates.
      // TextFormatting.valueOf(getColor(stack).toDye().getUnlocalizedName().toUpperCase())
      // getColor(stack).toDye().getTextColor()
      Color c = getColor(stack);
      if (c != null) {
         String closest = ColorHelper.getColorNameFromColor(c);
         tooltip.add("Color: " + ColorHelper.getFormattedColorValues(c) + (closest != null ? " (~" + closest + ")" : ""));
      }
   }

   @Override
   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
      for (Color c : ColorHelper.colorList.keySet()) {
         ItemStack variant = new ItemStack(this, 1);
         variant.setTagCompound(new NBTTagCompound());
         NBTTagCompound tags = variant.getTagCompound();
         tags.setString("type", EnumBedFabricType.SOLID_COLOR.name());
         NBTTagCompound colorTag = new NBTTagCompound();
         colorTag.setInteger("r", c.getRed());
         colorTag.setInteger("g", c.getGreen());
         colorTag.setInteger("b", c.getBlue());

         tags.setTag("color", colorTag);
         subItems.add(variant);
      }
   }
}
