package zornco.bedcraftbeyond.common.crafting.jei;

import mezz.jei.api.*;
import net.minecraft.item.ItemStack;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.crafting.jei.rug.RugDyeHandler;
import zornco.bedcraftbeyond.common.crafting.jei.stone_frame.StoneFrameHandler;
import zornco.bedcraftbeyond.common.item.BcbItems;
import zornco.bedcraftbeyond.common.crafting.jei.dye_bottle.DyeBottleHandler;
import zornco.bedcraftbeyond.common.crafting.jei.wood_frame.WoodFrameHandler;

@JEIPlugin
public class JeiCompat extends BlankModPlugin {

    IModRegistry registry;

    @Override
    public void register(IModRegistry registry) {
        this.registry = registry;

        registry.addRecipeHandlers(new DyeBottleHandler(),
            new WoodFrameHandler(),
            new StoneFrameHandler(),
            new RugDyeHandler());

        registry.addDescription(new ItemStack(BcbItems.dyeBottle), "Dye bottles are created by combining a water " +
            "bottle, a red, green, and blue dye, and a colored eyedropper.", "", "Use them to dye linen items to a specific color.");

        registry.addDescription(new ItemStack(BcbItems.woodenBed), "Wooden frames are the building blocks for wood beds. While the recipe shows " +
            "vanilla wood planks, any set of three wood planks that are whitelisted in the frame registry will work.");

        registry.addDescription(new ItemStack(BcbItems.stoneBed), "Stone beds are created by mixing stone slabs with any type of stone added " +
            "to the stone frame registry.");
    }
}
