package zornco.bedcraftbeyond.common.commands.fragments;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.commands.CommandFragment;
import zornco.bedcraftbeyond.common.commands.FramesCommandsUtils;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;

import java.util.Collections;
import java.util.List;

public class FragmentFramesCheck extends CommandFragment {

    public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        switch(args.length){
            case 3:
                return CommandBase.getListOfStringsMatchingLastWord(args, FrameRegistry.EnumFrameType.getPossible());

            case 4:
            case 5:
            case 6:
                return getCoordinateArg(args, pos, 4);

            default:
                return Collections.emptyList();
        }
    }

    public static void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 3) throw new CommandException("nope.jpg");
        FrameRegistry.EnumFrameType type = FramesCommandsUtils.getFrameTypeFromArgs(args, 2);
        IBlockState state = FramesCommandsUtils.getBlockstateFromCommand(args, 3, sender);
        ItemStack is = state.getBlock().getPickBlock(state, null, sender.getEntityWorld(), CommandBase.parseBlockPos(sender, args, 3, false), null);

        boolean valid = FrameRegistry.isValidFrameMaterial(type, state);
        sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.messages.frame_" + (valid ? "valid" : "invalid"), is.getDisplayName(), state.getBlock().getMetaFromState(state), type.name().toLowerCase()));
    }
}
