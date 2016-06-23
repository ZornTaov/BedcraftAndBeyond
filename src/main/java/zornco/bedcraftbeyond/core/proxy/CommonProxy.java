package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.core.BcbBlocks;

import java.awt.*;

public abstract class CommonProxy {

    public void registerModels() {
    }

    public void init() {
        BcbBlocks.registerTiles();
    }

    public abstract World getClientWorld();

    public abstract Color getColorFromTexture(ItemStack stack);
}
