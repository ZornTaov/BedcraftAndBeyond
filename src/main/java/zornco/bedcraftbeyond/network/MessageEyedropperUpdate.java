package zornco.bedcraftbeyond.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.common.util.ColorHelper;

import java.awt.*;
import java.util.UUID;

public class MessageEyedropperUpdate implements IMessage {

    public UUID playerID;
    public EnumHand hand;
    public int color;

    public MessageEyedropperUpdate(){
        this.playerID = null;
        this.hand = EnumHand.MAIN_HAND;
        this.color = 0;
    }

    public MessageEyedropperUpdate(EntityPlayer player, EnumHand hand, int color){
        this.playerID = player.getUniqueID();
        this.hand = hand;
        this.color = color;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.playerID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.hand = buf.readByte() == 1 ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
        this.color = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerID.toString());
        buf.writeByte(hand == EnumHand.MAIN_HAND ? 1 : 0);
        buf.writeInt(color);
    }

    public static class Handler implements IMessageHandler<MessageEyedropperUpdate, IMessage> {

        @Override
        public IMessage onMessage(MessageEyedropperUpdate message, MessageContext ctx) {
            World world = ctx.getServerHandler().playerEntity.worldObj;
            EntityPlayer player = world.getPlayerEntityByUUID(message.playerID);

            ItemStack eyedropper = player.getHeldItem(message.hand);
            if(eyedropper == null) return null;
            if(!eyedropper.hasTagCompound()) eyedropper.setTagCompound(new NBTTagCompound());
            NBTTagCompound current = eyedropper.getTagCompound();
            current.setTag("color", ColorHelper.getTagForColor(new Color(message.color)));
            eyedropper.setTagCompound(current);

            return null;
        }
    }
}
