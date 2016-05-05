package zornco.bedcraftbeyond.common.frames;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.config.ConfigHelper;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FrameHelper {

   private static void addFramesFromStream(InputStream stream){
      FrameJSON g = new Gson().fromJson(new InputStreamReader(stream), FrameJSON.class);

      for(String entry : g.wooden){
         ResourceLocation entryRes = new ResourceLocation(entry);
         Block b = Block.blockRegistry.getObject(entryRes);
         if(b != null){
            // Add meta to block list
            FrameRegistry.addToFrameList(FrameRegistry.EnumBedFrameType.WOOD, b.getRegistryName());
            if(entry.contains("@")){
               // TODO: Implement metadata only list
            }
         }
      }

      // TODO: Add check to see if registry value supported, and check meta list for block type (minecraft:planks -> Spruce Wood, for example)
      BedCraftBeyond.logger.info("Registered " + FrameRegistry.getInstance().getWoodFrameCount() + " wood frame types.");
   }

   @SideOnly(Side.CLIENT)
   public static void compileFramesClient(){
      Logger l = BedCraftBeyond.logger;
      l.info("Loading frames from resource packs..");

      IResourceManager man = Minecraft.getMinecraft().getResourceManager();
      try {
         IResource vanilla = man.getResource(new ResourceLocation(BedCraftBeyond.MOD_ID, "frames/minecraft.json"));
         l.info("Got vanilla json from pack " + vanilla.getResourcePackName());
         InputStream vanillaStream = vanilla.getInputStream();
         addFramesFromStream(vanillaStream);
      } catch (IOException e) {
         e.printStackTrace();
      }
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
