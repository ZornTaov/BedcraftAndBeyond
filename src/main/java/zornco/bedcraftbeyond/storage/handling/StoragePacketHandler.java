package zornco.bedcraftbeyond.storage.handling;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.frames.wooden.TileWoodenBed;
import zornco.bedcraftbeyond.linens.LinenUpdate;

public class StoragePacketHandler {
    public static void openStorage(World world, EntityPlayer player, BlockPos tilePos, EnumFacing side, IStorageHandler handler, String id) throws Exception {
        if(world.isRemote){
            BedCraftBeyond.PROXY.openStorage(handler, tilePos, id);
            return;
        }

        EntityPlayerMP playerMP = (EntityPlayerMP) player;
        Container remoteGuiContainer = handler.getSlotItem(id).createContainer(player, world.getTileEntity(tilePos), handler, id);

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
