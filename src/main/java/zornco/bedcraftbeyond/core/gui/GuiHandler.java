package zornco.bedcraftbeyond.core.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zornco.bedcraftbeyond.dyes.eyedropper.GuiEyedropper;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.frames.registry.gui.list.GuiRegistryList;
import zornco.bedcraftbeyond.core.gui.container.ContainerBlank;
import zornco.bedcraftbeyond.suitcase.*;

public class GuiHandler implements IGuiHandler {

    public static GuiHandler INSTANCE;
    public static final int ID_EYEDROPPER = 1;
    public static final int ID_FRAMES_LIST = 2;
    public static final int ID_FRAMES_EDIT = 3;
    public static final int GUI_SUITCASE = 4;

    public static int getCoordinateFromHand(EnumHand hand){
        switch (hand){
            case MAIN_HAND:
                return 1;
            case OFF_HAND:
                return 0;
        }

        return -1;
    }

    private EnumHand getHandFromCoord(int coord){
        switch (coord){
            case 0:
                return EnumHand.OFF_HAND;
            case 1:
                return EnumHand.MAIN_HAND;
            default:
                return EnumHand.MAIN_HAND;
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch(ID){
            case ID_EYEDROPPER:
                EnumHand hand = getHandFromCoord(pos.getX());
                return null;

            case ID_FRAMES_LIST:
                return new ContainerBlank();
	        case GUI_SUITCASE:
				return new ContainerSuitcase(player, player.inventory, new InventorySuitcase(player.getActiveItemStack()));
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        switch(ID){
            case ID_EYEDROPPER:
                return new GuiEyedropper(player, getHandFromCoord(x), player.getHeldItem(getHandFromCoord(pos.getX())));

            case ID_FRAMES_LIST:
                FrameRegistry.EnumFrameType type = FrameRegistry.EnumFrameType.values()[x];
                return new GuiRegistryList(player, type);
    		case GUI_SUITCASE:
    			return new GuiSuitcase((ContainerSuitcase) new ContainerSuitcase(player, player.inventory, new InventorySuitcase(player.getActiveItemStack())));
        }

        return null;
    }
}
