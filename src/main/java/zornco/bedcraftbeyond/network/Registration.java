package zornco.bedcraftbeyond.network;

import net.minecraftforge.fml.relauncher.Side;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class Registration {

  public static void registerMessages(){
    BedCraftBeyond.network.registerMessage(ColoredBedUpdate.Handler.class, ColoredBedUpdate.class, 1, Side.CLIENT);
  }

}
