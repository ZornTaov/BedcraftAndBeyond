package zornco.bedcraftbeyond.beds.frames.registry;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.oredict.OreDictionary;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

import java.util.Set;

public class FrameWhitelistEntry {

    private ResourceLocation id;
    private RangeSet<Integer> meta;

    public FrameWhitelistEntry(ResourceLocation id) {
        this.id = id;
        this.meta = TreeRangeSet.create();
    }

    public ResourceLocation getID(){
        return this.id;
    }

    public RangeSet<Integer> getValidMeta() {
        return meta;
    }

    public void clear() {
        this.meta.clear();
    }

    /**
     * Resets a whitelist for an entry back to including all valid meta (0-15, closed-open).
     * If the tracked id is vanilla's planks, this removes the extra "ghost" plank entries. (6-15)
     */
    public void reset() {
        if (id.equals(new ResourceLocation("minecraft:planks"))) {
            whitelist(Range.closed(0, 5));
        } else
            whitelist(Range.closed(0, 15));
    }

    /**
     * Adds a specific meta index to an entry's whitelist.
     * If passed OreDictionary.WILDCARD_VALUE, this method resets the whitelist for an entry.
     *
     * @param meta
     * @return
     * @throws FrameException
     */
    public boolean whitelist(int meta) throws FrameException {
        if (meta < 0)
            throw new FrameException(BedCraftBeyond.MOD_ID + ".frames.errors.invalid_meta", id, meta);

        if (meta == OreDictionary.WILDCARD_VALUE) {
            reset();
            return true;
        }

        if (isWhitelisted(meta))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.already_added_meta", id, meta, ""));

        return whitelist(Range.closed(meta, meta));
    }

    /**
     * Whitelists a range of metadata.
     *
     * @param range The range of metadata to whitelist.
     * @return
     */
    public boolean whitelist(Range<Integer> range) {
        meta.add(range);
        return isWhitelisted(range);
    }

    /**
     * Checks if a given metadata value is whitelisted under an entry.
     *
     * @param meta
     * @return
     */
    public boolean isWhitelisted(int meta) {
        return this.meta.contains(meta);
    }

    /**
     * Checks if a given range is whitelisted under an entry.
     *
     * @param range
     * @return
     */
    public boolean isWhitelisted(Range<Integer> range) {
        return this.meta.encloses(range);
    }

    /**
     * Removes a specific metadata entry from a given registry entry's whitelist.
     * If passed OreDictionary.WILDCARD_VALUE, it clears the whitelist for an entry.
     *
     * @param meta    A specific meta index to remove from the whitelist. If equal to {@see OreDictionary.WILDCARD_VALUE}
     *                then this completely removes the entry from the specified whitelist.
     * @return
     */
    public boolean blacklist(int meta) {
        if (meta == OreDictionary.WILDCARD_VALUE) {
            clear();
            return true;
        }

        return blacklist(Range.closed(meta, meta));
    }

    /**
     * Opposite of whitelist, this blacklists a range of metadata.
     *
     * @param range
     * @return True if the blacklisting was successful, otherwise false.
     */
    public boolean blacklist(Range<Integer> range) {
        this.meta.remove(range);
        return !this.isWhitelisted(range);
    }
}
