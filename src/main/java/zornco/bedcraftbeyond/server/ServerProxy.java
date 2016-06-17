package zornco.bedcraftbeyond.server;

import net.minecraft.world.World;
import zornco.bedcraftbeyond.common.CommonProxy;

public class ServerProxy extends CommonProxy {

    @Override
    public World getClientWorld() {
        return null;
    }
}
