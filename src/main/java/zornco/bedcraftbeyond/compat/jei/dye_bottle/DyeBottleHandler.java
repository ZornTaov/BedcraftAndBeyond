package zornco.bedcraftbeyond.compat.jei.dye_bottle;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeDyeBottle;

import javax.annotation.Nonnull;

public class DyeBottleHandler implements IRecipeHandler<RecipeDyeBottle> {

    @Nonnull
    @Override
    public Class<RecipeDyeBottle> getRecipeClass() {
        return RecipeDyeBottle.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return BedCraftBeyond.MOD_ID + ":dye_bottle";
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeDyeBottle recipeDyeBottle) {
        return new WrapperDyeBottle(recipeDyeBottle);
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeDyeBottle recipeDyeBottle) {
        return true;
    }
}
