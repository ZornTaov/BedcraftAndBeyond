package zornco.bedcraftbeyond.common.frames;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;

import java.util.*;

public class FrameRegistry {

   // Boolean is used to force only a set of metadata to be allowed
   private HashMap<ResourceLocation, Set<Integer>> woodFrames;
   private HashMap<ResourceLocation, Set<Integer>> stoneFrames;

   public enum EnumBedFrameType { WOOD, STONE }

   private static FrameRegistry INSTANCE;

   private FrameRegistry(){
      woodFrames = new HashMap<>();
      stoneFrames = new HashMap<>();
   }

   public static FrameRegistry getInstance(){
      if(INSTANCE == null) INSTANCE = new FrameRegistry();
      return INSTANCE;
   }

   public static void dumpFrameList(){
      // This makes me sad.
      getInstance().woodFrames.clear();
      getInstance().stoneFrames.clear();
   }

   public static HashMap<ResourceLocation, Set<Integer>> getFrameSet(EnumBedFrameType type){
      HashMap<ResourceLocation, Set<Integer>> set = null;
      switch(type){
         case WOOD:
            set = getInstance().woodFrames; break;
         case STONE:
            set = getInstance().stoneFrames; break;
      }

      return set;
   }

   public static boolean addEntry(EnumBedFrameType type, ResourceLocation registryName){
      return addEntry(type, registryName, OreDictionary.WILDCARD_VALUE);
   }

   public static boolean addEntry(EnumBedFrameType type, ResourceLocation registryName, int meta){
      // DO not keep instances of meta < 0 in registry
      if(meta < 0) return false;

      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName))
         if(meta != OreDictionary.WILDCARD_VALUE)
            return set.get(registryName).add(meta);
         else
            return false;

      if(meta == OreDictionary.WILDCARD_VALUE) {
         set.put(registryName, new HashSet<>());
         return true;
      }

      HashSet<Integer> newSet = new HashSet<>();
      newSet.add(meta);
      set.put(registryName, newSet);
      return true;
   }

   public static boolean removeEntry(EnumBedFrameType type, ResourceLocation registryName){
    return removeEntry(type, registryName, OreDictionary.WILDCARD_VALUE);
   }

   /**
    *
    * @param type
    * @param regName
    * @param meta A specific meta index to remove from the whitelist. If equal to {@see OreDictionary.WILDCARD_VALUE}
    *             then this completely removes the entry from the specified set.
    * @return
    */
   public static boolean removeEntry(EnumBedFrameType type, ResourceLocation regName, int meta){
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      if(set.containsKey(regName)){
         if(meta == OreDictionary.WILDCARD_VALUE)
            set.remove(regName);
         return set.get(regName).remove(meta);
      }

      return false;
   }

   public static boolean clearEntryWhitelist(EnumBedFrameType type, ResourceLocation registryName){
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName)){
         set.get(registryName).clear();
         return true;
      }

      return false;
   }

   public static long getFrameTypeCount(EnumBedFrameType type) {
      long amt = 0;
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      for(ResourceLocation rl : set.keySet()){
         if(set.get(rl).size() == 0){
            // TODO: Possibly find a better means to add up subItems on a server
            if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
               List<ItemStack> stacks = new ArrayList<>();
               Item i = Item.getByNameOrId(rl.toString());
               i.getSubItems(i, CreativeTabs.tabAllSearch, stacks);
               amt += stacks.size();
               continue;
            }

            ++amt; continue;
         }

         amt += set.get(rl).size();
      }

      return amt;
   }
}
