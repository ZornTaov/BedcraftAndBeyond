package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class Templates {

    public static boolean hasTemplate(ResourceLocation rl){
        return getAllTemplateKeys().contains(rl);
    }

    public static Set<ResourceLocation> getAllTemplateKeys(){
        return CarpenterRecipes.getRecipeKeys();
    }
}
