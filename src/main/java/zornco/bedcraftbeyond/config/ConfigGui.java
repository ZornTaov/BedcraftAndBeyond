package zornco.bedcraftbeyond.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.ArrayList;
import java.util.List;

public class ConfigGui extends GuiConfig {

   public ConfigGui(GuiScreen parent) {
      super(parent, getConfigElements(), BedCraftBeyond.MOD_ID, false, false, "Bedcraft & Beyond Configuration");
   }

   /**
    * Compiles a list of config elements
    */
   private static List<IConfigElement> getConfigElements() {
      List<IConfigElement> list = new ArrayList<IConfigElement>();

      //Add categories to config GUI
      String guiPackage = "zornco.bedcraftbeyond.config.";
      list.add(categoryElement("general", "General", guiPackage + "CategoryGeneral"));
      list.add(categoryElement("frames", "Frames", guiPackage + "CategoryFrames"));

      return list;
   }

   /**
    * Creates a button linking to another screen where all options of the category are available
    */
   private static IConfigElement categoryElement(String category, String name, String tooltip_key) {
      return new DummyConfigElement.DummyCategoryElement(name, tooltip_key,
              new ConfigElement(BedCraftBeyond.config.getCategory(category)).getChildElements());
   }
}
