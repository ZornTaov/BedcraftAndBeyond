package zornco.bedcraftbeyond.compat.jei.beds.wooden;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import zornco.bedcraftbeyond.frames.wooden.RecipeBedFrameWood;

import javax.annotation.Nonnull;

public class WoodFrameHandler implements IRecipeHandler<RecipeBedFrameWood> {

    @Nonnull
    @Override
    public Class<RecipeBedFrameWood> getRecipeClass() {
        return RecipeBedFrameWood.class;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid() {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public String getRecipeCategoryUid(@Nonnull RecipeBedFrameWood recipeBedFrameWood) {
        return VanillaRecipeCategoryUid.CRAFTING;
    }

    @Nonnull
    @Override
    public IRecipeWrapper getRecipeWrapper(@Nonnull RecipeBedFrameWood recipeBedFrameWood) {
        return new WrapperWoodFrame();
    }

    @Override
    public boolean isRecipeValid(@Nonnull RecipeBedFrameWood recipeBedFrameWood) {
        return true;
    }
}
