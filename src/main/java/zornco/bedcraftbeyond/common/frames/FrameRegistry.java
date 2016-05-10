package zornco.bedcraftbeyond.common.frames;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.*;

public class FrameRegistry {

   // Boolean is used to force only a set of metadata to be allowed
   private HashMap<ResourceLocation, Boolean> woodFrames;
   private HashMap<ResourceLocation, Boolean> stoneFrames;

   private HashMap<ResourceLocation, int[]> allowedMeta;

   public enum EnumBedFrameType { WOOD, STONE }

   private static FrameRegistry INSTANCE;

   private FrameRegistry(){
      woodFrames = new HashMap<>();
      stoneFrames = new HashMap<>();
      allowedMeta = new HashMap<>();
   }

   public static FrameRegistry getInstance(){
      if(INSTANCE == null) INSTANCE = new FrameRegistry();
      return INSTANCE;
   }

   public static void dumpFrameList(){
      // This makes me sad.
      getInstance().woodFrames.clear();
      getInstance().stoneFrames.clear();
      getInstance().allowedMeta.clear();
   }

   public static boolean addToFrameList(EnumBedFrameType type, ResourceLocation registryName){
      HashMap<ResourceLocation, Boolean> set = null;
      switch(type){
         case WOOD:
            set = getInstance().woodFrames; break;
         case STONE:
            set = getInstance().stoneFrames; break;
      }

      set.put(registryName, true);
      return true;
   }

   public static boolean removeFromFrameList(EnumBedFrameType type, ResourceLocation registryName){
      HashMap<ResourceLocation, Boolean> set = null;
      switch(type){
         case WOOD:
            set = getInstance().woodFrames; break;
         case STONE:
            set = getInstance().stoneFrames; break;
      }

      if(set.containsKey(registryName)){
         if(set.get(registryName)) getInstance().allowedMeta.remove(registryName);
         set.remove(registryName);
         return true;
      }

      return false;
   }

   public static int getWoodFrameCount(){
      return getInstance().woodFrames.size();
   }

   public static Set<ResourceLocation> getWoodFrameSet(){
      Set<ResourceLocation> allFrames = Collections.emptySet();
      for(Map.Entry<ResourceLocation, Boolean> woodEntry : getInstance().woodFrames.entrySet()){
         if(!woodEntry.getValue()){
            allFrames.add(woodEntry.getKey());
         } else {
            // TODO: Implement meta list
         }
      }
      return allFrames;
   }
}
