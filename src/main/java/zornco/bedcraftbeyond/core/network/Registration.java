package zornco.bedcraftbeyond.core.network;

import net.minecraftforge.fml.relauncher.Side;
import zornco.bedcraftbeyond.frames.registry.messages.MessageOpenWhitelistEditor;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.frames.BedFrameUpdate;
import zornco.bedcraftbeyond.linens.LinenUpdate;
import zornco.bedcraftbeyond.dyes.eyedropper.MessageEyedropperUpdate;
import zornco.bedcraftbeyond.storage.MessageOpenStorage;
import zornco.bedcraftbeyond.storage.MessageStorageUpdate;

public class Registration {

    public static void registerMessages() {
        // Client
        BedCraftBeyond.NETWORK.registerMessage(LinenUpdate.Handler.class, LinenUpdate.class, 1, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(BedFrameUpdate.Handler.class, BedFrameUpdate.class, 2, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(MessageOpenStorage.Handler.class, MessageOpenStorage.class, 5, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(MessageStorageUpdate.Handler.class, MessageStorageUpdate.class, 6, Side.CLIENT);

        // Server
        BedCraftBeyond.NETWORK.registerMessage(MessageEyedropperUpdate.Handler.class, MessageEyedropperUpdate.class, 3, Side.SERVER);
        BedCraftBeyond.NETWORK.registerMessage(MessageOpenWhitelistEditor.Handler.class, MessageOpenWhitelistEditor.class, 4, Side.SERVER);
    }
}
