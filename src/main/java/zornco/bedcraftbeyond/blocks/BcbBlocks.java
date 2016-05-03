package zornco.bedcraftbeyond.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class BcbBlocks {

  public static BlockWoodenBed woodenBed;
  public static BlockStoneBed stoneBed;
  // public static BlockRainbowBed rainbowBed;
  public static BlockRug rug;

  private static void initBlocks(){
    woodenBed = new BlockWoodenBed();
    stoneBed = new BlockStoneBed();
    rug = new BlockRug();
  }

  private static void addBlocks(){
    GameRegistry.register(woodenBed);
    GameRegistry.register(stoneBed);
    GameRegistry.register(rug);
  }

  public static void registerBlocks(){
    initBlocks();
    addBlocks();
  }
}
