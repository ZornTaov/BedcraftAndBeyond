package zornco.bedcraftbeyond.network;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import zornco.bedcraftbeyond.BedCraftBeyond;

public class Registration {

    @SideOnly(Side.CLIENT)
    public static void registerMessagesClient() {
        BedCraftBeyond.NETWORK.registerMessage(BedPartUpdate.Handler.class, BedPartUpdate.class, 1, Side.CLIENT);
        BedCraftBeyond.NETWORK.registerMessage(MessageSlotsUpdate.Handler.class, MessageSlotsUpdate.class, 2, Side.CLIENT);
    }

    @SideOnly(Side.SERVER)
    public static void registerMessagesServer() {

    }

}
