package zornco.bedcraftbeyond.common.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;
import java.util.List;

public class ItemDyeBottle extends Item {

   public ItemDyeBottle(){
      setCreativeTab(BedCraftBeyond.bedsTab);
      setMaxStackSize(16);
      setHasSubtypes(true);
      setRegistryName(BedCraftBeyond.MOD_ID, "dye_bottle");
      setUnlocalizedName("dye_bottle");
   }

   @Override
   public void getSubItems(Item itemIn, CreativeTabs tab, List<ItemStack> subItems) {
      for(Color c : ColorHelper.colorList.keySet()){
         ItemStack dyeBottleStack = new ItemStack(this, 1);
         NBTTagCompound tags = new NBTTagCompound();
         tags.setTag("color", ColorHelper.getTagForColor(c));
         dyeBottleStack.setTagCompound(tags);
         subItems.add(dyeBottleStack);
      }
   }
}
