package zornco.bedcraftbeyond.common.frames;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.config.ConfigHelper;
import zornco.bedcraftbeyond.config.ConfigSettings;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FrameHelper {

   private static int removeFramesFromStream(InputStream stream, FrameRegistry.EnumBedFrameType type){
      FrameFile g = new Gson().fromJson(new InputStreamReader(stream), FrameFile.class);

      int removed = 0;
      for(String entry : g.blacklist){
         ResourceLocation entryRes = new ResourceLocation(entry);
         Block b = Block.blockRegistry.getObject(entryRes);
         if(b != null){
            if(entry.contains("@")){
               // TODO: Implement metadata only list
            } else {
               FrameRegistry.removeFromFrameList(type, b.getRegistryName());
               List<ItemStack> addedList = Collections.emptyList();
               b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
               removed += addedList.size();
            }
         }
      }

      // TODO: Add check to see if registry value supported, and check meta list for block type (minecraft:planks -> Spruce Wood, for example)
      return removed;
   }

   private static int addToFramesFromStream(InputStream stream, FrameRegistry.EnumBedFrameType type){
      FrameFile g = new Gson().fromJson(new InputStreamReader(stream), FrameFile.class);

      int added = 0;
      for(String entry : g.whitelist){
         ResourceLocation entryRes = new ResourceLocation(entry);
         Block b = Block.blockRegistry.getObject(entryRes);
         if(b != null){
            if(entry.contains("@")){
               // TODO: Implement metadata only list
            } else {
               FrameRegistry.addToFrameList(type, b.getRegistryName());
               List<ItemStack> addedList = Collections.emptyList();
               b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
               added += addedList.size();
            }
         }
      }

      // TODO: Add check to see if registry value supported, and check meta list for block type (minecraft:planks -> Spruce Wood, for example)
      return added;
   }

   private static void addWoodenOredictFrames(){
      for (ItemStack stack : OreDictionary.getOres("plankWood")) {
         //not bothering with items that are not blocks
         if (!(stack.getItem() instanceof ItemBlock)) continue;
         if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            //iterate over sub items
            ItemStack itemNoMeta = new ItemStack(stack.getItem());
            ResourceLocation registryName = itemNoMeta.getItem().getRegistryName();
            if (registryName == null) {
               BedCraftBeyond.logger.info("Found a null registry entry trying to add oreDict frames: " + itemNoMeta.toString());
               continue;
            }

            BedCraftBeyond.logger.info("Got plank \"" + stack.getDisplayName() + "\"." + "Registry name: " + registryName);
            FrameRegistry.addToFrameList(FrameRegistry.EnumBedFrameType.WOOD, stack.getItem().getRegistryName());
         }
      }
   }

   @SideOnly(Side.CLIENT)
   public static void compileFramesClient(){
      Logger l = BedCraftBeyond.logger;
      l.info("Loading frames from resource packs..");

      IResourceManager man = Minecraft.getMinecraft().getResourceManager();
      try {
         FrameRegistry.dumpFrameList();
         if(ConfigSettings.addWoodenOredictFrames) addWoodenOredictFrames();

         IResource vanilla = man.getResource(new ResourceLocation(BedCraftBeyond.MOD_ID, "wooden_frames.json"));
         l.info("Got wooden frames json from pack " + vanilla.getResourcePackName());
         InputStream vanillaStream = vanilla.getInputStream();
         int num_added = addToFramesFromStream(vanillaStream, FrameRegistry.EnumBedFrameType.WOOD);
         BedCraftBeyond.logger.info("Added " + num_added + " wooden frame types from wooden_frames.json");
      } catch (IOException e) {
         e.printStackTrace();
      }

      BedCraftBeyond.logger.info("Got a grand total of " + FrameRegistry.getWoodFrameCount() + " wooden frames to use.");
   }

   @SideOnly(Side.SERVER)
   public static void compileFramesServer(){
      File framesDir = Paths.get(ConfigHelper.modConfigDir.getPath(), "frames").toFile();
      File[] framesFiles = framesDir.listFiles();
      for(File f : framesFiles){
         try {
            JsonReader reader = new JsonReader(new FileReader(f));
            reader.beginObject();

            reader.endObject();
         }

         catch(IOException ioe){ }
      }
   }

}
