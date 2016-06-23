package zornco.bedcraftbeyond.core.commands;

import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.math.BlockPos;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.beds.frames.registry.FrameRegistry;

public class FramesCommandsUtils {

    /**
     * Get a frame type from a "hard-coded" value.
     *
     * @param args
     * @param index
     * @return
     * @throws CommandException
     */
    public static FrameRegistry.EnumFrameType getFrameTypeFromArgs(String[] args, int index) {
        switch (args[index].toLowerCase()) {
            case "wooden":
            case "wood":
                return FrameRegistry.EnumFrameType.WOOD;

            case "stone":
                return FrameRegistry.EnumFrameType.STONE;

            default:
                return FrameRegistry.EnumFrameType.UNKNOWN;
        }
    }

    public static IBlockState getBlockstateFromCommand(String[] args, int argStart, ICommandSender sender) throws CommandException {
        BlockPos p = CommandBase.parseBlockPos(sender, args, argStart, false);
        if (sender.getEntityWorld() == null)
            throw new CommandException(BedCraftBeyond.MOD_ID + ".errors.invalid_world");

        return sender.getEntityWorld().getBlockState(p);
    }

}
