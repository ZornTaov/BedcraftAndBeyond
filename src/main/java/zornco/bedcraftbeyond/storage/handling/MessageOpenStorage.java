package zornco.bedcraftbeyond.storage.handling;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class MessageOpenStorage implements IMessage {

    // Only for getting params
    protected EntityPlayer player;
    protected IStorageHandler handler;

    // Serialized
    protected BlockPos tilePos;
    protected EnumFacing side;
    protected String storageID;
    protected int windowID;

    public MessageOpenStorage(){ }
    public MessageOpenStorage(EntityPlayer player, BlockPos tilePos, EnumFacing side, IStorageHandler handler, String id){
        this.player = player;
        this.tilePos = tilePos;
        this.side = side;
        this.handler = handler;
        this.storageID = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = BlockPos.fromLong(buf.readLong());
        storageID = ByteBufUtils.readUTF8String(buf);
        side = EnumFacing.getFront(buf.readInt());
        windowID = buf.readInt();

        World w = BedCraftBeyond.PROXY.getClientWorld();
        TileEntity te = w.getTileEntity(tilePos);
        if(te == null) return;
        if(!te.hasCapability(CapabilityStorageHandler.INSTANCE, side)) return;
        handler = te.getCapability(CapabilityStorageHandler.INSTANCE, side);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(tilePos.toLong());
        ByteBufUtils.writeUTF8String(buf, storageID);
        buf.writeInt(side.getIndex());
        buf.writeInt(((EntityPlayerMP) player).currentWindowId);
    }

    public static class Handler implements IMessageHandler<MessageOpenStorage, IMessage> {

        public Handler(){ }

        @Override
        public IMessage onMessage(MessageOpenStorage message, MessageContext ctx) {
            EntityPlayer player = BedCraftBeyond.PROXY.getPlayer();
            try {
                StoragePacketHandler.openStorage(player.worldObj, player, message.tilePos, message.side, message.handler, message.storageID);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player.openContainer.windowId = message.windowID;
            return null;
        }
    }
}
