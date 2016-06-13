package zornco.bedcraftbeyond.common.frames;


import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class FrameRegistry {

    // Key is an ALLOWED block registry type. If a meta value is in the RangeSet, then it is a valid block type.
    private FrameWhitelist woodFrames;
    private FrameWhitelist stoneFrames;

    /**
     * Specifies the available frame types for making beds and things.
     */
    public enum EnumFrameType {
        WOOD, STONE, UNKNOWN;

        public static List<String> getPossible(){
            ArrayList<String> possible = new ArrayList<>();
            for(EnumFrameType val : values())
                if(val != UNKNOWN) possible.add(val.name().toLowerCase());
            return possible;
        }
    }

    private static FrameRegistry INSTANCE = new FrameRegistry();

    private FrameRegistry() {
        woodFrames = new FrameWhitelist(EnumFrameType.WOOD);
        stoneFrames = new FrameWhitelist(EnumFrameType.STONE);
    }

    public static void dumpFrameList() {
        INSTANCE.woodFrames.resetWhitelist();
        INSTANCE.stoneFrames.resetWhitelist();
    }

    public static FrameWhitelist getFrameWhitelist(EnumFrameType type) {
        FrameWhitelist set = null;
        switch (type) {
            case WOOD:
                set = INSTANCE.woodFrames;
                break;
            case STONE:
                set = INSTANCE.stoneFrames;
                break;
        }

        return set;
    }

    /**
     * Checks if a given itemStack is registered in the registry for a given type.
     * This checks item metadata and hopes that the meta is the same as the blockstate.
     * If not, please use the method with a direct check for meta instead.
     *
     * @param type
     * @param stack
     * @return
     */
    // TODO: Check if this has issues with mods
    public static boolean isValidFrameMaterial(EnumFrameType type, ItemStack stack) {
        if(stack == null) return false;
        FrameWhitelist set = getFrameWhitelist(type);
        Block b = Block.getBlockFromItem(stack.getItem());
        ResourceLocation rl = b.getRegistryName();

        return isValidFrameMaterial(type, rl, stack.getMetadata());
    }

    public static boolean isValidFrameMaterial(EnumFrameType type, ResourceLocation registryName, int meta) {
        FrameWhitelist set = getFrameWhitelist(type);
        return set.hasEntryFor(registryName) && set.metaIsWhitelisted(registryName, meta);
    }

    /**
     * Checks if a given blockstate can be used to build a bed.
     * Basically just grabs the registry name and meta from the state and checks it.
     *
     * @param type
     * @param state
     * @return
     */
    public static boolean isValidFrameMaterial(EnumFrameType type, IBlockState state){
        return isValidFrameMaterial(type, state.getBlock().getRegistryName(), state.getBlock().getMetaFromState(state));
    }


}
