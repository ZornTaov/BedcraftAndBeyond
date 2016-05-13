package zornco.bedcraftbeyond.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.RecipeSorter;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.io.File;
import java.nio.file.Paths;

public class ConfigHelper {

   private static boolean runConfigDirs;

   // This mod config directory (instance/config/MODID/)
   public static File modConfigDir;

   // All the mods config directory (instance/config/)
   public static File allModConfigsDir;

   private static void setupConfigFile(){
      BedCraftBeyond.config = new Configuration(Paths.get(modConfigDir.getPath(), BedCraftBeyond.MOD_ID + ".cfg").toFile());
      refreshConfigs();
   }

   private static void setupModDirs(){
      if(runConfigDirs) return;
      modConfigDir = Paths.get(allModConfigsDir.getPath(), BedCraftBeyond.MOD_ID).toFile();
      if(!modConfigDir.exists()) modConfigDir.mkdir();

      File framesDir = Paths.get(modConfigDir.getPath(), "frames").toFile();
      if(!framesDir.exists()) framesDir.mkdir();

      runConfigDirs = true;
   }

   public static void setup(){
      setupModDirs();
      setupConfigFile();
   }

   public static void refreshConfigs(){
      ConfigSettings.addWoodenOredictFrames = BedCraftBeyond.config.getBoolean("addWoodenFrames", "frames", true, "Add wooden frames from the ore dictionary.");
      ConfigSettings.addStoneOredictFrames = BedCraftBeyond.config.getBoolean("addStoneFrames", "frames", true, "Add stone frames from the ore dictionary.");

      if(BedCraftBeyond.config.hasChanged())
         BedCraftBeyond.config.save();
   }
}
