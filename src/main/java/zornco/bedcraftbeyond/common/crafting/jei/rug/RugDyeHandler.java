package zornco.bedcraftbeyond.common.crafting.jei.rug;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import zornco.bedcraftbeyond.common.crafting.recipes.RecipeDyedRug;

import javax.annotation.Nonnull;

public class RugDyeHandler implements IRecipeHandler<RecipeDyedRug> {

    @Nonnull
    @Override
    public Class<RecipeDyedRug> getRecipeClass() {
        return RecipeDyedRug.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull RecipeDyedRug recipeDyedRugs) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeDyedRug recipeDyedRugs) {
        return new WrapperDyedRugs();
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeDyedRug recipeDyedRugs) {
        return true;
    }
}
