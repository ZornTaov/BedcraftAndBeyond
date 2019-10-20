package zornco.bedcraftbeyond.core;

public class ModContent {

    public static class Blocks {

        // public static BlockWoodenBed woodenBed = new
        // zornco.bedcraftbeyond.frames.wooden.BlockWoodenBed();
        // public static BlockStoneBed stoneBed = new
        // zornco.bedcraftbeyond.frames.stone.BlockStoneBed();
        //
        // public static BlockRug rug = new BlockRug();
    }

    public static class BedParts {
        // public static DrawerPart drawer = new DrawerPart();
        // public static BlanketPart blanket = new BlanketPart();
        // public static SheetPart sheet = new SheetPart();
        // public static ReinforcedDrawerPart drawerReinforced = new
        // ReinforcedDrawerPart();
    }

    public static class TileEntities {
        public static void registerTiles() {
            // GameRegistry.registerTileEntity(TileGenericBed.class, "generic_bed");
            // GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
        }
    }

    // You can use EventBusSubscriber to automatically subscribe events on the
    // contained class (this is subscribing to the MOD
    // Event bus for receiving Registry Events)
    // @Mod.EventBusSubscriber(modid = BedCraftBeyond.MOD_ID, bus =
    // Mod.EventBusSubscriber.Bus.MOD)
    // public static class RegistryEvents {
    // @SubscribeEvent
    // public static void onBlocksRegistry(final RegistryEvent.Register<Block>
    // blockRegistryEvent) {
    // // register a new block here
    // BedCraftBeyond.LOGGER.info("HELLO from Register Block");
    // }
    //
    // }
}
