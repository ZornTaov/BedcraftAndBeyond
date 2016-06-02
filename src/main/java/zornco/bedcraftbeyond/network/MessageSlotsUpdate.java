package zornco.bedcraftbeyond.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.common.blocks.tiles.TileCarpenter;

public class MessageSlotsUpdate implements IMessage {

    public BlockPos pos;
    public int slots = 0;

    public MessageSlotsUpdate(){}
    public MessageSlotsUpdate(BlockPos pos, int slots){
        this.pos = pos;
        this.slots = slots;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slots = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slots);
        buf.writeLong(pos.toLong());
    }

    public static class Handler implements IMessageHandler<MessageSlotsUpdate, IMessage> {

        @Override
        public IMessage onMessage(MessageSlotsUpdate message, MessageContext ctx) {
            World w = Minecraft.getMinecraft().theWorld;
            TileCarpenter tc = (TileCarpenter) w.getTileEntity(message.pos);
            tc.craftingInv.setSize(message.slots);
            return null;
        }
    }
}
