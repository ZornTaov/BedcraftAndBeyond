package zornco.bedcraftbeyond.compat.jei.dye_bottle;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.asm.transformers.ItemStackTransformer;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeDyeBottle;
import zornco.bedcraftbeyond.common.item.BcbItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WrapperDyeBottle extends BlankRecipeWrapper {

    public WrapperDyeBottle(RecipeDyeBottle recipe){

    }

    @Nonnull
    @Override
    public List getInputs() {
        return ImmutableList.of(
          null, OreDictionary.getOres("dyeRed"), null,
            OreDictionary.getOres("dyeGreen"), new ItemStack(BcbItems.eyedropper), OreDictionary.getOres("dyeBlue"),
            null, OreDictionary.getOres("bottleWater"), null
        );
    }

    @Nonnull
    @Override
    public List getOutputs() {
        return ImmutableList.of(new ItemStack(BcbItems.dyeBottle));
    }
}
