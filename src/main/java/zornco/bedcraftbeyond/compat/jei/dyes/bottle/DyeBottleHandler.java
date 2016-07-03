package zornco.bedcraftbeyond.compat.jei.dyes.bottle;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import zornco.bedcraftbeyond.dyes.bottle.RecipeDyeBottle;

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
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull RecipeDyeBottle recipeDyeBottle) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeDyeBottle recipeDyeBottle) {
        return new DyeBottleWrapper();
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeDyeBottle recipeDyeBottle) {
        return true;
    }
}
