package zornco.bedcraftbeyond.dyes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import zornco.bedcraftbeyond.core.BcbItems;
import zornco.bedcraftbeyond.rugs.ItemRug;

public class RecipeDyedRug implements IRecipe {

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inv
     * @param worldIn
     */
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean hasDye = false, hasRug = false;
        for(int i = 0; i < inv.getSizeInventory(); ++i){
            ItemStack stack = inv.getStackInSlot(i);
            if(stack == null) continue;
            if(stack.getItem() instanceof ItemRug) {
                if (hasRug) return false;
                hasRug = true;
            }

            if(stack.getItem() instanceof ItemDye){
                if(hasDye) return false;
                hasDye = true;
            }
        }

        return hasDye && hasRug;
    }

    /**
     * Returns an Item that is the result of this recipe
     *
     * @param inv
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack rug = null;
        ItemStack dye = null;

        for(int i = 0; i < inv.getSizeInventory(); ++i){
            ItemStack stack = inv.getStackInSlot(i);
            if(stack == null) continue;
            if(stack.getItem() instanceof ItemRug) {
                rug = stack;
                if(dye != null) break;
                continue;
            }

            if(stack.getItem() instanceof ItemDye){
                dye = stack;
                if(rug != null) break;
                continue;
            }
        }

        if(rug == null || dye == null) return null;
        ItemStack linenCopy = rug.copy();
        linenCopy.stackSize = 1;
        EnumDyeColor dyeColor = EnumDyeColor.byMetadata(dye.getMetadata());
        linenCopy.setItemDamage(dyeColor.getDyeDamage());
        return linenCopy;
    }

    /**
     * Returns the size of the recipe area
     */
    @Override
    public int getRecipeSize() {
        return 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(BcbItems.rug, 1);
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
