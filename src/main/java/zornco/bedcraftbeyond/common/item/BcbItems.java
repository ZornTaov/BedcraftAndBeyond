package zornco.bedcraftbeyond.common.item;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;
import zornco.bedcraftbeyond.common.item.frames.ItemStoneBed;
import zornco.bedcraftbeyond.common.item.frames.ItemWoodenFrame;
import zornco.bedcraftbeyond.common.item.linens.ItemBlanket;
import zornco.bedcraftbeyond.common.item.linens.ItemSheets;

public class BcbItems {

  // Colored bed stuff
  public static ItemWoodenFrame woodenBed;
  public static ItemBlanket blanket;
  public static ItemSheets sheets;
  public static ItemDrawer drawer;
  public static ItemDrawerKey drawerKey;

  // Other stuff
  public static ItemStoneBed stoneBed;
  public static ItemRug rug;
  public static ItemScissors scissors;

  // ItemBlocks
  public static ItemDyeBottle dyeBottle;

  private static void initItems(){
    woodenBed = new ItemWoodenFrame(BcbBlocks.woodenBed);
    blanket = new ItemBlanket();
    sheets = new ItemSheets();
    drawer = new ItemDrawer();
    drawerKey = new ItemDrawerKey();

    stoneBed = new ItemStoneBed(BcbBlocks.stoneBed);
    rug = new ItemRug(BcbBlocks.rug);
    scissors = new ItemScissors();
    dyeBottle = new ItemDyeBottle();
  }

  private static void addItems(){
    GameRegistry.register(woodenBed);
    GameRegistry.register(blanket);
    GameRegistry.register(sheets);
    GameRegistry.register(drawer);
    GameRegistry.register(drawerKey);

    GameRegistry.register(stoneBed);
    GameRegistry.register(rug);
    GameRegistry.register(scissors);

    GameRegistry.register(dyeBottle);
  }

  public static void registerItems() {
    initItems(); addItems();
  }
}
