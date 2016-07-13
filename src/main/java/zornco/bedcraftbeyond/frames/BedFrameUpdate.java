package zornco.bedcraftbeyond.frames;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.frames.wooden.BlockWoodenBed;
import zornco.bedcraftbeyond.frames.wooden.TileWoodenBed;
import zornco.bedcraftbeyond.frames.registry.FrameException;

public class BedFrameUpdate implements IMessage {

    private BlockPos pos;
    private ResourceLocation registryName;
    private int metadata;

    @SuppressWarnings("unused")
    public BedFrameUpdate(){}

    public BedFrameUpdate(BlockPos pos, ResourceLocation regName, int meta){
        this.pos = pos;
        this.registryName = regName;
        this.metadata = meta;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.pos = BlockPos.fromLong(buf.readLong());
        this.registryName = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        this.metadata = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, registryName.toString());
        buf.writeInt(metadata);
    }

    public static class Handler implements IMessageHandler<BedFrameUpdate, IMessage> {

        public Handler(){ }

        @Override
        public IMessage onMessage(BedFrameUpdate message, MessageContext ctx) {
            World w = BedCraftBeyond.PROXY.getClientWorld();
            IBlockState curState = w.getBlockState(message.pos);
            TileWoodenBed twb = (TileWoodenBed) ((BlockWoodenBed) curState.getBlock()).getTileForBed(w, curState, message.pos);
            if(twb == null) return null;

            try {
                twb.setPlankType(message.registryName, message.metadata, false);
            } catch (FrameException e) {
                BedCraftBeyond.LOGGER.error("There was an error setting the plank type on the client.");
                BedCraftBeyond.LOGGER.error(e);
            }

            return null;
        }
    }
}
