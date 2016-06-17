package zornco.bedcraftbeyond.frames;

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
