package zornco.bedcraftbeyond.compat.jei;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import zornco.bedcraftbeyond.compat.jei.dye_bottle.DyeBottleHandler;

@JEIPlugin
public class JeiCompat extends BlankModPlugin {

    IModRegistry registry;

    @Override
    public void register(IModRegistry registry) {
        this.registry = registry;
        registry.addRecipeHandlers(new DyeBottleHandler());
    }
}
