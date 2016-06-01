package zornco.bedcraftbeyond.common.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zornco.bedcraftbeyond.client.gui.IClientGui;
import zornco.bedcraftbeyond.server.gui.IServerGui;

public class GuiHandler implements IGuiHandler {

    public static GuiHandler INSTANCE;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos bp = new BlockPos(x,y,z);
        IBlockState state = world.getBlockState(bp);
        if(state.getBlock() instanceof IServerGui){
            return ((IServerGui) state.getBlock()).getServerGui(world, bp, player);
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos bp = new BlockPos(x,y,z);
        IBlockState state = world.getBlockState(bp);
        if(state.getBlock() instanceof IClientGui){
            return ((IClientGui) state.getBlock()).getClientGui(world, bp, player);
        }

        return null;
    }
}
