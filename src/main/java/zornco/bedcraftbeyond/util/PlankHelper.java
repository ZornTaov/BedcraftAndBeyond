package zornco.bedcraftbeyond.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
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
   public static final ItemStack oakItemStack = new ItemStack(Blocks.planks, 1, 0);

   public static void compilePlanks() {
      planks = new ArrayList<>(OreDictionary.getOres("plankWood"));
      //= (ArrayList<ItemStack>) OreDictionary.getOres("plankWood");
   }

   public static boolean readyToColor = false;
   static int prevcolor = 0;

   public static void compilePlanksClient() {

      for (ItemStack stack : planks) {
         //not bothering with items that are not blocks
         if (stack.getItem() instanceof ItemBlock) {
            if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
               //iterate over sub items
               List<ItemStack> list = new ArrayList<ItemStack>();
               ItemStack stackNoMeta = new ItemStack(stack.getItem(), 1, 0);
               String stack1Name = stackNoMeta.getDisplayName();

               if (stack1Name == null) {
                  BedCraftBeyond.logger.info("STACK1 NULL");
                  break;
               } else {
                  BedCraftBeyond.logger.info("Planks, meta 0:" + stack1Name);
                  list.add(stackNoMeta);

                  for (int i = 1; i < 16; i++) {
                     ItemStack stackWithMeta = new ItemStack(stack.getItem(), 1, i);
                     String stack2Name = stackWithMeta.getDisplayName();
                     if (stack2Name == null) {
                        BedCraftBeyond.logger.info("STACK2 NULL");
                        break;
                     } else {
                        BedCraftBeyond.logger.info("Planks, meta " + i + ":" + stack2Name);
                        if (stack2Name.equals(stack1Name)) break;
                        list.add(stackWithMeta);
                     }
                  }

                  for (ItemStack itemStack : list) addPlankToList(itemStack);
               }
            } else
               //just grab texture for the given meta
               addPlankToList(stack);
         }
      }
   }

	/*public static int getPlankColor(ItemStack plank) {
    if (getPlankColorMap().containsKey(plank)) {

			return getPlankColorMap().get(plank);
		}
		else return PlankHelper.oakColor;
	}*/

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
      while (!PlankHelper.compareNames(plank, (String) entry.getKey()));

      return true;
   }

   /*public static int getPlankColor(ItemStack plank)
   {
     Iterator iterator = getPlankColorMap().entrySet().iterator();
     Entry entry;

     do
     {
       if (!iterator.hasNext())
       {
         return PlankHelper.oakColor;
       }

       entry = (Entry)iterator.next();
     }
     while (!PlankHelper.compareStacks(plank, (ItemStack)entry.getKey()));

     return (Integer)entry.getValue();
   }*/
   public static int getPlankColor(String plank) {
      Iterator iterator = getPlankColorMap().entrySet().iterator();
      Entry entry;

      do {
         if (!iterator.hasNext()) {
            return PlankHelper.oakColor;
         }

         entry = (Entry) iterator.next();
      }
      while (!PlankHelper.compareNames(plank, (String) entry.getKey()));

      return (Integer) entry.getValue();
   }

   private static boolean compareStacks(ItemStack stack1, ItemStack stack2) {
      return stack1 != null && stack2 != null && stack2.getItem() == stack1.getItem() && (stack2.getItemDamage() == 32767 || stack2.getItemDamage() == stack1.getItemDamage());
   }

   private static boolean compareNames(String stack1, String stack2) {
      return stack1.equals(stack2);
   }

   /*public static LinkedHashMap<ItemStack, Integer> getPlankColorMap() {
     //done this way for debugging and seeing what's in this map
     LinkedHashMap<ItemStack, Integer> plankColorMap = PlankHelper.plankColorMap;
     return plankColorMap;
   }*/
   public static LinkedHashMap<String, Integer> getPlankColorMap() {
      //done this way for debugging and seeing what's in this map
      LinkedHashMap<String, Integer> plankColorMap = PlankHelper.plankColorMap;
      return plankColorMap;
   }

   public static void addPlankToList(ItemStack itemStack) {
      PlankHelper.addPlankToList(itemStack, -3);
   }

   public static void addPlankToList(ItemStack itemStack, int color) {
      ItemStack stack2 = new ItemStack(itemStack.getItem(), 1, itemStack.getItemDamage());

      String result = "Block: " + stack2.getDisplayName() + " meta: " + stack2.getItemDamage();
      try {
         //int color = ClientUtils.getAverageItemColour(stack2);
         //int color = ClientUtils.getAverageItemColour2(stack2);
         if (color != -2) {
            //getPlankColorMap().put(stack2, color);
            getPlankColorMap().put(PlankHelper.plankStringfromItemStack(stack2), color);

         }
         result += " Color: " + color;
         if (color == -2) {
            result += " THIS TEXTURE IS MISSING OR BROKEN! WON'T BE ADDED!";
         }
         if (color == prevcolor && color > -1) {
            result += " SAME AS LAST COLOR! WTF?!";
         }
         prevcolor = color;
      } catch (Exception e) {
         result += " THIS TEXTURE IS REALLY MISSING OR BROKEN! WON'T BE ADDED!";
      }
      if (color != -3) BedCraftBeyond.logger.info(result);
   }

   public static ItemStack validatePlank(ItemStack bed) {
      if (bed.getTagCompound() == null) bed.setTagCompound(new NBTTagCompound());
      return validatePlank(bed.getTagCompound(), bed.getItemDamage(), null);
   }

   public static ItemStack validatePlank(NBTTagCompound bedTags, ItemStack plankToAdd) {
      return validatePlank(bedTags, -1, plankToAdd);
   }

   public static ItemStack validatePlank(NBTTagCompound bedTags, int damage, ItemStack plankToAdd) {
      if (!bedTags.hasKey("plank") && !bedTags.hasKey("plankNameSpace")) {
         //this should only be called for 1.0.5 or lower

         if (plankToAdd != null) return PlankHelper.addPlankInfo(bedTags, plankToAdd);

         int frameNum = 0;

         //check if item is from 1.0.5 or lower
         if (damage >= 241) frameNum = damage >> 8;

         //check if block is from 1.0.5 or lower
         if (bedTags.hasKey("colorCombo")) frameNum = bedTags.getShort("colorCombo") >> 8;

         //change to adding plankNameSpace
         ItemStack plankIS = new ItemStack(Blocks.planks, 1, frameNum);
         String plankString = PlankHelper.plankStringfromItemStack(plankIS);
         bedTags.setString("plankNameSpace", plankString);

         return plankIS;
      } else if (!bedTags.hasKey("plankNameSpace")) {
         NBTTagList list = bedTags.getTagList("plank", 10);
         NBTTagCompound plank = list.getCompoundTagAt(0);
         //change Plank to plankNameSpace
         ItemStack plankIS = ItemStack.loadItemStackFromNBT(plank);
         if (isPlankKnown(plankIS)) {
            String plankString = PlankHelper.plankStringfromItemStack(plankIS);
            bedTags.setString("plankNameSpace", plankString);
            bedTags.removeTag("plank");//			return new ItemStack(Blocks.planks, 1, 0);
            return plankIS;
         } else {
            bedTags.setString("plankNameSpace", oakNameSpace);
            return oakItemStack;
         }
      } else {
         if (isPlankKnown(bedTags.getString("plankNameSpace"))) {
            String[] plank = bedTags.getString("plankNameSpace").split("@");
            return new ItemStack((Item) (Item.itemRegistry.getObject(new ResourceLocation(plank[0]))), 1, Integer.parseInt(plank[1]));

         } else {
            bedTags.setString("plankNameSpace", oakNameSpace);
            return oakItemStack;
         }
      }
   }

   public static String plankStringfromItemStack(ItemStack plank) {
      //BedCraftBeyond.logger.info(plank.toString());
      return Item.itemRegistry.getNameForObject(plank.getItem()) + "@" + plank.getItemDamage();
   }

   public static ItemStack plankItemStackfromString(String plank) {
      //BedCraftBeyond.logger.info(plank);
      String[] plankString = plank.split("@");
      return new ItemStack((Item) (Item.itemRegistry.getObject(new ResourceLocation(plankString[0]))), 1, Integer.parseInt(plankString[1]));
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
