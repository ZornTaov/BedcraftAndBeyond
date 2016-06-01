package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.item.frames.ItemWoodenFrame;

import java.util.HashMap;
import java.util.HashSet;

public class CarpenterRecipes {

    public static HashMap<ResourceLocation, CarpenterRecipe> recipes;

    public static void registerAll(){
        recipes = new HashMap<>();
        recipes.put(new ResourceLocation(BedCraftBeyond.MOD_ID, "wooden_frame"), new RecipeWoodFrame());
    }

}
