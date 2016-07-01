package zornco.bedcraftbeyond.beds.frames.registry;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;

/**
 * Represents a user-defined white and blacklist for frames.
 */
public class FrameFile {

    public FrameEntry[] entries;

    public static class FrameEntry {
        public String key;
        public String type;
        public Object[] whitelist;
        public Object[] blacklist;

        public boolean hasWhitelist(){ return whitelist != null; }
        public boolean hasBlacklist(){ return blacklist != null; }
    }
}
