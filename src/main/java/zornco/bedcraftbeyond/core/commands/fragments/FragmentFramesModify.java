package zornco.bedcraftbeyond.core.commands.fragments;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelistEntry;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.commands.CommandFragment;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;

import java.util.Collections;
import java.util.List;

// 0: frames, 1: add/remove
// 2: wooden/stone, 3-5: pos, 6 (opt): all/single
public class FragmentFramesModify extends CommandFragment {

    public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        switch(args.length){
            case 3:
                return CommandBase.getListOfStringsMatchingLastWord(args, FrameRegistry.EnumFrameType.getPossible());

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
        IBlockState b = zornco.bedcraftbeyond.core.commands.FramesCommandsUtils.getBlockstateFromCommand(args, 3, sender);
        FrameRegistry.EnumFrameType type = zornco.bedcraftbeyond.core.commands.FramesCommandsUtils.getFrameTypeFromArgs(args, 2);
        if (b == null) {
            sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.registry_mod_fail_unknown"));
            return;
        }

        ItemStack is = b.getBlock().getPickBlock(b, null, sender.getEntityWorld(), CommandBase.parseBlockPos(sender, args, 3, false), null);

        switch (args[1].toLowerCase()) {
            case "add":
                handleAdd(type, b, is, sender, args);
                break;

            case "reset":
                FrameRegistry.getFrameWhitelist(type).getEntry(b.getBlock().getRegistryName()).reset();
                sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.messages.reset_entry_complete", type));
                break;

            case "remove":
            case "delete":
                handleRemove(type, b, is, sender, args);
                break;
        }
    }

    private static void handleAdd(FrameRegistry.EnumFrameType type, IBlockState state, ItemStack item, ICommandSender sender, String[] args) throws CommandException {
        try {
            String modType;
            boolean success;
            if (args.length < 7) modType = "all";
            else modType = args[6].toLowerCase();

            switch (modType) {
                case "all":
                    success = FrameRegistry.getFrameWhitelist(type).addEntry(state.getBlock().getRegistryName());
                    if (!success) throw new CommandException(BedCraftBeyond.MOD_ID + ".frames.whitelisted.fail", item.getDisplayName(), type.name().toLowerCase());
                    sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.whitelisted", item.getDisplayName(), type.name().toLowerCase()));
                    break;

                case "meta":
                    success = FrameRegistry.getFrameWhitelist(type).getEntry(state.getBlock().getRegistryName()).whitelist(state.getBlock().getMetaFromState(state));
                    if (!success)
                        throw new CommandException(BedCraftBeyond.MOD_ID + ".frames.whitelisted.by_meta.fail", item.getDisplayName(), state.getBlock().getMetaFromState(state), type.name().toLowerCase());
                    sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.whitelisted.by_meta", item.getDisplayName(), state.getBlock().getMetaFromState(state), type.name().toLowerCase()));
                    break;
            }
        } catch (FrameException fe) {
            BedCraftBeyond.LOGGER.error(fe);
            throw new CommandException(fe.getLocalizedMessage());
        }
    }

    private static void handleRemove(FrameRegistry.EnumFrameType type, IBlockState state, ItemStack item, ICommandSender sender, String[] args) throws CommandException {
        try {
            String modType;
            boolean success;
            if (args.length < 7) modType = "all";
            else modType = args[6].toLowerCase();

            switch (modType) {
                case "all":
                    success = FrameRegistry.getFrameWhitelist(type).removeEntry(state.getBlock().getRegistryName());
                    if (!success) throw new CommandException(BedCraftBeyond.MOD_ID + ".frames.blacklisted.fail", item.getDisplayName(), type.name().toLowerCase());
                    sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.blacklisted", item.getDisplayName(), type.name().toLowerCase()));
                    break;

                case "meta":
                    FrameWhitelistEntry entry = FrameRegistry.getFrameWhitelist(type).getEntry(state.getBlock().getRegistryName());
                    success = entry.blacklist(state.getBlock().getMetaFromState(state));
                    if (!success)
                        throw new CommandException(BedCraftBeyond.MOD_ID + ".frames.blacklisted.by_meta.fail", item.getDisplayName(), state.getBlock().getMetaFromState(state), type.name().toLowerCase());
                    sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".frames.blacklisted.by_meta", item.getDisplayName(), state.getBlock().getMetaFromState(state), type.name().toLowerCase()));
                    break;
            }
        } catch (FrameException fe) {
            BedCraftBeyond.LOGGER.error(fe);
            throw new CommandException(fe.getLocalizedMessage());
        }
    }
}
