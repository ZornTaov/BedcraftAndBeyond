package zornco.bedcraftbeyond.core.commands.fragments;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import zornco.bedcraftbeyond.frames.registry.FrameException;
import zornco.bedcraftbeyond.frames.registry.FrameFile;
import zornco.bedcraftbeyond.frames.registry.FrameRegistry;
import zornco.bedcraftbeyond.frames.registry.FrameWhitelist;
import zornco.bedcraftbeyond.core.commands.CommandFragment;
import zornco.bedcraftbeyond.core.config.ConfigHelper;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class FragmentFramesDump extends CommandFragment {

    // 0: frames, 1: dump
    // 2: wooden/stone
    public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        if(args.length == 3)
            return CommandBase.getListOfStringsMatchingLastWord(args, FrameRegistry.EnumFrameType.getPossible());
        return Collections.emptyList();
    }

    public static void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length < 3) throw new CommandException("nope.jpg");
        FrameRegistry.EnumFrameType type = zornco.bedcraftbeyond.core.commands.FramesCommandsUtils.getFrameTypeFromArgs(args, 2);
        dumpFrameList(type);
    }

    private static void dumpFrameList(FrameRegistry.EnumFrameType type) throws FrameException {
        FrameWhitelist frames = FrameRegistry.getFrameWhitelist(type);

        List<FrameFile.FrameEntry> entriesContainer = new ArrayList<>();
        for(ResourceLocation entry : frames.getValidRegistryEntries()){
            RangeSet<Integer> entryMetas = frames.getEntry(entry).getValidMeta();
            FrameFile.FrameEntry fEntry = new FrameFile.FrameEntry();
            fEntry.key = entry.toString();
            fEntry.type = type.name().toLowerCase();
            List<Object> entries = new ArrayList<>();
            for(Range<Integer> range : entryMetas.asRanges()){
                if(range.lowerEndpoint() == range.upperEndpoint()){
                    entries.add(range.lowerEndpoint());
                    continue;
                }

                entries.add(range.lowerEndpoint() + "-" + range.upperEndpoint());
            }
            fEntry.whitelist = entries.toArray(new Object[entries.size()]);
            entriesContainer.add(fEntry);
        }

        FrameFile f = new FrameFile();
        f.entries = entriesContainer.toArray(new FrameFile.FrameEntry[entriesContainer.size()]);

        Gson gb = new GsonBuilder().setPrettyPrinting().create();
        String json = gb.toJson(f, FrameFile.class);
        File frameFile = Paths.get(ConfigHelper.modConfigDir.getPath(), "frames", type.name().toLowerCase() + ".json").toFile();
        if(!frameFile.exists()) {
            try {
                frameFile.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        try {
            FileOutputStream os = new FileOutputStream(frameFile);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write(json);
            osw.flush();
            osw.close();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
