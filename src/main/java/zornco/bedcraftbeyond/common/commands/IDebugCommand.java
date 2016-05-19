package zornco.bedcraftbeyond.common.commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface IDebugCommand {

   void addDebugInformation(MinecraftServer server, ICommandSender sender, BlockPos pos);

}
