package zornco.bedcraftbeyond.core;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.beds.wooden.*;
import zornco.bedcraftbeyond.beds.base.TileGenericBed;
import zornco.bedcraftbeyond.rugs.BlockRug;

public class BcbBlocks {

    public static zornco.bedcraftbeyond.beds.wooden.BlockWoodenBed woodenBed = new zornco.bedcraftbeyond.beds.wooden.BlockWoodenBed();
    public static zornco.bedcraftbeyond.beds.stone.BlockStoneBed stoneBed = new zornco.bedcraftbeyond.beds.stone.BlockStoneBed();
    // public static BlockRainbowBed rainbowBed;
    public static BlockRug rug = new BlockRug();

    public static void registerTiles() {
        GameRegistry.registerTileEntity(TileGenericBed.class, "generic_bed");
        GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
    }
}
