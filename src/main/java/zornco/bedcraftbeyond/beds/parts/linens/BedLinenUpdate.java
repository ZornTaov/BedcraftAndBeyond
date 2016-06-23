package zornco.bedcraftbeyond.beds.parts.linens;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.beds.wooden.BlockWoodenBed;
import zornco.bedcraftbeyond.beds.wooden.TileWoodenBed;

public class BedLinenUpdate implements IMessage {

    private BlockPos pos;
    private LinenType part;
    private PropertyFabricType partType;
    private ItemStack partItem;

    @SuppressWarnings("unused")
    public BedLinenUpdate() {}

    public BedLinenUpdate(BlockPos pos, LinenType part, ItemStack partItem) {
        this.pos = pos;
        this.part = part;
        this.partType = PropertyFabricType.SOLID_COLOR;
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
        part = LinenType.valueOf(ByteBufUtils.readUTF8String(buf));
        partType = PropertyFabricType.valueOf(ByteBufUtils.readUTF8String(buf));

        switch (partType) {
            case SOLID_COLOR:
                partItem = ByteBufUtils.readItemStack(buf);
                break;

            case RAINBOW:
            case TEXTURED:

                break;

            case NONE:

                break;
        }

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
        ByteBufUtils.writeUTF8String(buf, partType.name());

        switch (partType) {
            case SOLID_COLOR:
                ByteBufUtils.writeItemStack(buf, partItem);
                break;

            default:
                break;
        }
    }

    public static class Handler implements IMessageHandler<BedLinenUpdate, IMessage> {

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
        public IMessage onMessage(BedLinenUpdate message, MessageContext ctx) {
            World w = BedCraftBeyond.PROXY.getClientWorld();
            IBlockState curState = w.getBlockState(message.pos);
            TileWoodenBed twb = (TileWoodenBed) w.getTileEntity(message.pos);
            if(twb == null) return null;

            LinenHandler handler = twb.getLinenHandler();
            switch (message.partType) {
                case SOLID_COLOR:
                    handler.setLinenPart(message.part, message.partItem);
                    break;

                default:
                    handler.setLinenPart(message.part, null);
            }

            IBlockState newState = w.getBlockState(message.pos).getActualState(w, message.pos);
            w.notifyBlockUpdate(message.pos, curState, newState, 2);
            return null;
        }
    }
}
