package zornco.bedcraftbeyond.core;

import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.beds.base.TileGenericBed;
import zornco.bedcraftbeyond.beds.parts.drawer.DrawerPart;
import zornco.bedcraftbeyond.beds.parts.drawer.ItemDrawer;
import zornco.bedcraftbeyond.beds.parts.drawer.ItemDrawerKey;
import zornco.bedcraftbeyond.beds.parts.linens.impl.BlanketPart;
import zornco.bedcraftbeyond.beds.parts.linens.impl.ItemBlanket;
import zornco.bedcraftbeyond.beds.parts.linens.impl.ItemSheets;
import zornco.bedcraftbeyond.beds.parts.linens.impl.SheetPart;
import zornco.bedcraftbeyond.beds.stone.BlockStoneBed;
import zornco.bedcraftbeyond.beds.stone.ItemStoneBed;
import zornco.bedcraftbeyond.beds.wooden.BlockWoodenBed;
import zornco.bedcraftbeyond.beds.wooden.ItemWoodenFrame;
import zornco.bedcraftbeyond.beds.wooden.TileWoodenBed;
import zornco.bedcraftbeyond.dyes.bottle.ItemDyeBottle;
import zornco.bedcraftbeyond.dyes.eyedropper.ItemEyedropper;
import zornco.bedcraftbeyond.rugs.BlockRug;
import zornco.bedcraftbeyond.rugs.ItemRug;
import zornco.bedcraftbeyond.rugs.ItemScissors;
import zornco.bedcraftbeyond.suitcase.ItemSuitcase;

public class ModContent {

    public static class Blocks {

        public static BlockWoodenBed woodenBed = new zornco.bedcraftbeyond.beds.wooden.BlockWoodenBed();
        public static BlockStoneBed stoneBed = new zornco.bedcraftbeyond.beds.stone.BlockStoneBed();

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
