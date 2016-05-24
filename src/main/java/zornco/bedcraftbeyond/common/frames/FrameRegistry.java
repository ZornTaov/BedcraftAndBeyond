package zornco.bedcraftbeyond.common.frames;


import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.OreDictionary;
import sun.reflect.generics.tree.Tree;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.*;

public class FrameRegistry {

   // Key is an ALLOWED block registry type. If a meta value is in the RangeSet, then it is a valid block type.
   private HashMap<ResourceLocation, RangeSet<Integer>> woodFrames;
   private HashMap<ResourceLocation, RangeSet<Integer>> stoneFrames;

   /**
    * Specifies the available frame types for making beds and things.
    */
   public enum EnumBedFrameType { WOOD, STONE }

   private static FrameRegistry INSTANCE = new FrameRegistry();

   private FrameRegistry(){
      woodFrames = new HashMap<>();
      stoneFrames = new HashMap<>();
   }

   public static void dumpFrameList(){
      // This makes me sad.
      INSTANCE.woodFrames.clear();
      INSTANCE.stoneFrames.clear();
   }

   public static HashMap<ResourceLocation, RangeSet<Integer>> getFrameSet(EnumBedFrameType type){
      HashMap<ResourceLocation, RangeSet<Integer>> set = null;
      switch(type){
         case WOOD:
            set = INSTANCE.woodFrames; break;
         case STONE:
            set = INSTANCE.stoneFrames; break;
      }

      return set;
   }

   /**
    * Checks if a given itemStack is registered in the registry for a given type.
    * This checks item metadata and hopes that the meta is the same as the blockstate.
    * If not, please use the method with a direct check for meta instead.
    *
    * @param type
    * @param stack
    * @return
     */
   // TODO: Check if this has issues with mods
   public static boolean isValidFrameMaterial(EnumBedFrameType type, ItemStack stack){
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      Block b = Block.getBlockFromItem(stack.getItem());
      ResourceLocation rl = b.getRegistryName();

      return isValidFrameMaterial(type, rl, stack.getMetadata());
   }

   public static boolean isValidFrameMaterial(EnumBedFrameType type, ResourceLocation registryName, int meta){
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      return set.containsKey(registryName) && set.get(registryName).contains(meta);
   }

   /**
    * Adds an entry to the whitelist with a clear blacklist on meta.
    *
    * @param type
    * @param registryName
    * @return
    */
   public static boolean addEntry(EnumBedFrameType type, ResourceLocation registryName) throws FrameException {
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName))
         throw new FrameException("That block is already in the valid block list for type = " + type.name());

      resetWhitelistForEntry(type, registryName);
      return true;
   }

   public static void resetWhitelistForEntry(EnumBedFrameType type, ResourceLocation registryName) {
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName)) set.get(registryName).clear();
      else set.put(registryName, TreeRangeSet.create());

      set.get(registryName).add(Range.closedOpen(0, 15));
   }

   public static boolean addWhitelistEntry(EnumBedFrameType type, ResourceLocation registryName, int meta) throws FrameException {
      // DO not keep instances of meta < 0 in registry
      if(meta < 0) throw new FrameException("Cannot add negative meta indexes to an entry whitelist.");

      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName))
         if(meta != OreDictionary.WILDCARD_VALUE)
            set.get(registryName).add(Range.closed(meta, meta));
         else {
            resetWhitelistForEntry(type, registryName);
            return true;
         }


      if(meta == OreDictionary.WILDCARD_VALUE) {
         resetWhitelistForEntry(type, registryName);
         return true;
      }

      RangeSet<Integer> newSet = TreeRangeSet.create();
      newSet.add(Range.closed(meta, meta));
      set.put(registryName, newSet);

      return set.get(registryName).contains(meta);
   }

   public static boolean removeEntry(EnumBedFrameType type, ResourceLocation registryName){
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      if(set.containsKey(registryName)){
         set.remove(registryName);
         return true;
      }

      return false;
   }

   /**
    * Removes a set of integers (a range) from the whitelist for a given entry.
    *
    * @param type
    * @param regName
    * @param range The range of metadata to remove from the whitelist.
     * @return
     */
   public static boolean removeWhitelistEntries(EnumBedFrameType type, ResourceLocation regName, Range<Integer> range){
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      set.get(regName).remove(range);
      return set.get(regName).encloses(range);
   }

   /**
    * Removes a specific metadata entry from a given registry entry's whitelist.
    * If passed OreDictionary.WILDCARD_VALUE, it clears the whitelist for an entry.
    * @param type
    * @param regName
    * @param meta A specific meta index to remove from the whitelist. If equal to {@see OreDictionary.WILDCARD_VALUE}
    *             then this completely removes the entry from the specified set.
    * @return
    */
   public static boolean removeWhitelistEntry(EnumBedFrameType type, ResourceLocation regName, int meta){
      HashMap<ResourceLocation, RangeSet<Integer>> set = getFrameSet(type);
      if(set.containsKey(regName)){
         if(meta == OreDictionary.WILDCARD_VALUE) {
            set.get(regName).clear();
            return true;
         }

         return removeWhitelistEntries(type, regName, Range.closed(meta, meta));
      }

      return false;
   }
}
