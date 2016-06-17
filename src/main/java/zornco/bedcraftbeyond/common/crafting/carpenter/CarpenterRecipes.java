package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeDyeBottle;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeStoneFrame;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeWoodFrame;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.item.ItemTemplate;

import java.util.HashMap;
import java.util.Set;

import static zornco.bedcraftbeyond.common.crafting.recipes.Recipes.recipesAdded;

public class CarpenterRecipes {

    private static HashMap<ResourceLocation, CarpenterRecipe> recipes;

    public static Set<ResourceLocation> getRecipeKeys(){
        return recipes.keySet();
    }

    public static void registerAll(){
        recipes = new HashMap<>();

        // Wooden Frame
        createRecipe(new ResourceLocation(BedCraftBeyond.MOD_ID, "wooden_frame"), new RecipeWoodFrame(), "plankWood");

        // Stone Frame
        createRecipe(new ResourceLocation(BedCraftBeyond.MOD_ID, "stone_frame"), new RecipeStoneFrame(), Blocks.STONE);

        // Dye bottle
        createRecipe(new ResourceLocation(BedCraftBeyond.MOD_ID, "dye_bottle"), new RecipeDyeBottle(), "bottleWater");
    }

    private static void createRecipe(ResourceLocation rl, CarpenterRecipe rec, Object craftPiece){
        recipes.put(rl, rec);
        GameRegistry.addRecipe(new ShapelessOreRecipe(ItemTemplate.generateItemWithRecipe(rl, 1), craftPiece, BcbItems.template));
        ++recipesAdded;
    }

    public static CarpenterRecipe getRecipe(ResourceLocation key){
        if(!recipes.containsKey(key)) return null;
        return recipes.get(key);
    }

    public static CarpenterRecipe getFromItem(ItemStack stack){
        if(stack == null || !stack.hasTagCompound() || !stack.getTagCompound().hasKey("recipe"))
            return null;
        ResourceLocation rec = new ResourceLocation(stack.getTagCompound().getString("recipe"));
        if(!recipes.containsKey(rec)) return null;
        return recipes.get(rec);
    }
}
