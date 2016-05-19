package zornco.bedcraftbeyond.common.commands;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.IntegerCache;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.util.RangeHelper;

import javax.print.DocFlavor;
import java.util.*;

public class CommandPartFrames implements ICommandPart {

   @Override
   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if(args.length < 2) throw new CommandException(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".errors.no_subcommand"));
      switch(args[1].toLowerCase()){
         case "list":
            handleList(sender, args);
            break;

         case "add":
            addCommand(args, sender);
            break;

         case "remove":
         case "rem":

            break;

         case "reload":
            sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".messages.reloadingFrames"));
            BedCraftBeyond.proxy.compileFrames();
            break;
      }
   }

   private void addCommand(String[] args, ICommandSender sender) throws CommandException {
      if(args.length < 3) throw new CommandException(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".errors.missing_parameter"));
      String rl = args[2].toLowerCase();
      ResourceLocation resource = new ResourceLocation(rl);
      Block b = Block.getBlockFromName(resource.toString());
      if(b != null){
         boolean added = FrameRegistry.addEntry(FrameRegistry.EnumBedFrameType.WOOD, resource);
         sender.addChatMessage(new TextComponentTranslation(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".frames.registry_mod_" + (added ? "success" : "fail"))));
      }
   }

   private void remCommand(String[] args, ICommandSender sender) throws CommandException {
      if(args.length < 3) throw new CommandException(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".errors.missing_parameter"));
      String rl = args[2].toLowerCase();
      ResourceLocation resource = new ResourceLocation(rl);
      Block b = Block.getBlockFromName(resource.toString());
      if(b != null){
         boolean added = FrameRegistry.removeEntry(FrameRegistry.EnumBedFrameType.WOOD, resource);
         sender.addChatMessage(new TextComponentTranslation(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".frames.registry_mod_" + (added ? "success" : "fail"))));
      }
   }

   @Override
   public List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      switch(args.length){
         case 2:
            return ImmutableList.of("add", "remove", "reload", "list");
         case 3:
            switch(args[1].toLowerCase()){
               case "add":

                  break;

               case "remove":

                  break;

               case "reload":

                  break;

               case "list":
                  return ImmutableList.of("wooden", "stone");
            }

            return Collections.emptyList();

         default:
            return Collections.emptyList();
      }
   }

   // bedcraft frames list <part>
   private void handleList(ICommandSender sender, String[] args) throws CommandException {
      if(args.length < 3) throw new CommandException(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".errors.no_frame_type"));
      switch(args[2].toLowerCase()){
         case "wood":
         case "wooden":
            HashMap<ResourceLocation, Set<Integer>> frames = FrameRegistry.getFrameSet(FrameRegistry.EnumBedFrameType.WOOD);
            List<String> entries = new ArrayList<>();
            for(ResourceLocation rl : frames.keySet()){
               if(frames.get(rl).size() == 0){ if(rl.toString() != "") entries.add(rl.toString()); continue; }
               Set<Integer> allowedMetaSet = frames.get(rl);
               int[] allowedMeta = ArrayUtils.toPrimitive(allowedMetaSet.toArray(new Integer[allowedMetaSet.size()]));

               // TODO: Remove empty entries here.
               // biomesoplenty:planks_0@{0-15},,,,,,,,,,,,,,,, minecraft:planks
               RangeHelper.Range[] ranges = RangeHelper.getRanges(allowedMeta);
               entries.add(rl.toString() + "@" + StringUtils.join(ranges, ","));
            }

            sender.addChatMessage(new TextComponentString(StringUtils.join(entries, ", ")));
            break;

         case "stone":
            sender.addChatMessage(new TextComponentString("nyi"));
            break;

         default:
            throw new CommandException(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".errors.unknown_frame_type"));
      }
   }
}
