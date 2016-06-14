package zornco.bedcraftbeyond.common.frames;

import com.google.common.collect.Range;
import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.config.ConfigHelper;
import zornco.bedcraftbeyond.config.ConfigSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrameLoader {

    private static int removeFramesFromStream(FrameFile frames, FrameRegistry.EnumFrameType type) {
        int removed = 0;
        for (FrameFile.FrameEntry frameEntry : frames.entries) {
            if(!frameEntry.hasBlacklist()) continue;

            ResourceLocation entryRes = new ResourceLocation(frameEntry.key);
            Block b = Block.REGISTRY.getObject(entryRes);
            if(b == null) continue;
            for(Object entryItem : frameEntry.blacklist) {
                if(entryItem instanceof String) {
                    String entryString = String.valueOf(entryItem);
                    switch(entryString.toLowerCase()) {
                        case "all":
                            FrameRegistry.getFrameWhitelist(type).removeEntry(b.getRegistryName());
                            List<ItemStack> addedList = new ArrayList<>();
                            b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
                            removed += addedList.size();
                            break;

                        default:
                            // TODO: Meta range?
                            Pattern p = Pattern.compile("(?<lower>\\d+)\\-(?<upper>\\d+)");
                            Matcher m = p.matcher(entryString);
                            if(!m.matches()) continue;
                            int lower = Integer.parseInt(m.group("lower"));
                            int upper = Integer.parseInt(m.group("upper"));
                            Range<Integer> range = Range.open(lower, upper);
                            break;
                    }
                } else if(entryItem instanceof Number) {
                    try {
                        FrameRegistry.getFrameWhitelist(type).removeWhitelistEntry(entryRes, ((Number) entryItem).intValue());
                    } catch (FrameException e) {
                        BedCraftBeyond.LOGGER.error(e.getMessage());
                    }
                } else {

                }
            }
        }

        // TODO: Add check to see if registry value supported, and check meta list for block type (minecraft:planks -> Spruce Wood, for example)
        return removed;
    }

    private static int addToFramesFromStream(FrameFile frames, FrameRegistry.EnumFrameType type) {
        int added = 0;
        for (FrameFile.FrameEntry frameEntry : frames.entries) {
            if(!frameEntry.hasWhitelist()) continue;

            ResourceLocation entryRes = new ResourceLocation(frameEntry.key);
            Block b = Block.REGISTRY.getObject(entryRes);
            if(b == null) continue;
            for(Object entryItem : frameEntry.whitelist) {
                if(entryItem instanceof String) {
                    String entryString = String.valueOf(entryItem);
                    if(entryString.trim().equalsIgnoreCase("all")){
                        FrameRegistry.getFrameWhitelist(type).resetWhitelistForEntry(b.getRegistryName());
                        List<ItemStack> addedList = new ArrayList<>();
                        b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
                        added += addedList.size();
                        break;
                    }

                    // TODO: Meta range?
                    if(!entryString.contains("-")) continue;
                    Pattern p = Pattern.compile("(?<lower>\\d+)\\-(?<upper>\\d+)");
                    Matcher m = p.matcher(entryString);
                    if(!m.matches()) continue;
                    int lower = Integer.parseInt(m.group("lower"));
                    int upper = Integer.parseInt(m.group("upper"));
                    Range<Integer> range = Range.open(lower, upper);
                    try {
                        FrameRegistry.getFrameWhitelist(type).addWhitelistRange(entryRes, range);
                    } catch (FrameException e) {
                        e.printStackTrace();
                    }
                } else if(entryItem instanceof Number) {
                    try {
                        FrameRegistry.getFrameWhitelist(type).addWhitelistEntry(entryRes, ((Number) entryItem).intValue());
                    } catch (FrameException e) {
                        BedCraftBeyond.LOGGER.error(I18n.translateToLocal(e.getMessage()));
                    }
                } else {
                    BedCraftBeyond.LOGGER.info("Unrecognized entry: " + entryItem.toString());
                }
            }
        }
        // TODO: Add check to see if registry value supported, and check meta list for block type (minecraft:planks -> Spruce Wood, for example)
        return added;
    }

    private static void addFramesFromOredictEntries(FrameRegistry.EnumFrameType type, String oreDictName) {
        for (ItemStack stack : OreDictionary.getOres(oreDictName)) {
            //not bothering with items that are not blocks
            if (!(stack.getItem() instanceof ItemBlock)) continue;
            ResourceLocation regName = stack.getItem().getRegistryName();
            if (regName == null) {
                BedCraftBeyond.LOGGER.error("Found a null registry entry trying to add oreDict frames: " + stack.toString()
                    + ". This is a critical issue, report it to the mod author!");
                continue;
            }

            try {
                // If set to use all metadatas from item
                if (stack.getItemDamage() == OreDictionary.WILDCARD_VALUE)
                    FrameRegistry.getFrameWhitelist(type).addEntry(stack.getItem().getRegistryName());
                else
                    FrameRegistry.getFrameWhitelist(type).addWhitelistEntry(regName, stack.getMetadata());
            } catch (FrameException e) {
                BedCraftBeyond.LOGGER.error(e.getMessage());
            }
        }
    }

    public static void tryFrameTypeFileLoad(FrameRegistry.EnumFrameType type) throws FileNotFoundException {
        File frameFile = Paths.get(ConfigHelper.modConfigDir.getPath(), "frames", type.name().toLowerCase() + ".json").toFile();
        if(!frameFile.exists()) return;

        FileReader fr = new FileReader(frameFile);
        FrameFile frames = new Gson().fromJson(fr, FrameFile.class);
        addToFramesFromStream(frames, type);
        removeFramesFromStream(frames, type);
    }

    public static void compileFrames() {
        Logger l = BedCraftBeyond.LOGGER;

        FrameRegistry.dumpFrameList();
        if (ConfigSettings.ADD_OREDICT_WOODEN || ConfigSettings.ADD_OREDICT_STONE)
            l.info("Loading frame data from the ore dictionary...");
        if (ConfigSettings.ADD_OREDICT_WOODEN)
            addFramesFromOredictEntries(FrameRegistry.EnumFrameType.WOOD, "plankWood");

        l.info("Loading frames from config files..");
        try {
            tryFrameTypeFileLoad(FrameRegistry.EnumFrameType.WOOD);
            tryFrameTypeFileLoad(FrameRegistry.EnumFrameType.STONE);
        }

        catch(FileNotFoundException fne){
            BedCraftBeyond.LOGGER.error("Could not find frame file to load: " + fne.getMessage());
        }

        // TODO: Reimplement total available frame count?
    }
}
