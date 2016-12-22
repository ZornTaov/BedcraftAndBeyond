package zornco.bedcraftbeyond.linens;

import com.sun.deploy.uitoolkit.impl.fx.ui.FXAppContext;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.frames.base.BlockBedBase;
import zornco.bedcraftbeyond.linens.cap.CapabilityLinenHandler;
import zornco.bedcraftbeyond.linens.cap.ILinenHandler;

public class LinenUpdate implements IMessage {

    private BlockPos pos;
    private LinenType type;
    private ItemStack stack;

    @SuppressWarnings("unused")
    public LinenUpdate() {}

    public LinenUpdate(BlockPos pos, LinenType type, ItemStack newStack) {
        this.pos = pos;
        this.type = type;
        this.stack = newStack;
    }

    /**
     * Convert from the supplied buffer into your specific message type
     *
     * @param buf
     */
    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.type =  LinenType.valueOf(ByteBufUtils.readUTF8String(buf));
        this.stack = ByteBufUtils.readItemStack(buf);
    }

    /**
     * Deconstruct your message into the supplied byte buffer
     *
     * @param buf
     */
    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, this.type.name());
        ByteBufUtils.writeItemStack(buf, stack);
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
            if(tile == null || !(tile.hasCapability(CapabilityLinenHandler.INSTANCE, EnumFacing.UP))) return null;

            ILinenHandler linens = tile.getCapability(CapabilityLinenHandler.INSTANCE, EnumFacing.UP);
            linens.setSlotItem(message.type, message.stack);

            tile.markDirty();
            w.markBlockRangeForRenderUpdate(message.pos, message.pos.offset(curState.getValue(BlockBedBase.FACING)));
            return null;
        }
    }
}
