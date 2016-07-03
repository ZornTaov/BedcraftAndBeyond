package zornco.bedcraftbeyond.compat.jei.beds.stone;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import zornco.bedcraftbeyond.beds.stone.RecipeBedFrameStone;

import javax.annotation.Nonnull;

public class StoneFrameHandler implements IRecipeHandler<RecipeBedFrameStone> {

    @Nonnull
    @Override
    public Class<RecipeBedFrameStone> getRecipeClass() {
        return RecipeBedFrameStone.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull RecipeBedFrameStone recipe) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeBedFrameStone recipe) {
        return new WrapperStoneFrame();
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeBedFrameStone recipe) {
        return true;
    }
}
