package zornco.bedcraftbeyond.storage;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.gui.GuiHandler;

public class MessageOpenStorage implements IMessage {

    protected BlockPos pos;
    protected int storageID;

    public MessageOpenStorage(){ }
    public MessageOpenStorage(BlockPos pos, int id){
        this.pos = pos;
        this.storageID = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        storageID = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        buf.writeInt(storageID);
    }

    public static class Handler implements IMessageHandler<MessageOpenStorage, IMessage> {

        public Handler(){ }

        @Override
        public IMessage onMessage(MessageOpenStorage message, MessageContext ctx) {
            EntityPlayer player = BedCraftBeyond.PROXY.getPlayer();
            player.getEntityData().setInteger("storageID", message.storageID);

            BlockPos pos = message.pos;
            player.openGui(BedCraftBeyond.INSTANCE, GuiHandler.ID_STORAGE, BedCraftBeyond.PROXY.getClientWorld(), pos.getX(), pos.getY(), pos.getZ());
            return null;
        }
    }
}
