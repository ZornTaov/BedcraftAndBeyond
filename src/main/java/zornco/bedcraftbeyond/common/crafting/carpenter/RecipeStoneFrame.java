package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.common.item.BcbItems;

public class RecipeStoneFrame implements CarpenterRecipe {

    private static final int stoneSlabID = OreDictionary.getOreID("slabStone");

    @Override
    public int getNumberOfSlots() {
        return 2;
    }

    @Override
    public boolean matches(TileCarpenter.CarpenterCraftingItemHandler inv) {
        ItemStack stone = inv.extractItem(0, 3, true);
        ItemStack slabs = inv.extractItem(1, 3, true);

        // If not enough stone
        if(stone == null || stone.stackSize != 3 || !FrameRegistry.isValidFrameMaterial(FrameRegistry.EnumFrameType.STONE, stone))
            return false;

        // If not enough slabs
        if(slabs == null || slabs.stackSize != 3) return false;

        // If slabs (item 1) not a stone slab
        if(!ArrayUtils.contains(OreDictionary.getOreIDs(slabs), stoneSlabID)) return false;

        return true;
    }

    @Override
    public CarpenterRecipeOutput doCraft(TileCarpenter.CarpenterCraftingItemHandler crafting, boolean simulate) {
        if(!matches(crafting)) return null;
        return new CarpenterRecipeOutput(new ItemStack(BcbItems.stoneBed, 1));
    }
}
