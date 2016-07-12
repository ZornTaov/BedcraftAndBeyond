package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

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

    @Override
    public void openStorage(IStorageHandler handler, BlockPos pos, String id) { }
}
