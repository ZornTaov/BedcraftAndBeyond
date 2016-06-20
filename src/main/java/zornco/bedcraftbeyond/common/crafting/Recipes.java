package zornco.bedcraftbeyond.common.crafting;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.ShapedOreRecipe;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.recipes.*;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class Recipes {

    public static int recipesAdded = 0;

    public static void addRecipes() {
        recipesAdded = 0;

        OreDictionary.registerOre("bottleWater", new ItemStack(Items.POTIONITEM, 1, 0));

        // Drawer key
        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.drawerKey, 1), "xy", 'x', "ingotIron", 'y', "ingotGold"));
        ++recipesAdded;

        // Rug
        GameRegistry.addRecipe(new ItemStack(BcbItems.rug, 4), "xxx", 'x', new ItemStack(Blocks.WOOL, 1));
        ++recipesAdded;

        GameRegistry.addShapedRecipe(new ItemStack(BcbItems.blanket), "CCC", "CCC", 'C', Blocks.CARPET);
        GameRegistry.addShapedRecipe(new ItemStack(BcbItems.sheets), "CCC", 'C', Blocks.CARPET);
        recipesAdded += 2;

        GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BcbItems.eyedropper),
            "S", "G", "B", 'S', "slabWood", 'G', "blockGlass", 'B', new ItemStack(Items.POTIONITEM, 1, 0)));
        ++recipesAdded;

        GameRegistry.addRecipe(new RecipeDyedLinens());
        RecipeSorter.register(BedCraftBeyond.MOD_ID + ":linen_dyes", RecipeDyedLinens.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        GameRegistry.addRecipe(new RecipeDyeBottle());
        RecipeSorter.register(BedCraftBeyond.MOD_ID + ":dye_bottles", RecipeDyeBottle.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        GameRegistry.addRecipe(new RecipeDyedRug());
        RecipeSorter.register(BedCraftBeyond.MOD_ID + ":rug)dyes", RecipeDyedRug.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        GameRegistry.addRecipe(new RecipeBedFrameStone());
        RecipeSorter.register(BedCraftBeyond.MOD_ID + ":frame_wood", RecipeBedFrameStone.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");

        GameRegistry.addRecipe(new RecipeBedFrameWood());
        RecipeSorter.register(BedCraftBeyond.MOD_ID + ":frame_stone", RecipeBedFrameWood.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
    }

}
