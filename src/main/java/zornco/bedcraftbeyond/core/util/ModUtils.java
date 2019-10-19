package zornco.bedcraftbeyond.core.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
/*import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;

public class ModUtils {

    public static ModContainer getModForBlock(Block b){
        return getModForRL(b.getRegistryName());
    }

    public static ModContainer getModForItem(Item i){
        return getModForRL(i.getRegistryName());
    }

    public static ModContainer getModForRL(ResourceLocation rl) {
        String modid = rl.getNamespace();
        for (ModContainer container : Loader.instance().getActiveModList()) {
            if (modid.equals(container.getModId())) {
                return container;
            }
        }
        return null;
    }

}
*/