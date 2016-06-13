package zornco.bedcraftbeyond.common.crafting.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipe;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.items.ItemHandlerGridHelper;
import zornco.bedcraftbeyond.items.ItemHelper;

import java.awt.*;

public class RecipeStoneFrame implements CarpenterRecipe {

    private static final int stoneSlabID = OreDictionary.getOreID("slabStone");

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(BcbItems.stoneBed, 1);
    }

    @Override
    public boolean matches(TileCarpenter.CraftingHandler inv) {
        IItemHandler slabTest = ItemHandlerGridHelper.getItemsWrapped(inv, new Rectangle(0,1, 3,1));
        IItemHandler stoneTest = ItemHandlerGridHelper.getItemsWrapped(inv, new Rectangle(0,2, 3,1));

        if(slabTest.getSlots() < 3 || stoneTest.getSlots() < 3) return false;
        if(!ItemHelper.areItemStacksEqual(slabTest)) return false;
        if(!ItemHelper.areItemStacksEqual(stoneTest)) return false;

        if(!FrameRegistry.isValidFrameMaterial(FrameRegistry.EnumFrameType.STONE, stoneTest.getStackInSlot(0)))
            return false;

        return true;
    }

    @Override
    public ItemStack doCraft(TileCarpenter.CraftingHandler inv, boolean simulate) {
        if(!matches(inv)) return null;
        for(int y = 3; y < 9; y++)
            inv.extractItem(y, 1, simulate);

        return new ItemStack(BcbItems.stoneBed, 1);
    }
}
