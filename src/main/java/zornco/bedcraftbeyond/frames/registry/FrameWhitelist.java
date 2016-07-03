package zornco.bedcraftbeyond.frames.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import zornco.bedcraftbeyond.core.BedCraftBeyond;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrameWhitelist {

    private FrameRegistry.EnumFrameType type;
    private HashMap<ResourceLocation, FrameWhitelistEntry> entries;

    public FrameWhitelist(FrameRegistry.EnumFrameType type) {
        this.type = type;
        entries = new HashMap<>();
    }

    public Set<ResourceLocation> getValidRegistryEntries(){
        return entries.keySet();
    }

    public String getTerminalOutput(){
        if(entries.isEmpty()) return "{{EMPTY}}";
        String output = "";
        for(Map.Entry<ResourceLocation, FrameWhitelistEntry> set : entries.entrySet()){
            output += " " + set.getKey() + ": {" + set.getValue().toString() + "},";
        }

        return output.substring(1, output.length() - 1);
    }

    /**
     * Checks if a whitelist exists for a registry name.
     *
     * @param registryName
     * @return
     */
    public boolean hasEntryFor(ResourceLocation registryName) {
        return entries.containsKey(registryName);
    }

    public FrameWhitelistEntry getEntry(ResourceLocation registryName) throws FrameException {
        if(!entries.containsKey(registryName))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.not_valid_entry", registryName));
        return entries.get(registryName);

    }

    public void resetWhitelist() {
        entries.clear();
    }

    /**
     * Adds an entry to the whitelist with a clear blacklist on meta.
     *
     * @param registryName
     * @return
     */
    public boolean addEntry(ResourceLocation registryName) throws FrameException {
        if (entries.containsKey(registryName))
            throw new FrameException(I18n.translateToLocalFormatted(BedCraftBeyond.MOD_ID + ".frames.errors.already_added", type.name().toLowerCase()));

        entries.put(registryName, new FrameWhitelistEntry(registryName));
        return true;
    }

    /**
     * Completely removes a given entry from the whitelist.
     *
     * @param registryName
     * @return
     */
    public boolean removeEntry(ResourceLocation registryName) {
        if (entries.containsKey(registryName)) {
            entries.remove(registryName);
            return true;
        }

        return false;
    }
}
