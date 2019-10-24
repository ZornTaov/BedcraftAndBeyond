package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
//import zornco.bedcraftbeyond.storage.handling.IStorageHandler;

import java.awt.*;

public abstract class CommonProxy {

    public void registerModels() {
    }

    public void init() {
    }

    public abstract World getClientWorld();

    public abstract Color getColorFromTexture(ItemStack stack);

    public abstract PlayerEntity getPlayer();

    //public abstract void openStorage(IStorageHandler handler, BlockPos tilePos, String id);
}
