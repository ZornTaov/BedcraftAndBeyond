package zornco.bedcraftbeyond.common;

import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.common.blocks.BcbBlocks;

import java.awt.*;

public abstract class CommonProxy {

    public void registerModels() {
    }

    public void init() {
        BcbBlocks.registerTiles();
    }

    public abstract World getClientWorld();

    public abstract Color getColorFromTexture(ItemStack stack);
}
