package zornco.bedcraftbeyond.beds.frames.registry;

import com.google.common.collect.Range;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.core.BedCraftBeyond;
import zornco.bedcraftbeyond.core.config.ConfigHelper;
import zornco.bedcraftbeyond.core.config.ConfigSettings;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FrameLoader {

    private static void parseFrameFile(FrameFile frames) {
        for (FrameFile.FrameEntry frameEntry : frames.entries) {
            try {
                parseFrameFileEntry(frameEntry);
            } catch (FrameException e) {
                BedCraftBeyond.LOGGER.error(e.getMessage());
            }
        }
    }

    private static void parseFrameFileEntry(FrameFile.FrameEntry entry) throws FrameException {
        if (entry.key == null || entry.key.trim().equalsIgnoreCase("")) return;
        if (entry.type == null)
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.parsing.no_type", entry.key));

        FrameRegistry.EnumFrameType type = FrameRegistry.EnumFrameType.parse(entry.type);
        if (type == FrameRegistry.EnumFrameType.UNKNOWN)
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.parsing.invalid_type", entry.key, entry.type));

        FrameWhitelist whitelist = FrameRegistry.getFrameWhitelist(type);
        ResourceLocation entryID = new ResourceLocation(entry.key);
        if(!Block.REGISTRY.containsKey(entryID))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.invalid_name", entry.key));

        if (!whitelist.hasEntryFor(entryID))
            whitelist.addEntry(entryID);

        try {
            FrameWhitelistEntry wlEntry = whitelist.getEntry(new ResourceLocation(entry.key));
            int added = whitelistFromEntry(entry, wlEntry);
            int removed = blacklistFromEntry(entry, wlEntry);

            BedCraftBeyond.LOGGER.info(String.format("Added %s entries and removed %s entries for registry name %s.", added, removed, entry.key));
        } catch (FrameException e) {
            BedCraftBeyond.LOGGER.error(e.getMessage());
        }

    }

    private static int whitelistFromEntry(FrameFile.FrameEntry entry, FrameWhitelistEntry whitelistEntry) throws FrameException {
        if (!entry.hasWhitelist()) return 0;

        int added = 0;
        Block b = Block.REGISTRY.getObject(whitelistEntry.getID());
        for (Object entryItem : entry.whitelist) {
            if (entryItem instanceof String) {
                String entryString = String.valueOf(entryItem);
                if (entryString.trim().equalsIgnoreCase("all")) {
                    whitelistEntry.reset();

                    List<ItemStack> addedList = new ArrayList<>();
                    b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
                    added += addedList.size();
                    break;
                }

                if (!entryString.contains("-")) continue;
                Pattern p = Pattern.compile("(?<lower>\\d+)\\-(?<upper>\\d+)");
                Matcher m = p.matcher(entryString);
                if (!m.matches()) continue;
                int lower = Integer.parseInt(m.group("lower"));
                int upper = Integer.parseInt(m.group("upper"));

                Range<Integer> range = Range.closed(lower, upper);
                whitelistEntry.whitelist(range);
                added += upper - lower + 1;
            } else if (entryItem instanceof Number) {
                whitelistEntry.whitelist(((Number) entryItem).intValue());
            } else {
                BedCraftBeyond.LOGGER.info("Unrecognized entry: " + entryItem.toString());
            }
        }

        return added;
    }

    private static int blacklistFromEntry(FrameFile.FrameEntry entry, FrameWhitelistEntry whitelistEntry) throws FrameException {
        if (!entry.hasBlacklist()) return 0;

        int removed = 0;

        Block b = Block.REGISTRY.getObject(whitelistEntry.getID());
        for (Object entryItem : entry.blacklist) {
            if (entryItem instanceof String) {
                String entryString = String.valueOf(entryItem);
                switch (entryString.toLowerCase()) {
                    case "all":
                        whitelistEntry.clear();
                        List<ItemStack> addedList = new ArrayList<>();
                        b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
                        removed += addedList.size();
                        break;

                    default:
                        // TODO: Meta range?
                        Pattern p = Pattern.compile("(?<lower>\\d+)\\-(?<upper>\\d+)");
                        Matcher m = p.matcher(entryString);
                        if (!m.matches()) continue;
                        int lower = Integer.parseInt(m.group("lower"));
                        int upper = Integer.parseInt(m.group("upper"));

                        Range<Integer> range = Range.closed(lower, upper);
                        whitelistEntry.blacklist(range);

                        break;
                }
            } else if (entryItem instanceof Number) {
                whitelistEntry.blacklist(((Number) entryItem).intValue());
            } else {

            }
        }

        return removed;
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
                else {
                    FrameWhitelistEntry entry = FrameRegistry.getFrameWhitelist(type).getEntry(regName);
                    entry.whitelist(stack.getMetadata());
                }
            } catch (FrameException e) {
                BedCraftBeyond.LOGGER.error(e.getMessage());
            }
        }
    }

    public static void tryFrameTypeFileLoad(File file) throws FrameException {
        if (!file.exists())
            throw new FrameException("Could not find frame file: " + file.getName());

        try {
            FileReader fr = new FileReader(file);
            FrameFile frames = new Gson().fromJson(fr, FrameFile.class);
            parseFrameFile(frames);
        } catch (Exception e) {
            BedCraftBeyond.LOGGER.error("There was an exception trying to load a frame file:");
            BedCraftBeyond.LOGGER.error(e);
        }
    }

    public static void compileFrames() {
        Logger l = BedCraftBeyond.LOGGER;

        FrameRegistry.dumpFrameList();
        if (ConfigSettings.ADD_OREDICT_WOODEN || ConfigSettings.ADD_OREDICT_STONE)
            l.info("Loading frame data from the ore dictionary...");
        if (ConfigSettings.ADD_OREDICT_WOODEN)
            addFramesFromOredictEntries(FrameRegistry.EnumFrameType.WOOD, "plankWood");
        if (ConfigSettings.ADD_OREDICT_STONE)
            addFramesFromOredictEntries(FrameRegistry.EnumFrameType.STONE, "blockStone");

        l.info("Loading frames from config files..");
        File framesDir = Paths.get(ConfigHelper.modConfigDir.getPath(), "frames").toFile();
        File[] framesFiles = framesDir.listFiles();

        for(File f : framesFiles){
            BedCraftBeyond.LOGGER.info("Trying to load frames from file: " + f.getName());
            try {
                tryFrameTypeFileLoad(f);
            } catch (FrameException e) {
                BedCraftBeyond.LOGGER.error("Error compiling from frame file: " + f.getName());
                BedCraftBeyond.LOGGER.error(e.getMessage());
            }
        }

        // TODO: Reimplement total available frame count?
    }
}
