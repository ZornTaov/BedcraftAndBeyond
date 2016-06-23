package zornco.bedcraftbeyond.compat.jei.rugs;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.core.BcbItems;

import javax.annotation.Nonnull;
import java.util.List;

public class WrapperDyedRugs extends BlankRecipeWrapper implements ICraftingRecipeWrapper {

    @Nonnull
    @Override
    public List getInputs() {
        return ImmutableList.of(OreDictionary.getOres("dye"), new ItemStack(BcbItems.rug));
    }

    @Nonnull
    @Override
    public List getOutputs() {
        return ImmutableList.of(new ItemStack(BcbItems.rug));
    }
}
