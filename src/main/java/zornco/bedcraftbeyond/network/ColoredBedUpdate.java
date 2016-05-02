package zornco.bedcraftbeyond.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import scala.util.control.Exception;
import zornco.bedcraftbeyond.blocks.BlockColoredBed;

public class ColoredBedUpdate implements IMessage {

  private BlockPos pos;
  private BlockColoredBed.EnumColoredPart part;
  private EnumDyeColor color;

  public ColoredBedUpdate(){  }
  public ColoredBedUpdate(BlockPos pos, BlockColoredBed.EnumColoredPart part, EnumDyeColor color){
    this.pos = pos;
    this.part = part;
    this.color = color;
  }

  /**
   * Convert from the supplied buffer into your specific message type
   *
   * @param buf
   */
  @Override
  public void fromBytes(ByteBuf buf) {
    pos = NBTUtil.getPosFromTag(ByteBufUtils.readTag(buf));
    color = EnumDyeColor.byMetadata(buf.readInt());
    part = BlockColoredBed.EnumColoredPart.valueOf(ByteBufUtils.readUTF8String(buf));
  }

  /**
   * Deconstruct your message into the supplied byte buffer
   *
   * @param buf
   */
  @Override
  public void toBytes(ByteBuf buf) {
    ByteBufUtils.writeTag(buf, NBTUtil.createPosTag(pos));
    buf.writeInt(color.getMetadata());
    ByteBufUtils.writeUTF8String(buf, part.name());
  }

  public static class Handler implements IMessageHandler<ColoredBedUpdate, IMessage> {

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
    public IMessage onMessage(ColoredBedUpdate message, MessageContext ctx) {
      World w = Minecraft.getMinecraft().theWorld;
      IBlockState curState = w.getBlockState(message.pos);
      BlockColoredBed.setPartColor(message.part, message.pos, w, message.color);
      return null;
    }
  }
}
