package zornco.bedcraftbeyond.common.crafting.recipes;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterLayout;
import zornco.bedcraftbeyond.common.crafting.carpenter.CarpenterRecipe;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.item.ItemEyedropper;
import zornco.bedcraftbeyond.util.ColorHelper;

import java.awt.*;

public class RecipeDyeBottle implements CarpenterRecipe {

    private static final int OREDICT_DYE_R = OreDictionary.getOreID("dyeRed");
    private static final int OREDICT_DYE_G = OreDictionary.getOreID("dyeGreen");
    private static final int OREDICT_DYE_B = OreDictionary.getOreID("dyeBlue");

    private static final int OREDICT_WATERBOTTLE = OreDictionary.getOreID("bottleWater");

    @Override
    public ItemStack getRecipeOutput(TileCarpenter.CraftingHandler inv) {
        if(!matches(inv)) return null;
        ItemStack bottle = new ItemStack(BcbItems.dyeBottle, 1);
        ItemStack eyedropper = inv.extractItem(CarpenterLayout.CENTER, 1, true);
        Color cur = ItemEyedropper.getCurrentColor(eyedropper);
        if(!bottle.hasTagCompound()) bottle.setTagCompound(new NBTTagCompound());
        NBTTagCompound colorTag = ColorHelper.getTagForColor(cur);
        bottle.getTagCompound().setTag("color", colorTag);
        return bottle;
    }

    @Override
    public boolean matches(TileCarpenter.CraftingHandler inv) {
        ItemStack eyedropper = inv.extractItem(CarpenterLayout.CENTER, 1, true);
        if(eyedropper == null || !(eyedropper.getItem() instanceof ItemEyedropper)) return false;

        ItemStack dyeRed = inv.extractItem(CarpenterLayout.TOP_MIDDLE, 1, true);
        if(dyeRed == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(dyeRed), OREDICT_DYE_R))) return false;

        ItemStack dyeGreen = inv.extractItem(CarpenterLayout.MIDDLE_LEFT, 1, true);
        if(dyeGreen == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(dyeGreen), OREDICT_DYE_G))) return false;

        ItemStack dyeBlue = inv.extractItem(CarpenterLayout.MIDDLE_RIGHT, 1, true);
        if(dyeBlue == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(dyeBlue), OREDICT_DYE_B))) return false;

        ItemStack waterBottle = inv.extractItem(CarpenterLayout.BOTTOM_MIDDLE, 1, true);
        if(waterBottle == null || !(ArrayUtils.contains(OreDictionary.getOreIDs(waterBottle), OREDICT_WATERBOTTLE))) return false;

        return true;
    }

    @Override
    public ItemStack doCraft(TileCarpenter.CraftingHandler inv) {
        if(!matches(inv)) return null;

        inv.extractItem(CarpenterLayout.TOP_MIDDLE, 1, false);
        inv.extractItem(CarpenterLayout.MIDDLE_LEFT, 1, false);
        inv.extractItem(CarpenterLayout.MIDDLE_RIGHT, 1, false);
        inv.extractItem(CarpenterLayout.BOTTOM_MIDDLE, 1, false);

        return getRecipeOutput(inv);
    }
}
