package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
//import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

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
    public PlayerEntity getPlayer() {
        return null;
    }

    //@Override
    //public void openStorage(IStorageHandler handler, BlockPos pos, String id) { }
}
