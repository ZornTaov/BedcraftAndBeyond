package zornco.bedcraftbeyond.network;

import net.minecraftforge.fml.relauncher.Side;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class Registration {

    public static void registerMessages() {
        // Client
        BedCraftBeyond.NETWORK.registerMessage(BedLinenUpdate.Handler.class, BedLinenUpdate.class, 1, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(BedFrameUpdate.Handler.class, BedFrameUpdate.class, 2, Side.CLIENT);

        // Server
        BedCraftBeyond.NETWORK.registerMessage(MessageEyedropperUpdate.Handler.class, MessageEyedropperUpdate.class, 3, Side.SERVER);
    }
}
