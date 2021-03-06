package zornco.bedcraftbeyond.linens;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.parts.Part;

public class LinenUpdate implements IMessage {

    private BlockPos pos;
    private Part.Type part;
    private ItemStack partItem;

    @SuppressWarnings("unused")
    public LinenUpdate() {}

    public LinenUpdate(BlockPos pos, Part.Type part, ItemStack partItem) {
        this.pos = pos;
        this.part = part;
        this.partItem = partItem;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        pos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
        part = Part.Type.valueOf(ByteBufUtils.readUTF8String(buf));
        partItem = ByteBufUtils.readItemStack(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(pos));
        ByteBufUtils.writeUTF8String(buf, part.name());
        ByteBufUtils.writeItemStack(buf, partItem);
    }

    public static class Handler implements IMessageHandler<LinenUpdate, IMessage> {

        public Handler() {
        }

        /**
         * Called when a message is received of the appropriate type. You can optionally return a reply message, or null if no reply
         * is needed.
         *
         * @param message The message
         * @param ctx
         * @return an optional return message
         */
        @Override
        public IMessage onMessage(LinenUpdate message, MessageContext ctx) {
            World w = BedCraftBeyond.PROXY.getClientWorld();
            IBlockState curState = w.getBlockState(message.pos);
            TileEntity tile = w.getTileEntity(message.pos);
            if(tile == null || !(tile instanceof ILinenHolder)) return null;

            ((ILinenHolder) tile).getLinenHandler().setLinenPart(message.part, message.partItem);

            IBlockState newState = w.getBlockState(message.pos).getActualState(w, message.pos);
            w.notifyBlockUpdate(message.pos, curState, newState, 2);
            return null;
        }
    }
}
