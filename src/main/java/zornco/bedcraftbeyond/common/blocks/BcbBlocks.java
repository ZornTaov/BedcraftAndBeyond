package zornco.bedcraftbeyond.common.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class BcbBlocks {

  public static BlockWoodenBed woodenBed;
  public static BlockStoneBed stoneBed;
  // public static BlockRainbowBed rainbowBed;
  public static BlockRug rug;
  public static BlockBedWorkbench workbench;

  private static void initBlocks(){
    woodenBed = new BlockWoodenBed();
    stoneBed = new BlockStoneBed();
    rug = new BlockRug();
    workbench = new BlockBedWorkbench();
  }

  private static void addBlocks(){
    GameRegistry.register(woodenBed);
    GameRegistry.register(stoneBed);
    GameRegistry.register(rug);
    GameRegistry.register(workbench);
  }

  public static void registerBlocks(){
    initBlocks();
    addBlocks();
  }
}
