package zornco.bedcraftbeyond.storage.handling;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

public class MessageStorageUpdate implements IMessage {

    protected BlockPos pos;
    protected String slot;
    protected EnumFacing side;
    protected ItemStack stack;

    public MessageStorageUpdate(){ }
    public MessageStorageUpdate(BlockPos pos, String slot, EnumFacing side, ItemStack stack){
        this.pos = pos;
        this.slot = slot;
        this.side = side;
        this.stack = stack;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = BlockPos.fromLong(buf.readLong());
        slot = ByteBufUtils.readUTF8String(buf);
        int sidei = buf.readInt();
        this.side = sidei != -99 ? EnumFacing.getFront(sidei) : null;
        stack = ByteBufUtils.readItemStack(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, slot);
        buf.writeInt(side != null ? side.getIndex() : -99);
        ByteBufUtils.writeItemStack(buf, stack);
    }

    public static class Handler implements IMessageHandler<MessageStorageUpdate, IMessage> {
        public Handler(){ }

        @Override
        public IMessage onMessage(MessageStorageUpdate message, MessageContext ctx) {
            World w = BedCraftBeyond.PROXY.getClientWorld();
            TileEntity te = w.getTileEntity(message.pos);
            if(!(te.hasCapability(CapabilityStorageHandler.INSTANCE, message.side))) return null;

            IStorageHandler handler = (IStorageHandler) te.getCapability((Capability) CapabilityStorageHandler.INSTANCE, message.side);
            handler.setSlotItem(message.slot, message.stack);

            return null;
        }
    }
}
