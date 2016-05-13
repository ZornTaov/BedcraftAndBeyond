package zornco.bedcraftbeyond.common.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.apache.commons.lang3.StringUtils;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class CommandFrames extends CommandBase {

   /**
    * Gets the name of the command
    */
   @Override
   public String getCommandName() {
      return "bcbframes";
   }

   /**
    * Gets the usage string for the command.
    *
    * @param sender
    */
   @Override
   public String getCommandUsage(ICommandSender sender) {
      return "/bcbframes [add/rem/dump] [wooden/stone]";
   }

   @Override
   public List<String> getCommandAliases() {
      return Collections.emptyList();
   }

   /**
    * Callback for when the command is executed
    *
    * @param server The Minecraft server instance
    * @param sender The source of the command invocation
    * @param args   The arguments that were passed
    */
   @Override
   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if(sender.getCommandSenderEntity().worldObj.isRemote) return;
      switch(args[0].toLowerCase()){
         case "list":
            if(args.length < 2) throw new CommandException("Must choose frame type to list.");
            switch(args[1].toLowerCase()){
               case "wood":
               case "wooden":
                  Set<ResourceLocation> frames = FrameRegistry.getWoodFrameSet();
                  String framesJoined = StringUtils.join(frames, ", ");
                  sender.addChatMessage(new TextComponentString(framesJoined));
                  break;

               case "stone":

                  break;

               default:
                  throw new CommandException("Unknown frame type.");
            }
            break;

         case "reload":
            sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".messages.reloadingFrames"));
            BedCraftBeyond.proxy.compileFrames();
            break;

         default:
            break;
      }
   }

   @Override
   public int getRequiredPermissionLevel() {
      return 2;
   }

   @Override
   public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      return null;
   }

   /**
    * Return whether the specified command parameter index is a username parameter.
    *
    * @param args
    * @param index
    */
   @Override
   public boolean isUsernameIndex(String[] args, int index) {
      return false;
   }

   @Override
   public int compareTo(ICommand o) {
      return o.getCommandName().compareToIgnoreCase(this.getCommandName());
   }
}
