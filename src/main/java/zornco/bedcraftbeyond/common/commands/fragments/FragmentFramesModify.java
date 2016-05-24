package zornco.bedcraftbeyond.common.commands.fragments;

import com.google.common.collect.ImmutableList;
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
import zornco.bedcraftbeyond.common.frames.FrameException;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;

import java.util.Collections;
import java.util.List;

// 0: frames, 1: add/remove
// 2: wooden/stone, 3-5: pos, 6 (opt): all/single
public class FragmentFramesModify extends CommandFragment {

    public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        switch(args.length){
            case 3:
                return ImmutableList.of("wooden", "stone");

            case 4:
            case 5:
            case 6:
                return getCoordinateArg(args, pos, 4);

            case 7:
                return ImmutableList.of("all", "meta");
        }

        return Collections.emptyList();
    }

    public static void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        IBlockState b = validateModifyCommand(args, sender);
        FrameRegistry.EnumBedFrameType type = FragmentFrames.getFrameTypeFromArgs(args, 2);
        if (b == null) {
            sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.registry_mod_fail_unknown"));
            return;
        }

        ItemStack is = b.getBlock().getPickBlock(b, null, sender.getEntityWorld(), CommandBase.parseBlockPos(sender, args, 3, false), null);

        // TODO: Move removed stuff to its own method and finish cleaning up the command LANG entries.
        // Almost there. Hang in.
        boolean modified = false;
        switch (args[1].toLowerCase()) {
            case "add":
                handleAdd(type, b, is, sender, args);
                break;

            case "clear":
                FrameRegistry.resetWhitelistForEntry(type, b.getBlock().getRegistryName());
                // TODO: Return success message
                break;

            case "remove":
            case "delete":
                //if(noMeta)
                modified = FrameRegistry.removeEntry(type, b.getBlock().getRegistryName());
                //else {
                try {
                    modified = FrameRegistry.addWhitelistEntry(type, b.getBlock().getRegistryName(), b.getBlock().getMetaFromState(b));
                } catch (FrameException e) {
                    BedCraftBeyond.logger.error(e);
                    throw new CommandException(e.getLocalizedMessage());
                }
                //}
                break;
        }
    }

    private static IBlockState validateModifyCommand(String[] args, ICommandSender sender) throws CommandException {
        if (args.length < 6) throw new CommandException("errors.generic.syntax");
        BlockPos p = CommandBase.parseBlockPos(sender, args, 3, false);
        if (sender.getEntityWorld() == null)
            throw new CommandException(BedCraftBeyond.MOD_ID + ".errors.invalid_world");

        return sender.getEntityWorld().getBlockState(p);
    }

    private static void handleAdd(FrameRegistry.EnumBedFrameType type, IBlockState state, ItemStack item, ICommandSender sender, String[] args) throws CommandException {
        try {
            String modType;
            boolean success = false;
            if (args.length < 7) modType = "all";
            else modType = args[6].toLowerCase();

            switch (modType) {
                case "all":
                    success = FrameRegistry.addEntry(type, state.getBlock().getRegistryName());
                    if (!success) throw new CommandException(BedCraftBeyond.MOD_ID + ".frames.whitelisted.fail");
                    sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.whitelisted", item.getDisplayName(), type.name().toLowerCase()));
                    break;

                case "meta":
                    success = FrameRegistry.removeWhitelistEntry(type, state.getBlock().getRegistryName(), state.getBlock().getMetaFromState(state));
                    if (!success)
                        throw new CommandException(BedCraftBeyond.MOD_ID + ".frames.whitelisted.by_meta.fail");
                    sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.whitelisted.by_meta", item.getDisplayName(), state.getBlock().getMetaFromState(state), type.name().toLowerCase()));
                    break;
            }
        } catch (FrameException fe) {
            BedCraftBeyond.logger.error(fe);
            throw new CommandException(fe.getLocalizedMessage());
        }
    }
}
