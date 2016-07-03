package zornco.bedcraftbeyond.core.network;

import net.minecraftforge.fml.relauncher.Side;
import zornco.bedcraftbeyond.frames.registry.messages.MessageOpenWhitelistEditor;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.frames.BedFrameUpdate;
import zornco.bedcraftbeyond.linens.BedLinenUpdate;
import zornco.bedcraftbeyond.dyes.eyedropper.MessageEyedropperUpdate;

public class Registration {

    public static void registerMessages() {
        // Client
        BedCraftBeyond.NETWORK.registerMessage(BedLinenUpdate.Handler.class, BedLinenUpdate.class, 1, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(BedFrameUpdate.Handler.class, BedFrameUpdate.class, 2, Side.CLIENT);

        // Server
        BedCraftBeyond.NETWORK.registerMessage(MessageEyedropperUpdate.Handler.class, MessageEyedropperUpdate.class, 3, Side.SERVER);
        BedCraftBeyond.NETWORK.registerMessage(MessageOpenWhitelistEditor.Handler.class, MessageOpenWhitelistEditor.class, 4, Side.SERVER);
    }
}
