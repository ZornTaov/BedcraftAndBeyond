package zornco.bedcraftbeyond.storage.handling;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class StoragePacketHandler {
    public static void openStorage(World world, EntityPlayer player, BlockPos tilePos, EnumFacing side, IStorageHandler handler, String id){
        if(world.isRemote){
            BedCraftBeyond.PROXY.openStorage(handler, tilePos, id);
            return;
        }

        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        Container remoteGuiContainer = handler.getSlotPart(id).createContainer(player, world.getTileEntity(tilePos), handler, id);

        if (remoteGuiContainer == null) return;
        playerMP.getNextWindowId();
        playerMP.closeContainer();
        int windowId = playerMP.currentWindowId;

        MessageOpenStorage openMessage = new MessageOpenStorage(player, tilePos, side, handler, id);
        BedCraftBeyond.NETWORK.sendTo(openMessage, playerMP);

        player.openContainer = remoteGuiContainer;
        player.openContainer.windowId = windowId;
        player.openContainer.addListener(playerMP);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(player, player.openContainer));
    }
}
