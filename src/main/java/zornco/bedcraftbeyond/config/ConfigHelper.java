package zornco.bedcraftbeyond.config;

import net.minecraftforge.common.config.Configuration;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.io.File;
import java.nio.file.Paths;

public class ConfigHelper {

   private static boolean runConfigDirs;

   public static Configuration config;

   // This mod config directory (instance/config/MODID/)
   public static File modConfigDir;

   // All the mods config directory (instance/config/)
   public static File allModConfigsDir;

   public static void setupModDirs(){
      if(runConfigDirs) return;
      modConfigDir = Paths.get(allModConfigsDir.getPath(), BedCraftBeyond.MOD_ID).toFile();
      if(!modConfigDir.exists()) modConfigDir.mkdir();

      config = new Configuration(Paths.get(modConfigDir.getPath(), BedCraftBeyond.MOD_ID + ".cfg").toFile());

      File framesDir = Paths.get(modConfigDir.getPath(), "frames").toFile();
      if(!framesDir.exists()) framesDir.mkdir();

      runConfigDirs = true;
   }

}
