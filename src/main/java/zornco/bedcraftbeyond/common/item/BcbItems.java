package zornco.bedcraftbeyond.common.item;

import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.item.frames.ItemStoneBed;
import zornco.bedcraftbeyond.common.item.frames.ItemWoodenFrame;
import zornco.bedcraftbeyond.common.item.linens.ItemBlanket;
import zornco.bedcraftbeyond.common.item.linens.ItemSheets;

public class BcbItems {

    // Bed stuff
    public static ItemWoodenFrame woodenBed = new ItemWoodenFrame(BcbBlocks.woodenBed);
    public static ItemBlanket blanket = new ItemBlanket();
    public static ItemSheets sheets = new ItemSheets();
    public static ItemDrawer drawer = new ItemDrawer();
    public static ItemDrawerKey drawerKey = new ItemDrawerKey();

    // Crafting
    public static ItemTemplate template = new ItemTemplate();

    // Other stuff
    public static ItemStoneBed stoneBed = new ItemStoneBed(BcbBlocks.stoneBed);
    public static ItemRug rug = new ItemRug();
    public static ItemScissors scissors = new ItemScissors();

    // ItemBlocks
    public static ItemDyeBottle dyeBottle = new ItemDyeBottle();

}
