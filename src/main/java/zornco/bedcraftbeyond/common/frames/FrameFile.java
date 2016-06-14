package zornco.bedcraftbeyond.common.frames;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a user-defined white and blacklist for frames.
 */
public class FrameFile {

    public String type;
    public FrameEntry[] entries;

    public static class FrameEntry {
        public String key;
        public Object[] whitelist;
        public Object[] blacklist;

        public boolean hasWhitelist(){ return whitelist != null; }
        public boolean hasBlacklist(){ return blacklist != null; }
    }
}
