package zornco.bedcraftbeyond.common.crafting.carpenter;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;

public interface CarpenterRecipe {

    ItemStack getRecipeOutput();

    boolean matches(TileCarpenter.CraftingHandler inv);

    ItemStack doCraft(TileCarpenter.CraftingHandler inv, boolean simulate);
}
