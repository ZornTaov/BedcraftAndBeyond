package zornco.bedcraftbeyond.core;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.frames.base.TileGenericBed;
import zornco.bedcraftbeyond.storage.drawers.standard.ItemDrawer;
import zornco.bedcraftbeyond.storage.ItemDrawerKey;
import zornco.bedcraftbeyond.linens.impl.ItemColoredBlanket;
import zornco.bedcraftbeyond.linens.impl.ItemColoredSheets;
import zornco.bedcraftbeyond.frames.stone.BlockStoneBed;
import zornco.bedcraftbeyond.frames.stone.ItemStoneBed;
import zornco.bedcraftbeyond.frames.wooden.BlockWoodenBed;
import zornco.bedcraftbeyond.frames.wooden.ItemWoodenFrame;
import zornco.bedcraftbeyond.frames.wooden.TileWoodenBed;
import zornco.bedcraftbeyond.dyes.bottle.ItemDyeBottle;
import zornco.bedcraftbeyond.dyes.eyedropper.ItemEyedropper;
import zornco.bedcraftbeyond.rugs.BlockRug;
import zornco.bedcraftbeyond.rugs.ItemRug;
import zornco.bedcraftbeyond.rugs.ItemScissors;
import zornco.bedcraftbeyond.suitcase.ItemSuitcase;
import zornco.bedcraftbeyond.storage.drawers.reinforced.ItemReinforcedDrawer;

public class ModContent {

    public static class Blocks {

        public static BlockWoodenBed woodenBed = new zornco.bedcraftbeyond.frames.wooden.BlockWoodenBed();
        public static BlockStoneBed stoneBed = new zornco.bedcraftbeyond.frames.stone.BlockStoneBed();

        public static BlockRug rug = new BlockRug();
    }

    public static class Items {

        // Bed stuff
        public static ItemWoodenFrame woodenBed = new ItemWoodenFrame(Blocks.woodenBed);
        public static ItemDrawerKey drawerKey = new ItemDrawerKey();

        // Other stuff
        public static ItemStoneBed stoneBed = new ItemStoneBed(Blocks.stoneBed);
        public static ItemRug rug = new ItemRug();
        public static ItemScissors scissors = new ItemScissors();
        
        // Suitcase stuff
        public static ItemSuitcase suitcase = new ItemSuitcase();

        // Dying
        public static ItemDyeBottle dyeBottle = new ItemDyeBottle();
        public static ItemEyedropper eyedropper = new ItemEyedropper();

        // Storage
        public static ItemDrawer drawer = new ItemDrawer();
        public static ItemReinforcedDrawer reinforcedDrawer = new ItemReinforcedDrawer();

        // Linens - Handled by the parts
        public static ItemColoredBlanket blanket = new ItemColoredBlanket();
        public static ItemColoredSheets sheets = new ItemColoredSheets();
    }

    public static class TileEntities {
        public static void registerTiles() {
            GameRegistry.registerTileEntity(TileGenericBed.class, "generic_bed");
            GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
        }
    }

}
