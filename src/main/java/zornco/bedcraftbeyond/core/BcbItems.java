package zornco.bedcraftbeyond.core;

import zornco.bedcraftbeyond.beds.parts.drawer.ItemDrawerKey;
import zornco.bedcraftbeyond.beds.parts.linens.ItemBlanket;
import zornco.bedcraftbeyond.beds.parts.linens.ItemSheets;
import zornco.bedcraftbeyond.beds.stone.ItemStoneBed;
import zornco.bedcraftbeyond.beds.wooden.ItemWoodenFrame;
import zornco.bedcraftbeyond.dyes.bottle.ItemDyeBottle;
import zornco.bedcraftbeyond.dyes.eyedropper.ItemEyedropper;
import zornco.bedcraftbeyond.rugs.ItemScissors;
import zornco.bedcraftbeyond.rugs.ItemRug;

public class BcbItems {

    // Bed stuff
    public static ItemWoodenFrame woodenBed = new ItemWoodenFrame(BcbBlocks.woodenBed);
    public static ItemBlanket blanket = new ItemBlanket();
    public static ItemSheets sheets = new ItemSheets();
    public static zornco.bedcraftbeyond.beds.parts.drawer.ItemDrawer drawer = new zornco.bedcraftbeyond.beds.parts.drawer.ItemDrawer();
    public static zornco.bedcraftbeyond.beds.parts.drawer.ItemDrawerKey drawerKey = new ItemDrawerKey();

    // Other stuff
    public static ItemStoneBed stoneBed = new ItemStoneBed(BcbBlocks.stoneBed);
    public static ItemRug rug = new ItemRug();
    public static ItemScissors scissors = new ItemScissors();

    // Dying
    public static ItemDyeBottle dyeBottle = new ItemDyeBottle();
    public static ItemEyedropper eyedropper = new ItemEyedropper();

}
