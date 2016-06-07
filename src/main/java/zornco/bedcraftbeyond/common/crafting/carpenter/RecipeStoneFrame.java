package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class RecipeStoneFrame implements CarpenterRecipe {

    private static final int stoneSlabID = OreDictionary.getOreID("slabStone");

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(BcbItems.stoneBed, 1);
    }

    @Override
    public boolean matches(TileCarpenter.CraftingHandler inv) {
        ItemStack stone = inv.extractItem(0, 3, true);
        ItemStack slabs = inv.extractItem(1, 3, true);

        // If not enough stone
        if(stone == null || stone.stackSize != 3 || !FrameRegistry.isValidFrameMaterial(FrameRegistry.EnumFrameType.STONE, stone))
            return false;

        // If not enough slabs
        if(slabs == null || slabs.stackSize != 3) return false;

        // If slabs (item 1) not a stone slab
        if(!slabs.getItem().getRegistryName().equals(Blocks.STONE_SLAB.getRegistryName()))
            return false;

        return true;
    }

    @Override
    public ItemStack doCraft(TileCarpenter.CraftingHandler inv, boolean simulate) {
        if(!matches(inv)) return null;
        inv.extractItem(0, 3, simulate);
        inv.extractItem(1, 3, simulate);

        return new ItemStack(BcbItems.stoneBed, 1);
    }
}
