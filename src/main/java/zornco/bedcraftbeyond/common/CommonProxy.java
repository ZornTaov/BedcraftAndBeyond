package zornco.bedcraftbeyond.common;

import net.minecraft.world.World;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;

public abstract class CommonProxy {

    public void registerModels() {
    }

    public void init() {
        BcbBlocks.registerTiles();
    }

    public abstract World getClientWorld();
}
