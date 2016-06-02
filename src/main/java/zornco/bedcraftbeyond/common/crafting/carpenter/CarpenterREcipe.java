package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;

public interface CarpenterRecipe {

    /**
     * Gets the number of slots needed to craft with this recipe.
     * @return
     */
    int getNumberOfSlots();

    boolean matches(TileCarpenter.CarpenterCraftingItemHandler inv);

    CarpenterRecipeOutput doCraft(TileCarpenter.CarpenterCraftingItemHandler crafting, boolean simulate);

}
