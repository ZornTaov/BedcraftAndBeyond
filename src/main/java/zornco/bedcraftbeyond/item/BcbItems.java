package zornco.bedcraftbeyond.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import zornco.bedcraftbeyond.blocks.BcbBlocks;
import zornco.bedcraftbeyond.item.colored_bed.*;

public class BcbItems {

  // Colored bed stuff
  public static ItemColoredBed coloredBed;
  public static ItemBlanket blanket;
  public static ItemSheets sheets;
  public static ItemDrawer drawer;
  public static ItemDrawerKey drawerKey;

  // Other stuff
  public static ItemStoneBed stoneBed;
  public static ItemRug rug;
  public static ItemScissors scissors;

  private static void initItems(){
    coloredBed = new ItemColoredBed();
    blanket = new ItemBlanket();
    sheets = new ItemSheets();
    drawer = new ItemDrawer();
    drawerKey = new ItemDrawerKey();

    stoneBed = new ItemStoneBed();
    rug = new ItemRug(BcbBlocks.rug);
    scissors = new ItemScissors();
  }

  private static void addItems(){

  }

  public static void registerItems() {
    initItems(); addItems();
  }
}
