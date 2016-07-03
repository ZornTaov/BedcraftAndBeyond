package zornco.bedcraftbeyond.core.commands.fragments;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.commands.CommandFragment;
import zornco.bedcraftbeyond.core.gui.GuiHandler;

import java.util.Collections;
import java.util.List;

public class FragmentFramesEdit extends CommandFragment {

    public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        switch(args.length){
            case 3:
                return CommandBase.getListOfStringsMatchingLastWord(args, FrameRegistry.EnumFrameType.getPossible());
        }

        return Collections.emptyList();
    }

    public static void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        FrameRegistry.EnumFrameType type = zornco.bedcraftbeyond.core.commands.FramesCommandsUtils.getFrameTypeFromArgs(args, 2);
        if(type != FrameRegistry.EnumFrameType.UNKNOWN)
            if(sender instanceof EntityPlayer) {
                ((EntityPlayer) sender).openGui(BedCraftBeyond.INSTANCE, GuiHandler.ID_FRAMES_LIST, sender.getEntityWorld(), type.ordinal(), 0, 0);
            }
    }

}
