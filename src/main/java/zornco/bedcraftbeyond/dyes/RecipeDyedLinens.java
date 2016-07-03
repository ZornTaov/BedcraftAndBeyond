package zornco.bedcraftbeyond.dyes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import zornco.bedcraftbeyond.beds.parts.BedPart;
import zornco.bedcraftbeyond.beds.parts.IBedPart;
import zornco.bedcraftbeyond.core.util.ColorHelper;
import zornco.bedcraftbeyond.dyes.bottle.ItemDyeBottle;

import java.awt.*;

public class RecipeDyedLinens implements IRecipe {

    /**
     * Used to check if a recipe matches current crafting inventory
     *
     * @param inv
     * @param worldIn
     */
    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        boolean hasDye = false, hasLinen = false;
        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) continue;
            if (stack.getItem() instanceof IBedPart &&
                ((IBedPart) stack.getItem()).getPartReference().getPartType().isLinenPart()) {
                if (hasLinen) return false;
                hasLinen = true;
            }

            if (stack.getItem() instanceof ItemDyeBottle) {
                if (hasDye) return false;
                hasDye = true;
            }
        }

        return hasDye && hasLinen;
    }

    /**
     * Returns an Item that is the result of this recipe
     *
     * @param inv
     */
    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack linen = null;
        ItemStack dye = null;

        for (int i = 0; i < inv.getSizeInventory(); ++i) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack == null) continue;
            if (stack.getItem() instanceof IBedPart) {
                BedPart ref = ((IBedPart) stack.getItem()).getPartReference();
                if (ref.getPartType().isLinenPart()) {
                    linen = stack;
                    if (dye != null) break;
                    continue;
                }
            }

            if (stack.getItem() instanceof ItemDyeBottle) {
                dye = stack;
                if (linen != null) break;
                continue;
            }
        }

        if (linen == null || dye == null) return null;
        ItemStack linenCopy = linen.copy();
        linenCopy.stackSize = 1;
        Color blended;
        Color linenOriginal = ColorHelper.getColorFromStack(linen);
        Color dyeColor = ColorHelper.getColorFromStack(dye);
        if (!linenOriginal.equals(Color.WHITE))
            blended = ColorHelper.blendColours(linenOriginal, dyeColor);
        else
            blended = dyeColor;

        NBTTagCompound colorTag = ColorHelper.getTagForColor(blended);
        linenCopy.getTagCompound().setTag("color", colorTag);
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
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return ForgeHooks.defaultRecipeGetRemainingItems(inv);
    }
}
