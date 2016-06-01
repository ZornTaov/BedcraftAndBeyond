package zornco.bedcraftbeyond.common.frames;

import com.google.gson.Gson;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;
import zornco.bedcraftbeyond.BedCraftBeyond;
import zornco.bedcraftbeyond.config.ConfigHelper;
import zornco.bedcraftbeyond.config.ConfigSettings;

import java.io.*;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;

public class FrameLoader {

    private static int removeFramesFromStream(FrameFile frames, FrameRegistry.EnumFrameType type) {
        int removed = 0;
        for (String entry : frames.blacklist) {
            ResourceLocation entryRes = new ResourceLocation(entry);
            Block b = Block.REGISTRY.getObject(entryRes);
            if (b != null) {
                if (entry.contains("@")) {
                    // TODO: Implement metadata only list
                } else {
                    FrameRegistry.getFrameWhitelist(type).resetWhitelistForEntry(b.getRegistryName());
                    List<ItemStack> addedList = new ArrayList<>();
                    b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
                    removed += addedList.size();
                }
            }
        }

        // TODO: Add check to see if registry value supported, and check meta list for block type (minecraft:planks -> Spruce Wood, for example)
        return removed;
    }

    private static int addToFramesFromStream(FrameFile frames, FrameRegistry.EnumFrameType type) {
        int added = 0;
        for (String entry : frames.whitelist) {
            ResourceLocation entryRes = new ResourceLocation(entry);
            Block b = Block.REGISTRY.getObject(entryRes);
            if (b != null) {
                if (entry.contains("@")) {
                    // TODO: Implement metadata only list
                } else {
                    try {
                        FrameRegistry.getFrameWhitelist(type).addEntry(b.getRegistryName());
                    } catch (FrameException e) {
                        BedCraftBeyond.logger.error("Error adding entry to the frame registry: " + e.getMessage());
                    }

                    List<ItemStack> addedList = Collections.emptyList();
                    b.getSubBlocks(Item.getItemFromBlock(b), null, addedList);
                    added += addedList.size();
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
                BedCraftBeyond.logger.error("Found a null registry entry trying to add oreDict frames: " + stack.toString()
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
                BedCraftBeyond.logger.error(e);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void compileFramesClient() {
        Logger l = BedCraftBeyond.logger;
        IResourceManager man = Minecraft.getMinecraft().getResourceManager();
        try {
            FrameRegistry.dumpFrameList();
            if (ConfigSettings.ADD_OREDICT_WOODEN || ConfigSettings.ADD_OREDICT_STONE)
                l.info("Loading frame data from the ore dictionary...");
            if (ConfigSettings.ADD_OREDICT_WOODEN)
                addFramesFromOredictEntries(FrameRegistry.EnumFrameType.WOOD, "plankWood");

            l.info("");

            l.info("Loading frames from resource packs..");
            IResource respack = man.getResource(new ResourceLocation(BedCraftBeyond.MOD_ID, "wooden_frames.json"));
            l.info("Got wooden frames json from pack " + respack.getResourcePackName());
            InputStream woodenStream = respack.getInputStream();
            FrameFile woodFrames = new Gson().fromJson(new InputStreamReader(woodenStream), FrameFile.class);

            int num_add = addToFramesFromStream(woodFrames, FrameRegistry.EnumFrameType.WOOD);
            int num_rem = removeFramesFromStream(woodFrames, FrameRegistry.EnumFrameType.WOOD);

            BedCraftBeyond.logger.info("wooden_frames.json results: +" + num_add + " -" + num_rem);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Reimplement total available frame count?
    }

    @SideOnly(Side.SERVER)
    public static void compileFramesServer() {
        if (ConfigSettings.ADD_OREDICT_WOODEN)
            addFramesFromOredictEntries(FrameRegistry.EnumFrameType.WOOD, "plankWood");

        File woodenFramesFile = Paths.get(ConfigHelper.modConfigDir.getPath(), "wooden_frames.json").toFile();
        if (woodenFramesFile.exists()) {
            // TODO: wooden_frames.json server integration
        }
    }

}
