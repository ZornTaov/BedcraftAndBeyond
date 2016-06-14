package zornco.bedcraftbeyond.common.crafting.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipes;
import zornco.bedcraftbeyond.common.crafting.carpenter.Templates;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeLinenItems;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.item.ItemTemplate;

public class Recipes {

    public static int recipesAdded = 0;

    public static void addRecipes() {
        recipesAdded = 0;

        // Drawer key
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.drawerKey, 1), "xy", 'x', "ingotIron", 'y', "ingotGold"));
        ++recipesAdded;

        // Rug
        GameRegistry.addRecipe(new ItemStack(BcbItems.rug, 4), "xxx", 'x', new ItemStack(Blocks.WOOL, 1));
        ++recipesAdded;

        // Removed: Beds are now frames, we need this.
        // GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(Items.bed), "coloredBed"));  ++recipesAdded;
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.blanket),
            "CC", "CC", 'C', Blocks.CARPET));
        ++recipesAdded;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.sheets),
            "CC", 'C', Blocks.CARPET));

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.eyedropper),
            "S", "G", "G", 'S', "slabWood", 'G', "blockGlass"));
        ++recipesAdded;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.template, 4),
            " P ", "PCP", " P ", 'P', "paper", 'C', Blocks.CRAFTING_TABLE));
        ++recipesAdded;

        RecipeLinenItems rli = new RecipeLinenItems();
        GameRegistry.addRecipe(rli);
        RecipeSorter.register(BedCraftBeyond.MOD_ID + ":linen_dyes", RecipeLinenItems.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        CarpenterRecipes.registerAll();
    }

}
