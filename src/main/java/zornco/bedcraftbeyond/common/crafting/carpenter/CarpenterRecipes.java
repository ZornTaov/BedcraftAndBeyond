package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeStoneFrame;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeWoodFrame;

import java.util.HashMap;

public class CarpenterRecipes {

    public static HashMap<ResourceLocation, CarpenterRecipe> recipes;

    public static void registerAll(){
        recipes = new HashMap<>();
        recipes.put(new ResourceLocation(BedCraftBeyond.MOD_ID, "wooden_frame"), new RecipeWoodFrame());
        recipes.put(new ResourceLocation(BedCraftBeyond.MOD_ID, "stone_frame"), new RecipeStoneFrame());
    }

    public static CarpenterRecipe getFromItem(ItemStack stack){
        if(stack == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("recipe"))
            return null;
        ResourceLocation rec = new ResourceLocation(stack.getTagCompound().getString("recipe"));
        if(!recipes.containsKey(rec)) return null;
        return recipes.get(rec);
    }
}
