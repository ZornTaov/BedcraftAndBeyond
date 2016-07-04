package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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

    @Override
    public EntityPlayer getPlayer() {
        return null;
    }
}
