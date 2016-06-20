package zornco.bedcraftbeyond.common.crafting.recipes;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.common.crafting.CraftingTableLayout;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.item.ItemEyedropper;
import zornco.bedcraftbeyond.common.util.ColorHelper;

import javax.annotation.Nullable;
import java.awt.*;

public class RecipeDyeBottle implements IRecipe {

    private static final int OREDICT_DYE_R = OreDictionary.getOreID("dyeRed");
    private static final int OREDICT_DYE_G = OreDictionary.getOreID("dyeGreen");
    private static final int OREDICT_DYE_B = OreDictionary.getOreID("dyeBlue");

    private static final int OREDICT_WATERBOTTLE = OreDictionary.getOreID("bottleWater");

    @Override
    public boolean matches(InventoryCrafting inv, World w) {
        InvWrapper invWrapped = new InvWrapper(inv);

        ItemStack eyedropper = invWrapped.extractItem(CraftingTableLayout.CENTER, 1, true);
        if(eyedropper == null || !(eyedropper.getItem() instanceof ItemEyedropper)) return false;

        ItemStack dyeRed = invWrapped.extractItem(CraftingTableLayout.TOP_MIDDLE, 1, true);
        if(dyeRed == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(dyeRed), OREDICT_DYE_R))) return false;

        ItemStack dyeGreen = invWrapped.extractItem(CraftingTableLayout.MIDDLE_LEFT, 1, true);
        if(dyeGreen == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(dyeGreen), OREDICT_DYE_G))) return false;

        ItemStack dyeBlue = invWrapped.extractItem(CraftingTableLayout.MIDDLE_RIGHT, 1, true);
        if(dyeBlue == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(dyeBlue), OREDICT_DYE_B))) return false;

        ItemStack waterBottle = invWrapped.extractItem(CraftingTableLayout.BOTTOM_MIDDLE, 1, true);
        if(waterBottle == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(waterBottle), OREDICT_WATERBOTTLE))) return false;

        return true;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        InvWrapper wrapped = new InvWrapper(inv);
        ItemStack bottle = new ItemStack(BcbItems.dyeBottle, 1);
        ItemStack eyedropper = wrapped.extractItem(CraftingTableLayout.CENTER, 1, true);
        Color cur = ItemEyedropper.getCurrentColor(eyedropper);
        if(!bottle.hasTagCompound()) bottle.setTagCompound(new NBTTagCompound());
        NBTTagCompound colorTag = ColorHelper.getTagForColor(cur);
        bottle.getTagCompound().setTag("color", colorTag);
        return bottle;
    }

    @Override
    public int getRecipeSize() {
        return 9;
    }

    @Nullable
    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public ItemStack[] getRemainingItems(InventoryCrafting inv) {
        return new ItemStack[0];
    }
}
