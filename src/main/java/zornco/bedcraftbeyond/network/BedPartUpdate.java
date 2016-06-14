package zornco.bedcraftbeyond.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;
import zornco.bedcraftbeyond.common.blocks.tiles.TileWoodenBed;

public class BedPartUpdate implements IMessage {

    private BlockPos pos;
    private BlockWoodenBed.EnumColoredPart part;
    private EnumBedFabricType partType;
    private ItemStack partItem;

    public BedPartUpdate() {
    }

    public BedPartUpdate(BlockPos pos, BlockWoodenBed.EnumColoredPart part, EnumBedFabricType partType) {
        this.pos = pos;
        this.part = part;
        this.partType = partType;
    }

    public BedPartUpdate(BlockPos pos, BlockWoodenBed.EnumColoredPart part, ItemStack partItem) {
        this.pos = pos;
        this.part = part;
        this.partType = EnumBedFabricType.SOLID_COLOR;
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
        part = BlockWoodenBed.EnumColoredPart.valueOf(ByteBufUtils.readUTF8String(buf));
        partType = EnumBedFabricType.valueOf(ByteBufUtils.readUTF8String(buf));

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

    public static class Handler implements IMessageHandler<BedPartUpdate, IMessage> {

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
        public IMessage onMessage(BedPartUpdate message, MessageContext ctx) {
            World w = Minecraft.getMinecraft().theWorld;
            IBlockState curState = w.getBlockState(message.pos);
            TileWoodenBed twb = (TileWoodenBed) w.getTileEntity(message.pos);
            switch (message.partType) {
                case SOLID_COLOR:
                    twb.setLinenPart(message.part, message.partItem);

                    break;

                default:
                    twb.setLinenPart(message.part, null);
            }

            IBlockState newState = w.getBlockState(message.pos).getActualState(w, message.pos);
            w.notifyBlockUpdate(message.pos, curState, newState, 2);
            return null;
        }
    }
}
