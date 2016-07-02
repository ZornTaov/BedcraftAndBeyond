package zornco.bedcraftbeyond.compat.jei.beds.wooden;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.core.ModContent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class WrapperWoodFrame extends BlankRecipeWrapper implements IShapedCraftingRecipeWrapper {

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 2;
    }

    @Nonnull
    @Override
    public List getInputs() {
        List l = new ArrayList<>();
        for(int i = 0; i < 3; i++) l.add(OreDictionary.getOres("slabWood"));
        for(int i = 0; i < 3; i++) l.add(new ItemStack(Blocks.PLANKS));
        return l;
    }

    @Nonnull
    @Override
    public List<ItemStack> getOutputs() {
        return ImmutableList.of(new ItemStack(ModContent.Items.woodenBed));
    }

}
