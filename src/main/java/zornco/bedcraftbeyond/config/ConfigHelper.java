package zornco.bedcraftbeyond.config;

import net.minecraftforge.common.config.Configuration;
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
      ConfigSettings.ADD_OREDICT_WOODEN = BedCraftBeyond.config.getBoolean("addWoodenFrames", "frames", true, "Add wooden frames from the ore dictionary.");
      ConfigSettings.ADD_OREDICT_STONE = BedCraftBeyond.config.getBoolean("addStoneFrames", "frames", true, "Add stone frames from the ore dictionary.");
      ConfigSettings.DRAWER_ITEM_LIMIT = BedCraftBeyond.config.getInt("drawerLimit", "beds", 12, 3, 15, "Number of slots the drawer gives to a bed frame.");

      if(BedCraftBeyond.config.hasChanged())
         BedCraftBeyond.config.save();
   }
}
