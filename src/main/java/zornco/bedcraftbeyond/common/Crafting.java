package zornco.bedcraftbeyond.common;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class Crafting {

   public static int recipesAdded = 0;
   public static void addRecipes(){
      recipesAdded = 0;

      // Drawer key
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.drawerKey, 1), "xy", 'x', "ingotIron", 'y', "ingotGold"));
      ++recipesAdded;

      // Rug
      GameRegistry.addRecipe(new ItemStack(BcbItems.rug, 4), "xxx", 'x', new ItemStack(Blocks.wool, 1));
      ++recipesAdded;

      // Stone bed
      GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.stoneBed, 1, 0),
              "SSS", "sss",
              'S', "blockStone",
              's', new ItemStack(Blocks.stone_slab, 1, 0)
      ));
      ++recipesAdded;

      // Removed: Beds are now frames, we need this.
      // GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.bed), "coloredBed"));  ++recipesAdded;
   }

}
