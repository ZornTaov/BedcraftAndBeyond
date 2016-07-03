package zornco.bedcraftbeyond.core;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.frames.base.TileGenericBed;
import zornco.bedcraftbeyond.storage.drawer.DrawerPart;
import zornco.bedcraftbeyond.storage.drawer.ItemDrawer;
import zornco.bedcraftbeyond.storage.drawer.ItemDrawerKey;
import zornco.bedcraftbeyond.linens.impl.BlanketPart;
import zornco.bedcraftbeyond.linens.impl.ItemBlanket;
import zornco.bedcraftbeyond.linens.impl.ItemSheets;
import zornco.bedcraftbeyond.linens.impl.SheetPart;
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

        // Dying
        public static ItemDyeBottle dyeBottle = new ItemDyeBottle();
        public static ItemEyedropper eyedropper = new ItemEyedropper();

        // Bed Parts - Do not initialize here, it's done with BedParts
        public static ItemDrawer drawer;
        public static ItemBlanket blanket;
        public static ItemSheets sheets;
    }

    public static class BedParts {
        // These also register their blocks and items.
        public static DrawerPart drawer = new DrawerPart();
        public static BlanketPart blanket = new BlanketPart();
        public static SheetPart sheet = new SheetPart();
    }

    public static class TileEntities {
        public static void registerTiles() {
            GameRegistry.registerTileEntity(TileGenericBed.class, "generic_bed");
            GameRegistry.registerTileEntity(TileWoodenBed.class, "wooden_bed");
        }
    }

}
