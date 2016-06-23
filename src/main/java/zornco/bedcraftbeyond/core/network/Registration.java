package zornco.bedcraftbeyond.core.network;

import net.minecraftforge.fml.relauncher.Side;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.beds.frames.BedFrameUpdate;
import zornco.bedcraftbeyond.beds.parts.linens.BedLinenUpdate;

public class Registration {

    public static void registerMessages() {
        // Client
        BedCraftBeyond.NETWORK.registerMessage(BedLinenUpdate.Handler.class, BedLinenUpdate.class, 1, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(BedFrameUpdate.Handler.class, BedFrameUpdate.class, 2, Side.CLIENT);

        // Server
        BedCraftBeyond.NETWORK.registerMessage(zornco.bedcraftbeyond.dyes.eyedropper.MessageEyedropperUpdate.Handler.class, zornco.bedcraftbeyond.dyes.eyedropper.MessageEyedropperUpdate.class, 3, Side.SERVER);
    }
}
