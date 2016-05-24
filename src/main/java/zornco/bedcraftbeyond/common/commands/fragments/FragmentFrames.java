package zornco.bedcraftbeyond.common.commands.fragments;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.commands.CommandFragment;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.util.RangeHelper;

import java.util.*;

public class FragmentFrames extends CommandFragment {

   public static void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if(args.length < 2) throw new CommandException(BedCraftBeyond.MOD_ID + ".errors.no_subcommand");
      switch(args[1].toLowerCase()){
         case "list":
            handleList(sender, args);
            break;

         case "add":
         case "remove":
            FragmentFramesModify.execute(server, sender, args);
            break;

         case "reload":
            sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".messages.reloadingFrames"));
            BedCraftBeyond.proxy.compileFrames();
            break;
      }
   }

   public static List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      switch(args.length){
         case 2:
            return CommandBase.getListOfStringsMatchingLastWord(args, ImmutableList.of("add", "remove", "reload", "list"));
         default:
            switch(args[1].toLowerCase()){
               case "add":
               case "remove":
                  return FragmentFramesModify.getTabOptions(server, sender, args, pos);

               case "reload":

                  break;

               case "list":
                  return ImmutableList.of("wooden", "stone");
            }

            return Collections.emptyList();
      }
   }

   public static FrameRegistry.EnumBedFrameType getFrameTypeFromArgs(String[] args, int index) throws CommandException {
      switch (args[index].toLowerCase()) {
         case "wooden":
         case "wood":
            return FrameRegistry.EnumBedFrameType.WOOD;

         case "stone":
            return FrameRegistry.EnumBedFrameType.STONE;

         default:
            throw new CommandException(BedCraftBeyond.MOD_ID + ".errors.frames.invalid_frame_type");
      }
   }

   // bedcraft frames list <part>
   private static void handleList(ICommandSender sender, String[] args) throws CommandException {
      if(args.length < 3) throw new CommandException(BedCraftBeyond.MOD_ID + ".errors.frames.no_frame_type");
      switch(args[2].toLowerCase()){
         case "wood":
         case "wooden":
            HashMap<ResourceLocation, RangeSet<Integer>> frames = FrameRegistry.getFrameSet(FrameRegistry.EnumBedFrameType.WOOD);
            List<String> entries = new ArrayList<>();
            for(ResourceLocation rl : frames.keySet()){
               RangeSet<Integer> allowedMetaSet = frames.get(rl);
               // biomesoplenty:planks_0@{0-15}, minecraft:planks
               entries.add(rl.toString() + "@" + StringUtils.join(allowedMetaSet, ","));
            }

            sender.addChatMessage(new TextComponentString(StringUtils.join(entries, ", ")));
            break;

         case "stone":
            sender.addChatMessage(new TextComponentString("nyi"));
            break;

         default:
            throw new CommandException(BedCraftBeyond.MOD_ID + ".errors.frames.unknown_frame_type");
      }
   }
}
