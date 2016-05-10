package zornco.bedcraftbeyond.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.common.blocks.BlockWoodenBed;
import zornco.bedcraftbeyond.common.blocks.properties.EnumBedFabricType;

public class BedPartUpdate implements IMessage {

  private BlockPos pos;
  private BlockWoodenBed.EnumColoredPart part;
  private EnumBedFabricType color;
  private int partColor;

  public BedPartUpdate(){  }
  public BedPartUpdate(BlockPos pos, BlockWoodenBed.EnumColoredPart part, EnumBedFabricType color){
    this(pos, part, color, Integer.MIN_VALUE);
  }

  public BedPartUpdate(BlockPos pos, BlockWoodenBed.EnumColoredPart part, EnumBedFabricType color, int partColor){
    this.pos = pos;
    this.part = part;
    this.color = color;
    this.partColor = partColor;
  }

  /**
   * Convert from the supplied buffer into your specific message type
   *
   * @param buf
   */
  @Override
  public void fromBytes(ByteBuf buf) {
    pos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
    color = EnumBedFabricType.valueOf(ByteBufUtils.readUTF8String(buf));
    part = BlockWoodenBed.EnumColoredPart.valueOf(ByteBufUtils.readUTF8String(buf));
    partColor = buf.readInt();
  }

  /**
   * Deconstruct your message into the supplied byte buffer
   *
   * @param buf
   */
  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(pos));
    ByteBufUtils.writeUTF8String(buf, color.name());
    ByteBufUtils.writeUTF8String(buf, part.name());
    buf.writeInt(partColor);
  }

  public static class Handler implements IMessageHandler<BedPartUpdate, IMessage> {

    public Handler(){ }

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
      // BlockWoodenBed.setPartColor(message.part, message.pos, w, message.color);
      return null;
    }
  }
}
