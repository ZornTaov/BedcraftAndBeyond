package zornco.bedcraftbeyond.compat.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.INbtIgnoreList;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.compat.jei.dye_bottle.DyeBottleHandler;

@JEIPlugin
public class JeiCompat extends BlankModPlugin {

    IModRegistry registry;

    @Override
    public void register(IModRegistry registry) {
        BedCraftBeyond.LOGGER.info("Registering JEI plugin.");
        this.registry = registry;
        registry.addRecipeHandlers(new DyeBottleHandler());
        INbtIgnoreList list = registry.getJeiHelpers().getNbtIgnoreList();
        list.ignoreNbtTagNames(BcbItems.eyedropper, "color");
        list.ignoreNbtTagNames(BcbItems.dyeBottle, "color");

        registry.addDescription(new ItemStack(BcbItems.dyeBottle), "Dye bottles are created by combining a water bottle,", "a red, green, and blue dye, and", "a colored eyedropper.");
    }
}
