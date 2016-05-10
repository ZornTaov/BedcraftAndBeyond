package zornco.bedcraftbeyond.common.item.linens;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemBlanket extends Item implements ILinenItem {

   public ItemBlanket() {
      setCreativeTab(BedCraftBeyond.bedsTab);
      setMaxStackSize(16);
      setUnlocalizedName("linens.blanket");
      setRegistryName(BedCraftBeyond.MOD_ID, "blanket");
   }

   @Override
   public void onCreated(ItemStack stack, World worldIn, EntityPlayer playerIn) {
      NBTTagCompound color = ColorHelper.getTagForColor(Color.WHITE);
      stack.setTagCompound(new NBTTagCompound());
      stack.getTagCompound().setTag("color", color);
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
}
