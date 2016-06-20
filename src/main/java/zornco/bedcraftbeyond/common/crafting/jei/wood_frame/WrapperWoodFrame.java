package zornco.bedcraftbeyond.common.crafting.jei.wood_frame;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.common.item.BcbItems;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
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
        return ImmutableList.of(new ItemStack(BcbItems.woodenBed));
    }

}
