package zornco.bedcraftbeyond.common.frames;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.BedCraftBeyond;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrameWhitelist {

    private FrameRegistry.EnumFrameType type;
    private HashMap<ResourceLocation, RangeSet<Integer>> whitelist;

    public FrameWhitelist(FrameRegistry.EnumFrameType type) {
        this.type = type;
        whitelist = new HashMap<>();
    }

    public Set<ResourceLocation> getValidRegistryEntries(){
        return whitelist.keySet();
    }

    public String getTerminalOutput(){
        if(whitelist.isEmpty()) return "{{EMPTY}}";
        String output = "";
        for(Map.Entry<ResourceLocation, RangeSet<Integer>> set : whitelist.entrySet()){
            output += " " + set.getKey() + ": {" + set.getValue().toString() + "},";
        }

        return output.substring(1, output.length() - 1);
    }

    public Set<Range<Integer>> getValidMetaForEntry(ResourceLocation registryName) throws FrameException {
        if(!hasEntryFor(registryName)) throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.not_valid_entry", registryName));
        return whitelist.get(registryName).asRanges();
    }

    /**
     * Checks if a whitelist exists for a registry name.
     *
     * @param registryName
     * @return
     */
    public boolean hasEntryFor(ResourceLocation registryName) {
        return whitelist.containsKey(registryName);
    }

    /**
     * Checks if a given metadata value is whitelisted under an entry.
     *
     * @param registryName
     * @param meta
     * @return
     */
    public boolean metaIsWhitelisted(ResourceLocation registryName, int meta) {
        return metaRangeIsWhitelisted(registryName, Range.closed(meta, meta));
    }

    /**
     * Checks if a given range is whitelisted under an entry.
     *
     * @param registryName
     * @param range
     * @return
     */
    public boolean metaRangeIsWhitelisted(ResourceLocation registryName, Range range) {
        if (!whitelist.containsKey(registryName)) return false;
        return whitelist.get(registryName).encloses(range);
    }

    public void resetWhitelist() {
        whitelist.clear();
    }

    /**
     * Resets a whitelist for an entry back to including all valid meta (0-15, closed-open).
     *
     * @param registryName The entry to reset.
     */
    public void resetWhitelistForEntry(ResourceLocation registryName) {
        if (whitelist.containsKey(registryName)) whitelist.get(registryName).clear();
        else whitelist.put(registryName, TreeRangeSet.create());

        whitelist.get(registryName).add(Range.closedOpen(0, 15));
    }

    /**
     * Adds an entry to the whitelist with a clear blacklist on meta.
     *
     * @param registryName
     * @return
     */
    public boolean addEntry(ResourceLocation registryName) throws FrameException {
        if (whitelist.containsKey(registryName))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.already_added", type.name().toLowerCase()));

        resetWhitelistForEntry(registryName);
        return true;
    }

    /**
     * Adds a specific meta index to an entry's whitelist.
     * If passed OreDictionary.WILDCARD_VALUE, this method resets the whitelist for an entry.
     *
     * If the whitelist does not contain an entry, this method also adds it.
     *
     * @param registryName
     * @param meta
     * @return
     * @throws FrameException
     */
    public boolean addWhitelistEntry(ResourceLocation registryName, int meta) throws FrameException {
        // DO not keep instances of meta < 0 in registry
        if (meta < 0)
            throw new FrameException(BedCraftBeyond.MOD_ID + ".frames.errors.invalid_meta", registryName, meta);

        if (!whitelist.containsKey(registryName))
            whitelist.put(registryName, TreeRangeSet.create());

        if (meta == OreDictionary.WILDCARD_VALUE) {
            resetWhitelistForEntry(registryName);
            return true;
        }

        if (metaIsWhitelisted(registryName, meta))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.already_added_meta", registryName, meta, type.name().toLowerCase()));

        addWhitelistRange(registryName, Range.closed(meta, meta));
        return whitelist.get(registryName).contains(meta);
    }

    public boolean addWhitelistRange(ResourceLocation registryName, Range<Integer> range) throws FrameException {
        if(!whitelist.containsKey(registryName))
            whitelist.put(registryName, TreeRangeSet.create());

        whitelist.get(registryName).add(range);
        return whitelist.get(registryName).encloses(range);
    }

    /**
     * Completely removes a given entry from the whitelist.
     *
     * @param registryName
     * @return
     */
    public boolean removeEntry(ResourceLocation registryName) {
        if (whitelist.containsKey(registryName)) {
            whitelist.remove(registryName);
            return true;
        }

        return false;
    }

    /**
     * Removes a specific metadata entry from a given registry entry's whitelist.
     * If passed OreDictionary.WILDCARD_VALUE, it clears the whitelist for an entry.
     *
     * @param regName
     * @param meta    A specific meta index to remove from the whitelist. If equal to {@see OreDictionary.WILDCARD_VALUE}
     *                then this completely removes the entry from the specified whitelist.
     * @return
     */
    public boolean removeWhitelistEntry(ResourceLocation regName, int meta) throws FrameException {
        if (whitelist.containsKey(regName)) {
            if (meta == OreDictionary.WILDCARD_VALUE) {
                whitelist.get(regName).clear();
                return true;
            }

            return removeWhitelistEntries(regName, Range.closed(meta, meta));
        }

        throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.not_valid_entry", regName));
    }

    /**
     * Removes a whitelist of integers (a range) from the whitelist for a given entry.
     *
     * @param regName
     * @param range   The range of metadata to remove from the whitelist.
     * @return
     */
    public boolean removeWhitelistEntries(ResourceLocation regName, Range<Integer> range) throws FrameException {
        if (!whitelist.containsKey(regName))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.not_valid_entry", regName));
        whitelist.get(regName).remove(range);
        return !whitelist.get(regName).encloses(range);
    }
}
