package zornco.bedcraftbeyond.common.commands;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.compress.archivers.dump.InvalidFormatException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.common.frames.FrameRegistry;
import zornco.bedcraftbeyond.util.RangeHelper;

import java.text.ParseException;
import java.util.*;

public class CommandFragmentFrames extends CommandFragment {

   @Override
   public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if(args.length < 2) throwError("no_subcommand");
      switch(args[1].toLowerCase()){
         case "list":
            handleList(sender, args);
            break;

         case "add":
            addCommand(args, sender);
            break;

         case "remove":
         case "rem":
            remCommand(args, sender);
            break;

         case "reload":
            sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".messages.reloadingFrames"));
            BedCraftBeyond.proxy.compileFrames();
            break;
      }
   }

   private FrameRegistry.EnumBedFrameType getFrameTypeFromArgs(String[] args, int index) throws CommandException {
      switch (args[index].toLowerCase()) {
         case "wooden":
         case "wood":
            return FrameRegistry.EnumBedFrameType.WOOD;

         case "stone":
            return FrameRegistry.EnumBedFrameType.STONE;

         default:
            throwError("invalid_frame_type");
      }

      return FrameRegistry.EnumBedFrameType.WOOD;
   }

   private Block validateModifyCommand(String[] args, ICommandSender sender) throws CommandException {
      if(args.length < 4) throwError("missing_parameter");
      ResourceLocation resource = new ResourceLocation(args[3].toLowerCase());
      Block b = Block.getBlockFromName(resource.toString());
      if(b == null) throwError("invalid_block");
      return b;
   }

   /* 0: frames, 1: add, 2: wooden/stone, 3: registryName, 4: meta??? */
   private void addCommand(String[] args, ICommandSender sender) throws CommandException {
      Block b = validateModifyCommand(args, sender);
      FrameRegistry.EnumBedFrameType type = getFrameTypeFromArgs(args, 2);
      if(b != null){
         boolean added = FrameRegistry.addEntry(type, b.getRegistryName());
         sender.addChatMessage(new TextComponentTranslation(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".frames.registry_mod_" + (added ? "success" : "fail"))));
      }
   }

   /* 0: frames, 1: remove, 2: wooden/stone, 3: registryName, 4: meta??? */
   private void remCommand(String[] args, ICommandSender sender) throws CommandException {
      Block b = validateModifyCommand(args, sender);
      FrameRegistry.EnumBedFrameType type = getFrameTypeFromArgs(args, 2);
      if(b != null){
         boolean removed = false; int meta = OreDictionary.WILDCARD_VALUE;
         if(args.length > 4 && args[4] != "") {
            try { meta = Integer.parseInt(args[4]); }
            catch(NumberFormatException fe){ throwError("bad_meta_format"); }
         }

         sender.addChatMessage(new TextComponentTranslation(I18n.translateToLocal(BedCraftBeyond.MOD_ID + ".frames.registry_mod_" + (removed ? "success" : "fail"))));
      }
   }

   @Override
   public List<String> getTabOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
      switch(args.length){
         case 2:
            return CommandBase.getListOfStringsMatchingLastWord(args, ImmutableList.of("add", "remove", "reload", "list"));
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
      if(args.length < 3) throwError("no_frame_type");
      switch(args[2].toLowerCase()){
         case "wood":
         case "wooden":
            HashMap<ResourceLocation, Set<Integer>> frames = FrameRegistry.getFrameSet(FrameRegistry.EnumBedFrameType.WOOD);
            List<String> entries = new ArrayList<>();
            for(ResourceLocation rl : frames.keySet()){
               if(frames.get(rl).size() == 0){ if(rl.toString() != "") entries.add(rl.toString()); continue; }
               Set<Integer> allowedMetaSet = frames.get(rl);
               int[] allowedMeta = ArrayUtils.toPrimitive(allowedMetaSet.toArray(new Integer[allowedMetaSet.size()]));

               // biomesoplenty:planks_0@{0-15}, minecraft:planks
               List<RangeHelper.Range> ranges = RangeHelper.getRanges(allowedMeta);
               entries.add(rl.toString() + "@" + StringUtils.join(ranges, ","));
            }

            sender.addChatMessage(new TextComponentString(StringUtils.join(entries, ", ")));
            break;

         case "stone":
            sender.addChatMessage(new TextComponentString("nyi"));
            break;

         default:
            throwError("unknown_frame_type");
      }
   }
}
