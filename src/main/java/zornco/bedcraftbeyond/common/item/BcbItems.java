package zornco.bedcraftbeyond.common.item;

import net.minecraftforge.fml.common.registry.GameRegistry;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;

public class BcbItems {

  // Colored bed stuff
  public static ItemWoodenBed woodenBed;
  public static ItemBlanket blanket;
  public static ItemSheets sheets;
  public static ItemDrawer drawer;
  public static ItemDrawerKey drawerKey;

  // Other stuff
  public static ItemStoneBed stoneBed;
  public static ItemRug rug;
  public static ItemScissors scissors;

  private static void initItems(){
    woodenBed = new ItemWoodenBed(BcbBlocks.woodenBed);
    blanket = new ItemBlanket();
    sheets = new ItemSheets();
    drawer = new ItemDrawer();
    drawerKey = new ItemDrawerKey();

    stoneBed = new ItemStoneBed(BcbBlocks.stoneBed);
    rug = new ItemRug(BcbBlocks.rug);
    scissors = new ItemScissors();
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
  }

  public static void registerItems() {
    initItems(); addItems();
  }
}
