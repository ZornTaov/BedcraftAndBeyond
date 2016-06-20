package zornco.bedcraftbeyond.common.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.common.blocks.tiles.TileGenericBed;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;

public class BcbBlocks {

    public static BlockWoodenBed woodenBed = new BlockWoodenBed();
    public static BlockStoneBed stoneBed = new BlockStoneBed();
    // public static BlockRainbowBed rainbowBed;
    public static BlockRug rug = new BlockRug();

    public static void registerTiles() {
        GameRegistry.registerTileEntity(TileGenericBed.class, "generic_bed");
        GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
    }
}
