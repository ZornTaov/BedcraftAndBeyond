package zornco.bedcraftbeyond.blocks;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class BcbBlocks {

  public static BlockColoredBed coloredBed;
  public static BlockStoneBed stoneBed;
  // public static BlockRainbowBed rainbowBed;
  public static BlockRug rug;

  private static void initBlocks(){
    coloredBed = new BlockColoredBed();
    stoneBed = new BlockStoneBed();
    rug = new BlockRug();
  }

  private static void addBlocks(){
    GameRegistry.register(coloredBed);
    GameRegistry.register(stoneBed);
    GameRegistry.register(rug);
  }

  public static void registerBlocks(){
    initBlocks();
    addBlocks();
  }
}
