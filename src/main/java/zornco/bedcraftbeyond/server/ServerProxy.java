package zornco.bedcraftbeyond.server;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.common.CommonProxy;

import java.awt.*;

public class ServerProxy extends CommonProxy {

    @Override
    public World getClientWorld() {
        return null;
    }

    @Override
    public Color getColorFromTexture(ItemStack stack) {
        return Color.WHITE;
    }
}
