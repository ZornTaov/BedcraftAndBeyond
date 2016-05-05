package zornco.bedcraftbeyond.config;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class BedcraftConfig extends Configuration {

   // public static boolean removeVanillaBed;

   public BedcraftConfig(File f){
      super(f);
      refreshConfigs();
   }

   public static void refreshConfigs(){
      // Removed: Need vanilla bed for a recipe
      // removeVanillaBed = this.getBoolean("removeVanillaBed", "beds", true, "Remove the vanilla bed and its crafting recipes?");
   }

}
