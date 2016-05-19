package zornco.bedcraftbeyond.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public interface ICommandPart {

   void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

   List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
}
