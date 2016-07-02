package zornco.bedcraftbeyond.beds.frames.registry.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.beds.frames.registry.FrameWhitelistEntry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.gui.GuiHandler;

import java.util.UUID;

public class MessageOpenWhitelistEditor implements IMessage {

    private ResourceLocation entryID;
    private UUID playerID;
    private FrameRegistry.EnumFrameType type;

    public MessageOpenWhitelistEditor(){}

    public MessageOpenWhitelistEditor(FrameWhitelistEntry entry, FrameRegistry.EnumFrameType type, UUID playerUniqueID) {
        this.entryID = entry.getID();
        this.type = type;
        this.playerID = playerUniqueID;
    }


    @Override
    public void fromBytes(ByteBuf buf) {
        entryID = new ResourceLocation(ByteBufUtils.readUTF8String(buf));
        playerID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        type = FrameRegistry.EnumFrameType.valueOf(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, entryID.toString());
        ByteBufUtils.writeUTF8String(buf, playerID.toString());
        ByteBufUtils.writeUTF8String(buf, type.name());
    }

    public static class Handler implements IMessageHandler<MessageOpenWhitelistEditor, IMessage> {

        public Handler(){}

        @Override
        public IMessage onMessage(MessageOpenWhitelistEditor message, MessageContext ctx) {
            MinecraftServer server = FMLCommonHandler.instance().getMinecraftServerInstance();
            EntityPlayer player = (EntityPlayer) server.getEntityFromUuid(message.playerID);
            if(player == null) return null;

            player.openGui(BedCraftBeyond.INSTANCE, GuiHandler.ID_FRAMES_EDIT, server.getEntityWorld(), 0,0,0);
            return null;
        }
    }
}
