package zornco.bedcraftbeyond.common.crafting.jei.dye_bottle;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.common.item.BcbItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WrapperDyeBottle extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {

    public WrapperDyeBottle(){

    }

    @Nonnull
    @Override
    public List getInputs() {
        List l = new ArrayList<>();
        // _ R _
        // G E B
        // _ W _

        l.add(null); l.add(OreDictionary.getOres("dyeRed")); l.add(null);
        l.add(OreDictionary.getOres("dyeGreen"));
        l.add(new ItemStack(BcbItems.eyedropper));
        l.add(OreDictionary.getOres("dyeBlue"));
        l.add(null); l.add(OreDictionary.getOres("bottleWater")); l.add(null);
        return l;
    }

    @Nonnull
    @Override
    public List getOutputs() {
        return ImmutableList.of(new ItemStack(BcbItems.dyeBottle));
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 3;
    }
}
