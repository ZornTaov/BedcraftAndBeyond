package zornco.bedcraftbeyond.core.proxy;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.core.ModContent;

import java.awt.*;

public abstract class CommonProxy {

    public void registerModels() {
    }

    public void init() {
        ModContent.TileEntities.registerTiles();
    }

    public abstract World getClientWorld();

    public abstract Color getColorFromTexture(ItemStack stack);

    public abstract EntityPlayer getPlayer();
}
