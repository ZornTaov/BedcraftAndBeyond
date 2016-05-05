package zornco.bedcraftbeyond.common.frames;

import com.sun.org.apache.regexp.internal.RESyntaxException;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

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

   @Deprecated
   public static boolean addToWoodList(ResourceLocation block){
      return addToFrameList(EnumBedFrameType.WOOD, block);
   }

   @Deprecated
   public static boolean addToStoneList(ResourceLocation block) {
      return addToFrameList(EnumBedFrameType.STONE, block);
   }

   public static int getWoodFrameCount(){
      return getInstance().woodFrames.size();
   }
}
