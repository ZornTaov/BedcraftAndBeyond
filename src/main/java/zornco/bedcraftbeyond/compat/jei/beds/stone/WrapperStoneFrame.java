package zornco.bedcraftbeyond.compat.jei.beds.stone;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.core.BcbItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WrapperStoneFrame extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {

    @Nonnull
    @Override
    public List getInputs() {
        List l = new ArrayList<>();
        for(int i = 0; i < 3; i++) l.add(new ItemStack(Blocks.STONE_SLAB));
        for(int i = 0; i < 3; i++) l.add(new ItemStack(Blocks.STONE));
        return l;
    }

    @Nonnull
    @Override
    public List getOutputs() {
        return ImmutableList.of(new ItemStack(BcbItems.stoneBed));
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 2;
    }
}
