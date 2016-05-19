package zornco.bedcraftbeyond.common.commands;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.List;

public abstract class CommandFragment {

   void throwError(String unlocal) throws CommandException { throw new CommandException(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".errors." + unlocal)); }

   abstract void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

   abstract List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos);
}
