package zornco.bedcraftbeyond.network;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class Registration {

    public static void registerMessages() {
        // Client
        BedCraftBeyond.NETWORK.registerMessage(BedPartUpdate.Handler.class, BedPartUpdate.class, 1, Side.CLIENT);

        // Server
        BedCraftBeyond.NETWORK.registerMessage(MessageEyedropperUpdate.Handler.class, MessageEyedropperUpdate.class, 3, Side.SERVER);
    }
}
