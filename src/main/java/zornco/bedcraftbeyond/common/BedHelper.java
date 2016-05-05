package zornco.bedcraftbeyond.common;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.config.ConfigHelper;

import java.io.File;
import java.nio.file.Paths;

public class BedHelper {

   @SideOnly(Side.SERVER)
   public static void compileWoodFrames(){
      File framesDir = Paths.get(ConfigHelper.modConfigDir.getPath(), "frames").toFile();
      File[] framesFiles = framesDir.listFiles();
      for(File f : framesFiles){

      }
   }

}
