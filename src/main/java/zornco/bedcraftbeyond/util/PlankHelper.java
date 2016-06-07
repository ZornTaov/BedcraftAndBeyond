package zornco.bedcraftbeyond.util;

import java.util.*;
import java.util.Map.Entry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class PlankHelper {


   public static ArrayList<ItemStack> planks;
   //public static LinkedHashMap<ItemStack, Integer> plankColorMap = new LinkedHashMap<ItemStack, Integer>();
   public static LinkedHashMap<String, Integer> plankColorMap = new LinkedHashMap<String, Integer>();
   public static final int oakColor = 0xaf8f58;
   public static final String oakNameSpace = "minecraft:planks@0";
   public static final ItemStack oakItemStack = new ItemStack(Blocks.PLANKS, 1, 0);

   public static void compilePlanks() {
      planks = new ArrayList<>(OreDictionary.getOres("plankWood"));
      //= (ArrayList<ItemStack>) OreDictionary.getOres("plankWood");
   }

   static int prevcolor = 0;

   public static boolean isPlankKnown(ItemStack plank) {
      return PlankHelper.isPlankKnown(PlankHelper.plankStringfromItemStack(plank));
   }

   public static boolean isPlankKnown(String plank) {

      Iterator iterator = getPlankColorMap().entrySet().iterator();
      Entry entry;

      do {
         if (!iterator.hasNext()) {
            return false;
         }

         entry = (Entry) iterator.next();
      }
      while (!plank.equalsIgnoreCase((String) entry.getKey()));

      return true;
   }

   public static LinkedHashMap<String, Integer> getPlankColorMap() {
      //done this way for debugging and seeing what's in this map
      LinkedHashMap<String, Integer> plankColorMap = PlankHelper.plankColorMap;
      return plankColorMap;
   }

   public static ItemStack validatePlank(NBTTagCompound bedTags, ItemStack plankToAdd) {
      return validatePlank(bedTags, -1, plankToAdd);
   }

   public static ItemStack validatePlank(NBTTagCompound bedTags, int damage, ItemStack plankToAdd) {
      if (!bedTags.hasKey("plank") && !bedTags.hasKey("plankType")) {
         //this should only be called for 1.0.5 or lower

         if (plankToAdd != null) return PlankHelper.addPlankInfo(bedTags, plankToAdd);

         int frameNum = 0;

         //check if item is from 1.0.5 or lower
         if (damage >= 241) frameNum = damage >> 8;

         //check if block is from 1.0.5 or lower
         if (bedTags.hasKey("colorCombo")) frameNum = bedTags.getShort("colorCombo") >> 8;

         //change to adding plankType
         ItemStack plankIS = new ItemStack(Blocks.PLANKS, 1, frameNum);
         String plankString = PlankHelper.plankStringfromItemStack(plankIS);
         bedTags.setString("plankType", plankString);

         return plankIS;
      } else if (!bedTags.hasKey("plankType")) {
         NBTTagList list = bedTags.getTagList("plank", 10);
         NBTTagCompound plank = list.getCompoundTagAt(0);
         //change Plank to plankType
         ItemStack plankIS = ItemStack.loadItemStackFromNBT(plank);
         if (isPlankKnown(plankIS)) {
            String plankString = PlankHelper.plankStringfromItemStack(plankIS);
            bedTags.setString("plankType", plankString);
            bedTags.removeTag("plank");//			return new ItemStack(Blocks.planks, 1, 0);
            return plankIS;
         } else {
            bedTags.setString("plankType", oakNameSpace);
            return oakItemStack;
         }
      } else {
         if (isPlankKnown(bedTags.getString("plankType"))) {
            String[] plank = bedTags.getString("plankType").split("@");
            return new ItemStack(Item.REGISTRY.getObject(new ResourceLocation(plank[0])), 1, Integer.parseInt(plank[1]));

         } else {
            bedTags.setString("plankType", oakNameSpace);
            return oakItemStack;
         }
      }
   }

   public static String plankStringfromItemStack(ItemStack plank) {
      //BedCraftBeyond.logger.info(plank.toString());
      return Item.REGISTRY.getNameForObject(plank.getItem()) + "@" + plank.getItemDamage();
   }

   public static ItemStack addPlankInfo(NBTTagCompound bedTags, ItemStack plank) {
      NBTTagList nbttaglist = new NBTTagList();
      NBTTagCompound plankTag = new NBTTagCompound();
      plank.writeToNBT(plankTag);
      nbttaglist.appendTag(plankTag);
      bedTags.setTag("plank", nbttaglist);
      return plank;
   }
}
