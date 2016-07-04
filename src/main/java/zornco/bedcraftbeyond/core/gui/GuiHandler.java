package zornco.bedcraftbeyond.core.gui;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.IGuiHandler;
import zornco.bedcraftbeyond.dyes.eyedropper.GuiEyedropper;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.frames.registry.gui.list.GuiRegistryList;
import zornco.bedcraftbeyond.core.gui.container.ContainerBlank;
import zornco.bedcraftbeyond.storage.CapabilityStorageHandler;
import zornco.bedcraftbeyond.storage.IStorageHandler;
import zornco.bedcraftbeyond.storage.gui.ContainerStorage;
import zornco.bedcraftbeyond.storage.gui.GuiStorage;

public class GuiHandler implements IGuiHandler {

    public static GuiHandler INSTANCE;
    public static final int ID_EYEDROPPER = 1;
    public static final int ID_FRAMES_LIST = 2;
    public static final int ID_FRAMES_EDIT = 3;
    public static final int ID_STORAGE = 4;

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
        BlockPos pos = new BlockPos(x,y,z);
        switch(ID){
            case ID_EYEDROPPER:
                EnumHand hand = getHandFromCoord(x);
                return new GuiEyedropper(player, hand, player.getHeldItem(hand));

            case ID_STORAGE:
                if(!player.getEntityData().hasKey("storageID")) return null;
                if(!player.getEntityData().hasKey("side")) return null;

                String storageID = player.getEntityData().getString("storageID");
                EnumFacing side = EnumFacing.getFront(player.getEntityData().getInteger("side"));

                player.getEntityData().removeTag("side");
                player.getEntityData().removeTag("storageID");

                TileEntity entity = world.getTileEntity(pos);
                if(!entity.hasCapability(CapabilityStorageHandler.INSTANCE, side)) return null;

                IStorageHandler handler = (IStorageHandler) entity.getCapability((Capability) CapabilityStorageHandler.INSTANCE, side);
                if(!handler.isSlotFilled(storageID)) return null;

                return new ContainerStorage(player, handler, storageID);

            case ID_FRAMES_LIST:
                return new ContainerBlank();
        }

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x,y,z);
        switch(ID){
            case ID_STORAGE:
                if(!player.getEntityData().hasKey("storageID")) return null;
                if(!player.getEntityData().hasKey("side")) return null;

                String storageID = player.getEntityData().getString("storageID");
                EnumFacing side = EnumFacing.getFront(player.getEntityData().getInteger("side"));

                player.getEntityData().removeTag("side");
                player.getEntityData().removeTag("storageID");

                TileEntity entity = world.getTileEntity(pos);
                if(!entity.hasCapability(CapabilityStorageHandler.INSTANCE, side)) return null;

                IStorageHandler handler = (IStorageHandler) entity.getCapability((Capability) CapabilityStorageHandler.INSTANCE, side);
                if(!handler.isSlotFilled(storageID)) return null;

                return new GuiStorage(player, handler, storageID);

            case ID_FRAMES_LIST:
                FrameRegistry.EnumFrameType type = FrameRegistry.EnumFrameType.values()[x];
                return new GuiRegistryList(player, type);
        }

        return null;
    }
}
