package zornco.bedcraftbeyond.common.frames;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.*;

public class FrameRegistry {

   // Key is an ALLOWED block registry type. If a meta value is in the Set, though,
   // then it is BLACKLISTED as a valid block type. If the set is empty, then all is good and any subtype will work.
   private HashMap<ResourceLocation, Set<Integer>> woodFrames;
   private HashMap<ResourceLocation, Set<Integer>> stoneFrames;

   public enum EnumBedFrameType { WOOD, STONE }

   public static FrameRegistry INSTANCE = new FrameRegistry();

   private FrameRegistry(){
      woodFrames = new HashMap<>();
      stoneFrames = new HashMap<>();
   }

   public static void dumpFrameList(){
      // This makes me sad.
      INSTANCE.woodFrames.clear();
      INSTANCE.stoneFrames.clear();
   }

   public static HashMap<ResourceLocation, Set<Integer>> getFrameSet(EnumBedFrameType type){
      HashMap<ResourceLocation, Set<Integer>> set = null;
      switch(type){
         case WOOD:
            set = INSTANCE.woodFrames; break;
         case STONE:
            set = INSTANCE.stoneFrames; break;
      }

      return set;
   }

   /**
    * Adds an entry to the whitelist with a clear blacklist on meta.
    *
    * @param type
    * @param registryName
    * @return
    */
   public static boolean addEntry(EnumBedFrameType type, ResourceLocation registryName) throws FrameException {
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName))
         throw new FrameException("That block is already in the valid block list for type = " + type.name());

      set.put(registryName, new HashSet<>());
      return true;
   }

   public static boolean addBlacklistEntry(EnumBedFrameType type, ResourceLocation registryName, int meta) throws FrameException {
      // DO not keep instances of meta < 0 in registry
      if(meta < 0) throw new FrameException("Cannot add negative meta indexes to an entry blacklist.");

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

   public static boolean clearBlacklistForEntry(EnumBedFrameType type, ResourceLocation registryName){
    return removeBlacklistEntry(type, registryName, OreDictionary.WILDCARD_VALUE);
   }

   public static boolean removeEntry(EnumBedFrameType type, ResourceLocation registryName){
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName)){
         set.remove(registryName);
         return true;
      }

      return false;
   }

   /**
    * Removes a specific metadata entry from a given registry entry's blacklist.
    * If passed OreDictionary.WILDCARD_VALUE, it clears the blacklist for an entry.
    * @param type
    * @param regName
    * @param meta A specific meta index to remove from the whitelist. If equal to {@see OreDictionary.WILDCARD_VALUE}
    *             then this completely removes the entry from the specified set.
    * @return
    */
   public static boolean removeBlacklistEntry(EnumBedFrameType type, ResourceLocation regName, int meta){
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      if(set.containsKey(regName)){
         if(meta == OreDictionary.WILDCARD_VALUE) {
            set.get(regName).clear();
            return true;
         }

         return set.get(regName).remove(meta);
      }

      return false;
   }

   public static long getFrameTypeCount(EnumBedFrameType type) {
      long amt = 0;
      HashMap<ResourceLocation, Set<Integer>> set = getFrameSet(type);
      for(ResourceLocation rl : set.keySet()){
         if(set.get(rl).size() == 0){
            // TODO: Possibly find a better means to add up subItems on a server?
            if(FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
               List<ItemStack> stacks = new ArrayList<>();
               Item i = Item.getByNameOrId(rl.toString());
               i.getSubItems(i, CreativeTabs.SEARCH, stacks);
               amt += stacks.size();
               continue;
            }

            BedCraftBeyond.logger.debug("You are on a server, exact frame count will be unknown due to client code.");
            ++amt; continue;
         }

         amt += set.get(rl).size();
      }

      return amt;
   }
}
