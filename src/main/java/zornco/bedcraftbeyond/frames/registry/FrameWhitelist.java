package zornco.bedcraftbeyond.frames.registry;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
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

    public void writeListToConsole(ICommandSender sender){
        if(entries.isEmpty()) sender.addChatMessage(new TextComponentTranslation(BedCraftBeyond.MOD_ID + ".errors.no_frames_whitelisted"));

        for(Map.Entry<ResourceLocation, FrameWhitelistEntry> set : entries.entrySet()){
            sender.addChatMessage(new TextComponentString(
                TextFormatting.GREEN + "" + TextFormatting.ITALIC + set.getKey().toString() +
                    TextFormatting.RESET + ": " + set.getValue().getValidMeta().toString() + "}"));
        }
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
