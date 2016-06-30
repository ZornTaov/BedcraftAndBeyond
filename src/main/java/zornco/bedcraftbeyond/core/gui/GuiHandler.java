package zornco.bedcraftbeyond.core.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.gui.list.GuiRegistryList;
import zornco.bedcraftbeyond.core.gui.container.ContainerBlank;

public class GuiHandler implements IGuiHandler {

    public static GuiHandler INSTANCE;
    public static final int BLOCK_ID = 0;

    public static final int ID_FRAMES_LIST = 2;
    public static final int ID_FRAMES_EDIT = 3;

    /**
     * If this is set and you want to handle main hand, send X = 1.
     */
    public static final int ITEM_ID = 1;

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case BLOCK_ID:
                BlockPos bp = new BlockPos(x,y,z);
                IBlockState state = world.getBlockState(bp);
                if(state.getBlock() instanceof IServerGui)
                    return ((IServerGui) state.getBlock()).getServerGui(world, bp, player);
                return null;
            case ITEM_ID:
                ItemStack held = player.getHeldItem(x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                if(held == null) return null;
                if(!(held.getItem() instanceof IServerGui)) return null;
                return ((IServerGui) held.getItem()).getServerGui(world, new BlockPos(x, y, z), player);
            case ID_FRAMES_LIST:
                return new ContainerBlank();
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch(ID){
            case BLOCK_ID:
                BlockPos bp = new BlockPos(x,y,z);
                IBlockState state = world.getBlockState(bp);
                if(!(state.getBlock() instanceof IClientGui)) return null;
                return ((IClientGui) state.getBlock()).getClientGui(world, bp, player);
            case ITEM_ID:
                ItemStack held = player.getHeldItem(x == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND);
                if(held == null) return null;
                if(!(held.getItem() instanceof IClientGui)) return null;
                return ((IClientGui) held.getItem()).getClientGui(world, new BlockPos(x, y, z), player);

            case ID_FRAMES_LIST:
                FrameRegistry.EnumFrameType type = FrameRegistry.EnumFrameType.values()[x];
                return new GuiRegistryList(player, type);
        }

        return null;
    }
}
