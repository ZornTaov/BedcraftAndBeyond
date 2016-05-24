package zornco.bedcraftbeyond.common.commands;

import com.google.common.collect.ImmutableList;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import java.util.Collections;
import java.util.List;

public abstract class CommandFragment {

   public static void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException { }

   public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos){ return Collections.emptyList(); }

   protected static List<String> getCoordinateArg(String[] args, BlockPos pos, int start){
      if(pos == null) return ImmutableList.of("~");
      switch(args.length - start){
         case 0:
            return ImmutableList.of(Integer.toString(pos.getX()));
         case 1:
            return ImmutableList.of(Integer.toString(pos.getY()));
         case 2:
            return ImmutableList.of(Integer.toString(pos.getZ()));
         default:
            return Collections.emptyList();
      }
   }
}
